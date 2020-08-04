package com.pkgs.service;

import com.alibaba.fastjson.JSON;
import com.pkgs.model.response.BaseResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-08-02 12:49
 */
@Slf4j
@Service
public class ApplyHistoryService {

    @Resource
    private HistoryService historyService;

    public BaseResponse trace(String processInstanceId) {

        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();
        List<HistoricTaskInstance> historicTaskInstances = historicTaskInstanceQuery
            // 包括流程参数
            .includeTaskLocalVariables().includeProcessVariables()
            // 构建查询条件
            .processInstanceId(processInstanceId)
            // 根据时间排序
            .orderByHistoricTaskInstanceStartTime().desc()
            // 获取数据
            .list();

        List<Object> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (HistoricTaskInstance h : historicTaskInstances) {
            String assignee = h.getAssignee();
            String taskName = h.getName();
            Date startTime = h.getStartTime();
            Date endTime = h.getEndTime();

            // 怎么获取请假原因那些???
            Map<String, Object> info = new HashMap<>(16);
            info.put("assignee", assignee);
            info.put("taskName", taskName);
            info.put("startTime", sdf.format(startTime));
            if (Objects.nonNull(endTime)) {
                info.put("endTime", sdf.format(endTime));
            }
            info.put("localVars", h.getTaskLocalVariables());
            //info.put("processVars", h.getProcessVariables());

            log.info("Function[history] info:{}", JSON.toJSONString(info, true));

            list.add(info);
        }

        return BaseResponse.success(list);
    }

}
