package com.github.base.service.interf.manage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.common.db.entity.primary.User;
import org.springframework.stereotype.Service;

public interface MyAccountService extends IService<User> {
    Boolean comparePassword(Long id, String oldPassword);

    Boolean updatePassword(Long id, String oldPassword);
}
