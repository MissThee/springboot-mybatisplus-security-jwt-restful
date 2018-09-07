package server.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import server.db.primary.model.sysoption.AreaInfo;
import server.db.primary.model.sysoption.CLogin;
//import server.db.primary.model.sysoption.SysPermission;
//import server.db.primary.model.sysoption.SysRole;
//import server.db.primary.model.sysoption.SysUser;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Slf4j
public class JavaJWT {
    private static final String issuer = "issuer";       //发布者
    private static final String secret = "secret20180718";       //发布者

    /**
     * @param cLogin            用户
     * @param expiresDayFromNow 有效时间（天）
     */
    public static String createToken(CLogin cLogin, int expiresDayFromNow) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTCreator.Builder builder = JWT.create();
            //添加发布人信息【可直接解析】
            builder.withIssuer(issuer);
            builder.withExpiresAt(now(expiresDayFromNow));
            //添加附加信息【可直接解析】
            builder.withClaim("id", cLogin.getId());
            builder.withClaim("ipLimitMark", cLogin.getIpLimitMark());
            try {
                builder.withClaim("groupId", cLogin.getAuthGroup().getId());
            } catch (Exception e) {
            }

            List<Long> areaIdList = new ArrayList<>();
            try {
                builder.withClaim("objId", cLogin.getAuthGroup().getAuthObj().getId());
                List<AreaInfo> areaInfoList = cLogin.getAuthGroup().getAuthObj().getAreaInfoList();
                for (AreaInfo areaInfo : areaInfoList) {
                    areaIdList.add(areaInfo.getId());
                }
            } catch (Exception e) {
            }
//            areaIdList.add(-1L);
            builder.withClaim("areaIds", Joiner.on(',').join(areaIdList));
            try {
                builder.withClaim("roleId", cLogin.getAuthGroup().getAuthRole().getId());
            } catch (Exception e) {
            }

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

    /**
     * @param cLogin 用户 有效时间固定
     */
//    public static String createToken(SysUser user) {
//        return createToken(user, 1);
//    }
    public static String createToken(CLogin cLogin) {
        return createToken(cLogin, 1);
    }

    public static String updateToken(String token, int expiresDayFromNow) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTCreator.Builder builder = JWT.create();
            builder.withIssuer(issuer);
            builder.withExpiresAt(now(expiresDayFromNow));
            DecodedJWT decodedJWT = JWT.decode(token);

            for (String claimKey : decodedJWT.getClaims().keySet()) {
                if (claimKey.equals("iss") || claimKey.equals("exp")) {

                } else if (claimKey.equals("areaIds")) {
                    builder.withClaim(claimKey, decodedJWT.getClaim(claimKey).asString());
                } else {
                    builder.withClaim(claimKey, decodedJWT.getClaim(claimKey).asInt());
                }
            }

//            builder.withClaim("id", decodedJWT.getClaim("id").asInt());
//            builder.withArrayClaim("roleList", decodedJWT.getClaim("roleList").asArray(String.class));
//            builder.withArrayClaim("permissionList", decodedJWT.getClaim("permissionList").asArray(String.class));
//            builder.withClaim("groupId", decodedJWT.getClaim("groupId").asInt());
//            builder.withClaim("objId", decodedJWT.getClaim("objId").asInt());
//              int c= decodedJWT.getClaim("roleId").asInt();
//              builder.withClaim("roleId", decodedJWT.getClaim("roleId").asInt());
//            builder.withClaim("unitId", decodedJWT.getClaim("unitId").asInt());
//            builder.withClaim("unitType", decodedJWT.getClaim("unitType").asString());

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

    public static Map<String, Object> verifyTokenResult(String token) {
        Map<String, Object> map = new HashMap<>();
        map.put("result", false);
        map.put("msg", "");
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();
            verifier.verify(token);
            log.info("CHECK TOEKN: Fine");
            map.put("result", true);
        } catch (UnsupportedEncodingException e) {
            log.info("CHECK TOEKN-ERROR: " + e);
            map.put("result", false);
            map.put("msg", "token格式有误。" + e);
        } catch (JWTVerificationException e) {
            map.put("result", false);
            map.put("msg", "token已过期。" + e);
        } catch (Exception e) {
            log.info("CHECK TOEKN-ERROR: " + e);
            map.put("result", false);
            map.put("msg", "token校验未通过。" + e);
        }
        return map;
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

//    public static List<String> getRoleList(String token) {
//        DecodedJWT jwt = JWT.decode(token);
//        return jwt.getClaim("roleList").asList(String.class);
//    }
//
//    public static List<String> getPermissionList(String token) {
//        DecodedJWT jwt = JWT.decode(token);
//        return jwt.getClaim("permissionList").asList(String.class);
//    }

    public static Long getId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("id").asLong();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Long getGroupId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("groupId").asLong();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Long getObjId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("objId").asLong();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Long getIpLimitMark(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("ipLimitMark").asLong();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Long> getAreaIds(String token) {
        List<Long> areaIds = new ArrayList<>();
        try {
            DecodedJWT jwt = JWT.decode(token);
            String areaIdstr = jwt.getClaim("areaIds").asString();
            if (areaIdstr != null) {
                for (String areaId : Arrays.asList(areaIdstr.split(","))) {
                    areaIds.add(Long.parseLong(areaId));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        areaIds.add(-1L);
        return areaIds;
    }

    public static Long getRoleId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("roleId").asLong();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

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
