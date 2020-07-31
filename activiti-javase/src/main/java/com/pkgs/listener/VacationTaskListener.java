/*
 *
 * This software is the confidential and proprietary information of sipai Company.
 *
 */
package com.pkgs.listener;

import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import lombok.extern.slf4j.Slf4j;

/**
 * @author huapeng.huang@medbanks.cn
 * @version V1.0
 * @since 2020-07-31 09:27
 */
@Slf4j
public class VacationTaskListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        String assignee = delegateTask.getAssignee();

        DelegateExecution execution = delegateTask.getExecution();
        String studentNo = execution.getProcessInstanceBusinessKey();

        // 当前审批信息
        Map<String, Object> variables = delegateTask.getVariables();
        Object comment = delegateTask.getVariable("comment");
        Object audit = delegateTask.getVariable("audit");

        // 审批节点完成,通知学生最新进度,但是怎么获取申请的学生???

        log.info("Function[notify] event:{},assignee:{},comment:{},audit:{}", eventName, assignee, comment, audit);
    }
}
