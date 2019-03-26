package com.github.missthee.controller.example.flowable.listener;

import com.github.missthee.db.primary.model.basic.User;
import com.github.missthee.service.interf.basic.UserService;
import com.github.missthee.tool.ApplicationContextHolder;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
//图中配置了此类的全路径，不要随意修改本类所属的包
public class TaskListenerImp implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        User user = userService().selectOneById(19);
        System.out.println(user);
        delegateTask.setAssignee("100");
    }

    private UserService userService() {
        return ApplicationContextHolder.getBean(UserService.class);
    }
}
