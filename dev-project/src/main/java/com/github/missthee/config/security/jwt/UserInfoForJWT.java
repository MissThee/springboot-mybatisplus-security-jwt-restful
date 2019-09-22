package com.github.missthee.config.security.jwt;

public interface UserInfoForJWT {
    /**
     * @param userId
     * @return
     * @apiNote A function of getting user's password. JWT need the value used as secret.
     */
    String getSecret(Object userId);



}
