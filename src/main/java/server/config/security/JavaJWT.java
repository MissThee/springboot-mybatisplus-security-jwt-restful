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
import java.util.*;

@Slf4j
public class JavaJWT {
    private static final String issuer = "issuer";       //发布者
    private static final String secret = "secret20180718";       //发布者

    /**
     * @param expiresDayFromNow 有效时间（天）
     */
    public static String createToken(Integer id,  int expiresDayFromNow) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTCreator.Builder builder = JWT.create();
            //添加发布人信息【可直接解析】
            builder.withIssuer(issuer);
            builder.withExpiresAt(now(expiresDayFromNow));
            //添加claim附加信息【可直接解析】
            builder.withClaim("id", id);
//            builder.withArrayClaim("roleList", roleList.toArray(new String[]{}));
//            builder.withArrayClaim("permissionList", permissionList.toArray(new String[]{}));
            //添加header键值对【可直接解析】
            //Map<String, Object> headerClaims = new HashMap();
            //headerClaims.put("userId", "1234");
            //builder.withHeader(headerClaims);
            String token = builder.sign(algorithm);
            log.info("CREATE TOKEN：" + token);
            return token;
        } catch (UnsupportedEncodingException exception) {
            // UTF-8 encoding not supported
        } catch (JWTCreationException exception) {
            // Invalid Signing configuration / Couldn‘t convert Claims.
        }
        return "";
    }

    public static String createToken(Integer id, List<String> roleList, List<String> permissionList) {
        return createToken(id,   1);
    }

    public static String updateToken(String token, int expiresDayFromNow) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTCreator.Builder builder = JWT.create();
            builder.withIssuer(issuer);
            builder.withExpiresAt(now(expiresDayFromNow));
            DecodedJWT decodedJWT = JWT.decode(token);

            for (String claimKey : decodedJWT.getClaims().keySet()) {
                switch (claimKey) {
                    case "exp":
                    case "iss":
                        break;
                    case "id":
                        builder.withClaim(claimKey, decodedJWT.getClaim(claimKey).asInt());
                        break;
//                    case "roleList":
//                    case "permissionList":
//                        builder.withArrayClaim(claimKey, decodedJWT.getClaim(claimKey).asList(String.class).toArray(new String[]{}));
//                        break;
                }
            }
            String newToken = builder.sign(algorithm);
            log.info("REFRESH TOKEN：" + newToken);
            return newToken;
        } catch (UnsupportedEncodingException exception) {
            // UTF-8 encoding not supported
        } catch (JWTCreationException exception) {
            // Invalid Signing configuration / Couldn‘t convert Claims.
        }
        return "";
    }

    public static String updateToken(String token) {
        return updateToken(token, 1);
    }

    public static boolean verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();
            verifier.verify(token);
            log.info("CHECK TOEKN: Fine");
            return true;
        } catch (UnsupportedEncodingException exception) {
            log.info("CHECK TOEKN-ERROR: " + exception);
            // UTF-8 encoding not supported
        } catch (JWTVerificationException exception) {
            log.info("CHECK TOEKN-ERROR: " + exception);
            // Invalid signature/claims
        } catch (Exception e) {
            log.info("CHECK TOEKN-ERROR: " + e);
        }
        return false;
    }

    public static boolean verifyTokenResult(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();
            verifier.verify(token);
            log.info("CHECK TOEKN: 通过");
            return true;
        } catch (Exception e) {
            log.info("CHECK TOEKN-ERROR: " + e);
            return false;
        }
    }

    /**
     * @return int剩余有效时间(分钟)
     */
    public static int getTokenRemainingTime(String token) {
        DecodedJWT jwt = JWT.decode(token);
        Date expiresDate = jwt.getExpiresAt();
        if (expiresDate == null) {
            return -1;
        }
        return dateDiff(now(), jwt.getExpiresAt(), 3);
    }

    public static Long getId(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("id").asLong();
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


    //---------------------以下为时间工具方法-----------------------
    private static Date now(int i) {
        Date date = new Date();// 取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, i);// 把日期往后增加一天.整数往后推,负数往前移动
        return calendar.getTime(); // 这个时间就是日期往后推一天的结果
    }

    private static Date now() {
        return now(0);
    }

    /**
     * @param fromDate Date起始时间
     * @param toDate   Date结束时间
     * @param type     返回单位：  1-天数；2-小时；3-分钟；其他-秒
     * @return int相差时间
     */
    private static int dateDiff(Date fromDate, Date toDate, int type) {
        long from = fromDate.getTime();
        long to = toDate.getTime();
        int a = 1000;
        switch (type) {
            case 1:
                a = a * 60 * 60 * 24;
                break;
            case 2:
                a = a * 60 * 60;
                break;
            case 3:
                a = a * 60;
                break;
            default:
                break;
        }
        int time = (int) ((to - from) / a);
        return time;
    }

    /**
     * @param fromDate Date起始时间
     * @param toDate   Date结束时间
     * @return int相差秒数
     */
    private static int dateDiff(Date fromDate, Date toDate) {
        return dateDiff(fromDate, toDate);
    }
}
