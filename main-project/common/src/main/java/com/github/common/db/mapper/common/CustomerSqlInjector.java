package com.github.common.db.mapper.common;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;

import java.util.List;

/**
 * 添加Sql注入方法,支持空字段更新
 */
public class CustomerSqlInjector extends DefaultSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList(){
        List<AbstractMethod> methodList=super.getMethodList();
        methodList.add(new BatchInsertByList());

        return methodList;
    }
}