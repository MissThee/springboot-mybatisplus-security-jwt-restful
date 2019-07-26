package com.github.base.service.imp.transaction;

import com.github.common.db.entity.primary.TableInDb1;
import com.github.base.db.mapper.primary.transaction.TableInDb1Mapper;
import com.github.base.db.mapper.secondary.transaction.TableInDb2Mapper;
import com.github.base.service.interf.transaction.OperateAllService;
import com.github.common.db.entity.secondary.TableInDb2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OperateAllImp implements OperateAllService {
    private final TableInDb1Mapper tableInDb1Mapper;
    private final TableInDb2Mapper tableInDb2Mapper;

    public OperateAllImp(TableInDb1Mapper tableInDb1Mapper, TableInDb2Mapper tableInDb2Mapper) {
        this.tableInDb1Mapper = tableInDb1Mapper;
        this.tableInDb2Mapper = tableInDb2Mapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertAll(boolean isOK) throws Exception {
        tableInDb1Mapper.insert(new TableInDb1().setColumn1(1).setColumn2(1));
        tableInDb2Mapper.insert(new TableInDb2().setColumn1(2).setColumn2(2));
        if (!isOK) {
            throw new Exception("抛出异常，回滚");
        }
    }
}
