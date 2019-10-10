package com.github.common.config.security.jwt;

public interface UserInfoForJWT {
    /**
     * @param userId
     * @return
     * @apiNote A function of getting user's password. JWT need the value used as secret.
     */
    String getSecret(Object userId);
    //本项目构建token时需使用用户的密码。
    //因每个系统获取用户密码的方式不同，此方法作为一个接口可供开发人员自行实现。若找不到任何实现类，运行会报错


}
