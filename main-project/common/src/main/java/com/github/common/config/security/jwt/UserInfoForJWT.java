package com.github.common.config.security.jwt;

public interface UserInfoForJWT {
    /**
     * @param userId
     * @return
     * @apiNote A function of getting user's password. JWT need the value used as secret.
     */
    String getSecret(Object userId);
    //本项目构建token时需使用用户的密码（加密后的）。验证时也会需要用户的密码（加密后的），所以当用户修改密码后，现有token会立即生效，需要重新登录。
    //因每个系统获取用户密码的方式不同，此方法作为一个接口可供自行实现。若找不到任何实现类，运行会报错
}
