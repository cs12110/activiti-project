/*
 *
 * This software is the confidential and proprietary information of sipai Company.
 *
 */
package com.pkgs;

import com.alibaba.fastjson.JSON;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import lombok.extern.slf4j.Slf4j;

/**
 * @author huapeng.huang@medbanks.cn
 * @version V1.0
 * @since 2020-07-31 16:42
 */
@Slf4j
public class VacationApp {

    public static String classMonitor = "haiyan";
    public static String gradeMonitor = "mr3306,mr3307";

    /**
     * 现实场景里面,如果没改动过流程,只需要发布一次,而不用每次调用都重新发布一次
     */
    public static boolean isAlreadyDeploy = true;

    /**
     * 并不是每次都要重新发布加载的
     *
     * @param args args
     */
    public static void main(String[] args) {
        try {
            // 初始化activit数据库
            ProcessEngine processEngine = loadProcessEngine();

            // 学生请假
//            studentApply(processEngine);
//
//            // 班主任审批
//            classAudit(processEngine);

            // 级主任审批
            //gradeAudit(processEngine);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ProcessEngine loadProcessEngine() {

        // 配置引擎数据库连接参数
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
            .setJdbcUrl("jdbc:mysql://118.89.113.147:3306/act_db?useUnicode=true&characterEncoding=UTF-8&useSSL=false")
            .setJdbcUsername("root").setJdbcPassword("Team@3306").setJdbcDriver("com.mysql.jdbc.Driver")
            .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        //.setHistoryLevel(HistoryLevel.FULL);
        ProcessEngine processEngine = cfg.buildProcessEngine();
        String pName = processEngine.getName();
        String ver = ProcessEngine.VERSION;

        log.info("Function[loadProcessEngine] start engine, name:{}, version:{}", pName, ver);

        // 发布流程
        deployIfNecessary(processEngine);

        return processEngine;
    }

    public static void deployIfNecessary(ProcessEngine processEngine) {
        if (isAlreadyDeploy) {
            return;
        }

        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
            .addClasspathResource("workflow/vacation.bpmn20.xml").deploy();
        String deployId = deployment.getId();

        log.info("Function[deployIfNecessary] deploy process,id:{}", deployId);

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployId)
            .singleResult();

        log.info("Function[deployIfNecessary] found id:{},name:{}", processDefinition.getId(),
            processDefinition.getName());

    }

    /**
     * 学生申请怎么动态指定班主任?
     *
     * @param processEngine
     */
    public static void studentApply(ProcessEngine processEngine) {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        TaskService taskService = processEngine.getTaskService();

        int[] days = { 1, 4 };
        for (int index = 0; index < days.length; index++) {

            String studentNo = "cs121" + index;

            Map<String, Object> map = new HashMap<>(8);
            // 对应申请节点的参数
            map.put("applyStudent", studentNo);
            map.put("days", days[index]);
            map.put("reason", "卧槽,批假呀");
            map.put("classMonitor", classMonitor);

            // 启动流程,指定申请学生
            ProcessInstance instance = runtimeService.startProcessInstanceByKey("vacation", studentNo, map);

            // 完成第一步申请,这个processId应该存放进业务表,方便查询使用
            String processId = instance.getId();
            Task task = taskService.createTaskQuery().processInstanceId(processId).singleResult();

            // 设置local变量,为历史打印输出
            taskService.setVariablesLocal(task.getId(), map);
            taskService.complete(task.getId(), map);

            log.info("Function[studentApply] processId:{},taskId:{},value:{}", processId, task.getId(),
                JSON.toJSONString(map));
        }
    }

    /**
     * 班主任怎么获取自己需要审批的请假?
     *
     * @param processEngine
     */
    public static void classAudit(ProcessEngine processEngine) {
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(classMonitor).orderByTaskCreateTime().desc()
            .list();

        for (Task task : tasks) {
            // 获取上一个任务传递过来的参数
            Map<String, Object> applyInfo = taskService.getVariables(task.getId());
            //            Object reason = taskService.getVariable(task.getId(), "reason");
            //            Object days = taskService.getVariable(task.getId(), "days");
            //            Object applyStudent = taskService.getVariable(task.getId(), "applyStudent");
            //
            //            Map<String, Object> applyInfo = new HashMap<>();
            //            applyInfo.put("student", applyStudent);
            //            applyInfo.put("reason", reason);
            //            applyInfo.put("days", days);

            Map<String, Object> map = new HashMap<>();
            map.put("audit", true);
            map.put("comment", "班主任同意");
            map.put("classMonitor", classMonitor);
            //ap.put("gradeMonitor", "3306");
            List<String> list = Arrays.asList(gradeMonitor.split(","));
            map.put("gradeMonitorList", list);
            map.put("applyInfo", applyInfo);

            // 同意学生请假
            taskService.setVariablesLocal(task.getId(), map);
            taskService.complete(task.getId(), map);

            log.info("Function[classAudit] class monitor pass:{}", JSON.toJSONString(applyInfo, true));
        }

    }

    /**
     * 班主任怎么获取自己需要审批的请假?
     *
     * @param processEngine
     */
    public static void gradeAudit(ProcessEngine processEngine) {
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskAssignee("mr3306").orderByTaskCreateTime().desc().list();

        for (Task task : tasks) {
            // 获取上一个任务传递过来的参数
            Map<String, Object> variables = taskService.getVariables(task.getId());

            Map<String, Object> map = new HashMap<>();
            map.put("audit", true);
            map.put("comment", "级主任同意");

            // 同意学生请假
            String processInstanceId = task.getProcessInstanceId();
            taskService.setVariablesLocal(task.getId(), map);
            taskService.complete(task.getId(), map);

            log.info("Function[gradeAudit] grade monitor audit:{}", JSON.toJSONString(variables, true));

            traceTaskHistory(processEngine, processInstanceId);
        }
    }

    /**
     * 查询该任务的经过历史流程
     *
     * @param processEngine     processEngine
     * @param processInstanceId processInstanceId
     */
    private static void traceTaskHistory(ProcessEngine processEngine, String processInstanceId) {
        HistoryService historyService = processEngine.getHistoryService();
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();
        List<HistoricTaskInstance> historicTaskInstances = historicTaskInstanceQuery
            // 包括流程参数
            .includeTaskLocalVariables().includeProcessVariables()
            // 构建查询条件
            .processInstanceId(processInstanceId)
            // 根据时间排序
            .orderByHistoricTaskInstanceStartTime().asc()
            // 获取数据
            .list();

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
            info.put("endTime", sdf.format(endTime));
            info.put("localVars", h.getTaskLocalVariables());
            info.put("processVars", h.getProcessVariables());

            log.info("Function[history] info:{}", JSON.toJSONString(info, true));
        }
    }
}
