/*
 *
 * This software is the confidential and proprietary information of sipai Company.
 *
 */
package com.pkgs.listener;

import com.pkgs.model.request.MessageRequest;
import com.pkgs.service.MessageService;

import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author huapeng.huang@medbanks.cn
 * @version V1.0
 * @since 2020-07-31 09:27
 */
@Component
@Slf4j
public class VacationTaskListener implements TaskListener {

    @Resource
    private MessageService messageService;

    @Override
    public void notify(DelegateTask delegateTask) {
        // String eventName = delegateTask.getEventName();
        String assignee = delegateTask.getAssignee();
        String taskName = delegateTask.getName();

        // 获取请假的学生编码
        DelegateExecution execution = delegateTask.getExecution();
        String studentNo = execution.getProcessInstanceBusinessKey();

        // 当前审批信息
        Map<String, Object> variables = delegateTask.getVariables();
        Object audit = variables.get("audit");
        Object comment = variables.get("comment");

        MessageRequest eventMessage = new MessageRequest();
        eventMessage.setAgree(Boolean.TRUE.equals(audit));
        eventMessage.setComment(String.valueOf(comment));
        eventMessage.setStudentNo(studentNo);
        eventMessage.setAssignee(assignee);
        eventMessage.setTaskName(taskName);

        // 通知学生最新进度
        messageService.send(eventMessage);
    }

}
