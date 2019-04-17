package com.github.common.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Component
public class JavaJWT {

    private static String JWT_TOKEN_KEY;
    private static final String SECRET = "^@GSF%@#AN";
    private final String issuer = "spring-project";
    private final UserInfoForJWT userInfoForJWT;

    @Value("${jwt.token.key:Authorization}")
    public void setJWT_TOKEN_KEY(String a) {
        JWT_TOKEN_KEY = a;
    }

    @Autowired
    public JavaJWT(UserInfoForJWT userSecretForJWT) {
        this.userInfoForJWT = userSecretForJWT;
    }

    /**
     * @param expiresDayFromNow 有效时间（天）
     */
    public String createToken(Object userId, int expiresDayFromNow) throws Exception {
        if (userId == null) {
            throw new Exception("Error when create token, id is null.");
        }
        JWTCreator.Builder builder = JWT.create();
        //添加发布人信息【可直接解析】
        builder.withIssuer(issuer);
        builder.withExpiresAt(toDate(LocalDateTime.now().plusDays(expiresDayFromNow)));
        //添加claim附加信息【可直接解析】
        builder.withClaim("id", String.valueOf(userId));
        builder.withClaim("duration", expiresDayFromNow);
//      builder.withArrayClaim("roleList", roleList.toArray(new String[]{}));
//      builder.withArrayClaim("permissionList", permissionList.toArray(new String[]{}));
        String token = builder.sign(Algorithm.HMAC256(secretBuilder(userId)));
        log.debug("CREATE TOKEN：" + token);
        return token;
    }

    public String updateToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            if (decodedJWT.getClaim("duration").isNull()) {
                throw new Exception("Error when update token, no duration.");
            }
            if (decodedJWT.getClaim("id").isNull()) {
                throw new Exception("Error when update token, no id.");
            }
            JWTCreator.Builder builder = JWT.create();
            builder.withIssuer(issuer);
            builder.withExpiresAt(toDate(LocalDateTime.now().plusDays(decodedJWT.getClaim("duration").asInt())));
            builder.withClaim("id", decodedJWT.getClaim("id").asString());
            builder.withClaim("duration", decodedJWT.getClaim("duration").asInt());
//          builder.withArrayClaim("roleList", decodedJWT.getClaim("roleList").asList(String.class).toArray(new String[]{}));
//          builder.withArrayClaim("permissionList", decodedJWT.getClaim("permissionList").asList(String.class).toArray(new String[]{}));
            String newToken = builder.sign(Algorithm.HMAC256(secretBuilder(decodedJWT.getClaim("id").asString())));
            log.debug("UPDATE TOKEN：" + newToken);
            return newToken;
        } catch (Exception e) {
            log.error("UPDATE TOKEN：" + e.getMessage());
            return null;
        }
    }

    public void updateTokenAndSetHeader(String token, HttpServletResponse httpServletResponse) {
        token = updateToken(token);
        if (token != null) {
            httpServletResponse.setHeader(JWT_TOKEN_KEY, token);
        }
    }

    public boolean verifyToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            String id = decodedJWT.getClaim("id").asString();
            Algorithm algorithm = Algorithm.HMAC256(secretBuilder(id));
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();
            verifier.verify(token);
        } catch (Exception ignored) {
            return false;
        }
        return true;
    }

    private byte[] secretBuilder(Object userId) {
        String userSecret = userInfoForJWT.getSecret(userId);
        return (SECRET + userSecret).getBytes();
    }

    /**
     * @return int剩余有效时间(分钟)
     */
    public long getTokenRemainingTime(String token) {
        DecodedJWT jwt = JWT.decode(token);
        Date expiresDate = jwt.getExpiresAt();
        if (expiresDate == null) {
            return -1;
        } else {
            LocalDateTime expiresLocalDateTime = expiresDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            Duration duration = Duration.between(LocalDateTime.now(), expiresLocalDateTime);
            return duration.toDays();
        }
    }

    public String getId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("id").asString();
        } catch (Exception e) {
            return null;
        }
    }

    public String getId(HttpServletRequest httpServletRequest) {
        try {
            String token = httpServletRequest.getHeader(JWT_TOKEN_KEY);
            return getId(token);
        } catch (Exception e) {
            return null;
        }
    }

    //    public static List<String> getRoleList(String token) {
//        DecodedJWT jwt = JWT.decode(token);
//        return jwt.getClaim("roleList").asList(String.class);
//    }
//
//    public static List<String> getPermissionList(String token) {
//        DecodedJWT jwt = JWT.decode(token);
//        return jwt.getClaim("permissionList").asList(String.class);
//    }
    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
