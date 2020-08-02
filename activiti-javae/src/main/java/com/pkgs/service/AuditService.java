/*
 *
 * This software is the confidential and proprietary information of sipai Company.
 *
 */
package com.pkgs.service;

import com.pkgs.model.request.AgreeRequest;
import com.pkgs.model.response.BaseResponse;
import com.pkgs.model.response.TaskInfoResponse;
import com.pkgs.util.BeanMapUtil;
import com.pkgs.util.ListUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Resource;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author huapeng.huang@medbanks.cn
 * @version V1.0
 * @since 2020-08-02 12:49
 */
@Slf4j
@Service
public class AuditService {

    @Resource
    private TaskService taskService;

    private Function<Task, TaskInfoResponse> fun = (t) -> {

        Map<String, Object> applyInfoMap = taskService.getVariables(t.getId());

        TaskInfoResponse response = new TaskInfoResponse();
        response.setId(t.getId());
        response.setApplyDate(t.getCreateTime());
        response.setApplyStudent(String.valueOf(applyInfoMap.get("applyStudent")));
        response.setReason(String.valueOf(applyInfoMap.get("reason")));
        String days = String.valueOf(applyInfoMap.get("days"));
        response.setDays(Integer.parseInt(days));

        return response;
    };

    public List<TaskInfoResponse> getTasks(String assignee) {
        if (StringUtils.isEmpty(assignee)) {
            return Collections.emptyList();
        }

        List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).orderByTaskCreateTime().desc().list();
        return ListUtil.parse(tasks, fun);
    }

    public BaseResponse agree(AgreeRequest request) {

        Map<String, Object> infoMap = BeanMapUtil.bean2Map(request);
        dealWithNextAuditor(infoMap);

        taskService.setVariablesLocal(request.getId(), infoMap);
        taskService.complete(request.getId(), infoMap);

        return BaseResponse.success("审批通过");
    }

    private void dealWithNextAuditor(Map<String, Object> infoMap) {
        List<String> monitors = new ArrayList<>();
        monitors.add("3306");
        monitors.add("3307");
        infoMap.put("gradeMonitorList", monitors);
    }

}
