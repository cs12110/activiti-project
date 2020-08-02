/*
 *
 * This software is the confidential and proprietary information of sipai Company.
 *
 */
package com.pkgs.service;

import com.alibaba.fastjson.JSON;
import com.pkgs.conf.WorkflowConf;
import com.pkgs.model.request.ApplyRequest;
import com.pkgs.model.response.BaseResponse;
import com.pkgs.util.BeanMapUtil;
import com.pkgs.util.LocalCacheUtil;

import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author huapeng.huang@medbanks.cn
 * @version V1.0
 * @since 2020-08-02 12:28
 */
@Service
@Slf4j
public class ApplyService {
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    private WorkflowConf workflowConf;

    public BaseResponse apply(ApplyRequest request) {

        Map<String, Object> applyInfoMap = BeanMapUtil.bean2Map(request);
        dealWithMonitorInfo(applyInfoMap);

        // 启动流程,指定申请学生
        ProcessInstance instance = runtimeService
            .startProcessInstanceByKey(workflowConf.getVacationKey(), request.getApplyStudent(), applyInfoMap);

        // 完成第一步申请,这个processId应该存放进业务表,方便查询使用
        String processId = instance.getId();
        Task task = taskService.createTaskQuery().processInstanceId(processId).singleResult();

        // 设置local变量,为历史打印输出
        taskService.setVariablesLocal(task.getId(), applyInfoMap);
        taskService.complete(task.getId(), applyInfoMap);

        log.info("Function[studentApply] processId:{},taskId:{},value:{}", processId, task.getId(),
            JSON.toJSONString(applyInfoMap));

        saveStudentApplyProcess(request.getApplyStudent(), processId);

        return BaseResponse.success("已提交请求申请,申请Id:" + processId);
    }

    private void dealWithMonitorInfo(Map<String, Object> applyInfoMap) {
        applyInfoMap.put("classMonitor", "haiyan");
    }

    private void saveStudentApplyProcess(String studentNo, String processId) {
        LocalCacheUtil.put("PROCESS:" + studentNo, processId);
    }
}
