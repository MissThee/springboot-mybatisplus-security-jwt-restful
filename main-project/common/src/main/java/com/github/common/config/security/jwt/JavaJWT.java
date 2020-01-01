package com.github.common.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Slf4j
@Component
public class JavaJWT {

    private static final String SECRET = "^@GSF%@#AN";
    public static final String JWT_TOKEN_KEY = "Authorization";
    private final String issuer = "spring-project";
    private final UserInfoForJWT userInfoForJWT;

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
        builder.withExpiresAt(getExpireDate(expiresDayFromNow));
        //添加claim附加信息【可直接解析】
        builder.withClaim("id", String.valueOf(userId));
        builder.withClaim("duration", expiresDayFromNow);
        String token = builder.sign(Algorithm.HMAC256(secretBuilder(userId)));
        log.debug("CREATE TOKEN：" + token);
        return token;
    }
    /**
     * 传入一个token，返回一个新的未过期token，并保持原token中所有参数
     */
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
            builder.withExpiresAt(getExpireDate(decodedJWT.getClaim("duration").asInt()));
            builder.withClaim("id", decodedJWT.getClaim("id").asString());
            builder.withClaim("duration", decodedJWT.getClaim("duration").asInt());
            String newToken = builder.sign(Algorithm.HMAC256(secretBuilder(decodedJWT.getClaim("id").asString())));
            log.debug("UPDATE TOKEN：" + newToken);
            return newToken;
        } catch (Exception e) {
            log.error("UPDATE TOKEN：" + e.getMessage());
            return null;
        }
    }

    //更新一个token，并将其设置到返回值的header中，有效期为Integer.MAX_VALUE分钟。基本等于永不过期了
    public void updateTokenAndSetHeader(String token) {
        updateTokenAndSetHeaderWithAvailableMinute(token, Integer.MAX_VALUE);
    }

    //更新一个token，并将其设置到返回值的header中，有效期为minute分钟
    public void updateTokenAndSetHeaderWithAvailableMinute(String token, int minute) {
        long tokenRemainingTime = getTokenRemainingTime(token);
        if (tokenRemainingTime < minute && tokenRemainingTime >= 0) {
            token = updateToken(token);
            if (token != null) {
                HttpServletResponse httpServletResponse = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
                if (httpServletResponse != null) {
                    httpServletResponse.setHeader(JWT_TOKEN_KEY, token);
                }
            }
        }
    }

    //验证token。异常为 JWTVerificationException 时 token 验证未通过。
    //若出现JWTVerificationException之外的异常则表示验证方法本身出现了错误，此时直接抛出异常，前端会接收到状态为500返回值，应该需要调整后台。
    public Boolean verifyToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return false; //验证未通过
        }
        try {
            DecodedJWT decodedJWT;
            try {
                decodedJWT = JWT.decode(token);
            } catch (Exception e) {
                return false; //验证未通过
            }
            Map<String, Claim> claims = decodedJWT.getClaims();
            if (!claims.containsKey("id")) {
                return false; //验证未通过
            }
            String id = claims.get("id").asString();
            Algorithm algorithm = Algorithm.HMAC256(secretBuilder(id));
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();
            verifier.verify(token);
        } catch (JWTVerificationException ignored) {
            return false; //验证未通过
        }
        return true; //验证通过
    }

    private byte[] secretBuilder(Object userId) {
        String userSecret = userInfoForJWT.getSecret(userId);
        return (SECRET + userSecret).getBytes();
    }

    /**
     * @return int剩余有效时间(分钟)
     */
    public static long getTokenRemainingTime(String token) {
        DecodedJWT jwt = JWT.decode(token);
        Date expiresDate = jwt.getExpiresAt();
        if (expiresDate == null) {
            return -1;
        } else {
            return (int) (expiresDate.getTime() - new Date().getTime()) / (1000 * 60);
        }
    }

    public static String getId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("id").asString();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getId(HttpServletRequest httpServletRequest) {
        try {
            String token = httpServletRequest.getHeader(JWT_TOKEN_KEY);
            return getId(token);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getId() {
        try {
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            return getId(httpServletRequest);
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

    private static Date getExpireDate(int expireDayFromNow) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        instance.add(Calendar.DATE, expireDayFromNow);
        return instance.getTime();
    }
}
