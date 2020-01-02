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
    //实际此值与用户密码（加密后）进行字符串拼接之后，作为jwt签发/验证token时的secret值。此值不会被直接解析，且不应外泄。
    private static final String SECRET = "^@GSF%@#AN";
    //token的issuer参数，签发/验证时也会使用此值。此值可被直接解析。
    private final String ISSUER = "spring-project";
    //约定的，http请求参数中header部分token存放的key。
    public static final String JWT_TOKEN_KEY = "Authorization";
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
        builder.withIssuer(ISSUER);
        builder.withExpiresAt(getExpireDate(expiresDayFromNow));
        //添加claim附加信息【可直接解析】
        builder.withClaim("id", String.valueOf(userId));//用户信息：token中仅携带用户id，服务端要使用用户其他信息（如：姓名，所属单位等），由此id在后台数据库自行查找。因每次访问都需要查找用户信息，用户信息最好使用缓存，避免频繁查询数据库
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
            builder.withIssuer(ISSUER);
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

    /**
     * 更新一个token，并将其设置到返回值的header中，有效期为Integer.MAX_VALUE分钟。基本等于永不过期了
     */
    public void updateTokenAndSetHeader(String token) {
        updateTokenAndSetHeaderWithAvailableMinute(token, Integer.MAX_VALUE);
    }

    /**
     * 更新一个token，并将其设置到返回值的header中，有效期为minute分钟
     */
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

    /**
     * 验证token。若验证方法异常为 JWTVerificationException 时， token 验证未通过。
     * 若出现JWTVerificationException之外的异常则表示验证方法本身出现了错误，此时直接抛出异常，前端表现为会接收到状态为500返回值，应该需要调整后台。
     */
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
                    .withIssuer(ISSUER)
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

    /**
     * 从token中获取id值
     */
    public static String getId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("id").asString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从请求参数的header中的token中获取用户id值
     */
    public static String getId(HttpServletRequest httpServletRequest) {
        try {
            String token = httpServletRequest.getHeader(JWT_TOKEN_KEY);
            return getId(token);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从当前线程的请求参数的header中的token中获取用户id值
     */
    public static String getId() {
        try {
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            return getId(httpServletRequest);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 以当前时间为基础，获取此时间expireDayFromNow天之后的日期
     */
    private static Date getExpireDate(int expireDayFromNow) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        instance.add(Calendar.DATE, expireDayFromNow);
        return instance.getTime();
    }
}
