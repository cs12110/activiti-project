package com.pkgs.listener;

import com.alibaba.fastjson.JSON;

import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-07-31 09:27
 */
@Slf4j
public class VacationTaskListener implements TaskListener {

    @Data
    public static class EventMessage {
        private String taskName;
        private String assignee;
        private String studentNo;
        private Boolean agree;
        private String comment;

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        String assignee = delegateTask.getAssignee();
        String taskName = delegateTask.getName();

        // 获取请假的学生编码
        DelegateExecution execution = delegateTask.getExecution();
        String studentNo = execution.getProcessInstanceBusinessKey();

        // 当前审批信息
        Map<String, Object> variables = delegateTask.getVariables();
        Object audit = variables.get("audit");
        Object comment = variables.get("comment");

        EventMessage eventMessage = new EventMessage();
        eventMessage.setAgree(Boolean.TRUE.equals(audit));
        eventMessage.setComment(String.valueOf(comment));
        eventMessage.setStudentNo(studentNo);
        eventMessage.setAssignee(assignee);
        eventMessage.setTaskName(taskName);

        // 通知学生最新进度
        sendNotifyMessage(eventMessage);
    }

    public static void sendNotifyMessage(EventMessage eventMessage) {
        log.info("Function[sendNotifyMessage] message:{}", JSON.toJSONString(eventMessage, true));
    }
}
