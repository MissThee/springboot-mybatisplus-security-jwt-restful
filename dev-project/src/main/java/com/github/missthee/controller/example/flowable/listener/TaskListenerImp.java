package com.github.missthee.controller.example.flowable.listener;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
//图中配置了此类的全路径，不要随意修改本类所属的包
//此类的对象为flowable运行时new 出来的对象，无法使用注解注入，仅能用getBean获取
public class TaskListenerImp implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        delegateTask.setAssignee("100");
    }

}
