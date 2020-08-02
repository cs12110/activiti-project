/*
 *
 * This software is the confidential and proprietary information of sipai Company.
 *
 */
package com.pkgs.ctrl;

import com.pkgs.model.request.AgreeRequest;
import com.pkgs.model.response.BaseResponse;
import com.pkgs.model.response.TaskInfoResponse;
import com.pkgs.service.AuditService;
import com.pkgs.util.ListUtil;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

/**
 * @author huapeng.huang@medbanks.cn
 * @version V1.0
 * @since 2020-08-02 12:04
 */
@Slf4j
@Controller
@RequestMapping("/audit")
public class AuditController {

    @Resource
    private AuditService auditService;

    @GetMapping("/tasks")
    @ResponseBody
    public BaseResponse tasks(String user) {
        log.info("Function[tasks] user:{}", user);
        List<TaskInfoResponse> tasks = auditService.getTasks(user);

        log.info("Function[tasks] user:{},response:{}", user, ListUtil.size(tasks));
        return BaseResponse.success(tasks);
    }

    @PostMapping("/agree")
    @ResponseBody
    public BaseResponse agree(@RequestBody AgreeRequest request) {
        log.info("Function[agree] request:{}", request);
        BaseResponse response = auditService.agree(request);
        log.info("Function[agree] request:{},response:{}", request, response);
        return response;
    }
}
