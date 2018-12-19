package server.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
public class JavaJWT {
    private static final String issuer = "issuer";
    private static final String secret = "secret20180718";

    /**
     * @param expiresDayFromNow 有效时间（天）
     */
    public static String createToken(String id, int expiresDayFromNow) {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        JWTCreator.Builder builder = JWT.create();
        //添加发布人信息【可直接解析】
        builder.withIssuer(issuer);
        builder.withExpiresAt(toDate(LocalDateTime.now().plusDays(expiresDayFromNow)));
        //添加claim附加信息【可直接解析】
        builder.withClaim("id", id);
//            builder.withArrayClaim("roleList", roleList.toArray(new String[]{}));
//            builder.withArrayClaim("permissionList", permissionList.toArray(new String[]{}));
        //添加header键值对【可直接解析】
        //Map<String, Object> headerClaims = new HashMap();
        //headerClaims.put("userId", "1234");
        //builder.withHeader(headerClaims);
        String token = builder.sign(algorithm);
        log.debug("CREATE TOKEN：" + token);
        return token;
    }

    public static String updateToken(String token, int expiresDayFromNow) {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        JWTCreator.Builder builder = JWT.create();
        builder.withIssuer(issuer);
        builder.withExpiresAt(toDate(LocalDateTime.now().plusDays(expiresDayFromNow)));
        DecodedJWT decodedJWT = JWT.decode(token);

        for (String claimKey : decodedJWT.getClaims().keySet()) {
            switch (claimKey) {
                case "exp":
                case "iss":
                    break;
                case "id":
                    builder.withClaim(claimKey, decodedJWT.getClaim(claimKey).asString());
                    break;
//                    case "roleList":
//                    case "permissionList":
//                        builder.withArrayClaim(claimKey, decodedJWT.getClaim(claimKey).asList(String.class).toArray(new String[]{}));
//                        break;
            }
        }
        String newToken = builder.sign(algorithm);
        log.debug("REFRESH TOKEN：" + newToken);
        return newToken;
    }

    public static boolean verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
        try {
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
    public static long getTokenRemainingTime(String token) {
        DecodedJWT jwt = JWT.decode(token);
        Date expiresDate = jwt.getExpiresAt();
        if (expiresDate == null) {
            return 999999;
        }
        LocalDateTime expiresLocalDateTime = expiresDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        Duration duration = Duration.between(LocalDateTime.now(), expiresLocalDateTime);
        return duration.toMinutes();
    }

    public static String getId(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("id").asString();
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

    private static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
