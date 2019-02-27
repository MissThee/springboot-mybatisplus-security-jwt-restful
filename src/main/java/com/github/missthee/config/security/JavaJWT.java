package com.github.missthee.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.github.missthee.db.primary.model.basic.User;
import com.github.missthee.service.interf.basic.UserService;

import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Component
public class JavaJWT {
    private final String issuer = "spring-project";

    private final UserService userService;

    @Autowired
    public JavaJWT(UserService userService) {
        this.userService = userService;
    }

    /**
     * @param expiresDayFromNow 有效时间（天）
     */
    public String createToken(String id, int expiresDayFromNow) {
        JWTCreator.Builder builder = JWT.create();
        //添加发布人信息【可直接解析】
        builder.withIssuer(issuer);
        builder.withExpiresAt(toDate(LocalDateTime.now().plusDays(expiresDayFromNow)));
        //添加claim附加信息【可直接解析】
        builder.withClaim("id", id);
        builder.withClaim("duration", expiresDayFromNow);
//            builder.withArrayClaim("roleList", roleList.toArray(new String[]{}));
//            builder.withArrayClaim("permissionList", permissionList.toArray(new String[]{}));
        //添加header键值对【可直接解析】
        //Map<String, Object> headerClaims = new HashMap();
        //headerClaims.put("userId", "1234");
        //builder.withHeader(headerClaims);
        User user = userService.selectOneById(Integer.parseInt(id));
        String token = builder.sign(Algorithm.HMAC256(user.getPassword().getBytes()));
        log.debug("CREATE TOKEN：" + token);
        return token;
    }

    public String updateToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            if (decodedJWT.getClaim("duration").isNull()) {
                log.debug("REFRESH TOKEN：[no duration]");
                return null;
            }
            if (decodedJWT.getClaim("id").isNull()) {
                log.debug("REFRESH TOKEN：[no id]");
                return null;
            }
            JWTCreator.Builder builder = JWT.create();
            builder.withIssuer(issuer);
            builder.withExpiresAt(toDate(LocalDateTime.now().plusDays(decodedJWT.getClaim("duration").asInt())));
            String id = decodedJWT.getClaim("id").asString();
            builder.withClaim("id", id);
            builder.withClaim("duration", decodedJWT.getClaim("duration").asInt());
//      builder.withArrayClaim("roleList", decodedJWT.getClaim("roleList").asList(String.class).toArray(new String[]{}));
//      builder.withArrayClaim("permissionList", decodedJWT.getClaim("permissionList").asList(String.class).toArray(new String[]{}));
            User user = userService.selectOneById(Integer.parseInt(id));
            String newToken = builder.sign(Algorithm.HMAC256(user.getPassword().getBytes()));
            log.debug("REFRESH TOKEN：" + newToken);
            return newToken;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("REFRESH TOKEN：[ERROR!]");
            return null;
        }
    }

    public void updateTokenAndSetHeader(String token, HttpServletResponse httpServletResponse) {
        token = updateToken(token);
        if (token != null) {
            httpServletResponse.setHeader("Authorization", token);
        }
    }

    public boolean verifyToken(String token) {
        if (StringUtils.isEmpty(token)) {
            log.debug("CHECK TOEKN: NULL");
            return false;
        }
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            String id = decodedJWT.getClaim("id").asString();
            User user = userService.selectOneById(Integer.parseInt(id));
            Algorithm algorithm = Algorithm.HMAC256(user.getPassword().getBytes());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();
            verifier.verify(token);
            log.debug("CHECK TOEKN: Fine");
            return true;
        } catch (Exception exception) {
            log.debug("CHECK TOEKN-ERROR: " + exception);
            return false;
        }
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
