package com.github.missthee.service.imp.manage;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.missthee.config.security.jwt.UserInfoForJWT;
import com.github.missthee.db.dto.manage.usercontroller.InsertOneReq;
import com.github.missthee.db.dto.manage.usercontroller.SelectListReq;
import com.github.missthee.db.dto.manage.usercontroller.UpdateOneReq;
import com.github.missthee.db.mapper.primary.manage.UserRoleMapper;
import com.github.missthee.db.po.primary.manage.UserRole;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.github.missthee.db.mapper.primary.manage.UserMapper;
import com.github.missthee.db.po.primary.manage.User;
import com.github.missthee.service.interf.manage.UserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;


@Service
@Transactional(rollbackFor = {Exception.class})
public class UserImp extends ServiceImpl<UserMapper, User> implements UserService, UserInfoForJWT {
    private final MapperFacade mapperFacade;
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;

    @Autowired
    public UserImp(UserMapper userMapper, MapperFacade mapperFacade, UserRoleMapper userRoleMapper) {
        this.userMapper = userMapper;
        this.mapperFacade = mapperFacade;
        this.userRoleMapper = userRoleMapper;
    }

    @Override
    public Long insertOne(InsertOneReq insertOneReq) {
        User user = mapperFacade.map(insertOneReq, User.class);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userMapper.insert(user);
        Long userId = user.getId();
        if (userId != null) {
            updateUserRole(insertOneReq.getRoleIdList(), user.getId());
        }
        return userId;
    }

    @Override
    public Boolean deleteOne(Long id) {
        if (id == null) {
            return false;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(User.ID, id)
                .eq(User.IS_DELETE, id);
        Boolean result = userMapper.delete(queryWrapper) > 0;
        if (result) {
            updateUserRole(null, id);
        }
        return result;
    }

    @Override
    public Boolean updateOne(UpdateOneReq updateOneReq) {
        //密码加密
        if (!StringUtils.isEmpty(updateOneReq.getPassword())) {
            updateOneReq.setPassword(new BCryptPasswordEncoder().encode(updateOneReq.getPassword()));
        }
        //拷贝用户信息，生成User对象
        User user = mapperFacade.map(updateOneReq, User.class);
        //更新信息
        Boolean result = userMapper.updateById(user) > 0;
        if (result) {
            updateUserRole(updateOneReq.getRoleIdList(), user.getId());
        }
        return result;
    }

    @Override
    public User selectOne(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public List<User> selectList(SelectListReq selectListReq) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(User.IS_DELETE, selectListReq.getIsDelete());
        for (Map.Entry<String, Boolean> entry : selectListReq.getOrderBy().entrySet()) {
            queryWrapper.orderBy(true, entry.getValue(), entry.getKey());
        }
        return userMapper.selectList(queryWrapper);
    }

    @Override
    public Boolean deleteOnePhysical(Long id) {
        return userMapper.deleteById(id) > 0;
    }

    @Override
    public Boolean isDuplicate(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(User.USERNAME, username);
        return userMapper.selectList(queryWrapper).size() > 0;
    }

    @Override
    public String getSecret(Object userId) {
        String userIdStr = String.valueOf(userId);
        if ("null".equals(userId) || "".equals(userIdStr)) {
            throw new BadCredentialsException("empty userId, when get secret");
        }
        User user = selectOne(Long.valueOf(String.valueOf(userId)));
        if (user == null) {
            throw new BadCredentialsException("can't find user, when get secret");
        }
        return user.getPassword();

    }

    private void updateUserRole(List<Long> roleIdList, Long userId) {
        //清理关系
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(UserRole.USER_ID, userId);
        userRoleMapper.delete(queryWrapper);
        //插入关系
        if (roleIdList != null && roleIdList.size() > 0) {
            for (Long roleId : roleIdList) {
                userRoleMapper.insert(new UserRole().setUserId(userId).setRoleId(roleId));
            }
        }
    }
}
