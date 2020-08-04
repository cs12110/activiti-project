package com.pkgs.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import lombok.extern.slf4j.Slf4j;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-07-31 09:27
 */
@Slf4j
public class VacationExecutionListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution execution) {
        String eventName = execution.getEventName();



        log.info("Function[notify]execution event name:{}", eventName);
    }
}
