package com.github.base.service.interf.manage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.base.dto.manage.user.SysUserInTableDTO;
import com.github.base.dto.manage.user.SysUserInTableDetailDTO;
import com.github.base.vo.manage.SysUserVO;
import com.github.common.db.entity.primary.SysUser;
import com.github.common.tool.SimplePageInfo;

import javax.management.InvalidAttributeValueException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;

public interface SysUserService extends IService<SysUser> {
    Long insertOne(SysUserVO.InsertOneReq userInsertOneDTO);

    Boolean deleteOne(Long id);

    Boolean updateOne(SysUserVO.UpdateOneReq updateOneReq);

    SysUserInTableDetailDTO selectOne(Long id);

    SimplePageInfo<SysUserInTableDTO> selectList(Integer pageNum, Integer pageSize, Boolean isDelete, LinkedHashMap<String, Boolean> orderBy) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InvalidAttributeValueException;

    Boolean deleteOnePhysical(Long id);

    Boolean resetDefaultPassword(Long id);
}
