package com.github.base.service.imp.manage;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.base.db.mapper.primary.manage.SysSignatureMapper;
import com.github.base.service.interf.manage.SignatureService;
import com.github.common.db.entity.primary.SysSignature;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author WORK-PC,MT
 * @since 2019-07-10
 */
@Service
public class SignatureImp extends ServiceImpl<SysSignatureMapper, SysSignature> implements SignatureService {

}
