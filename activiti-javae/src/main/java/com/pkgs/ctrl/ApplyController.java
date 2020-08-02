/*
 *
 * This software is the confidential and proprietary information of sipai Company.
 *
 */
package com.pkgs.ctrl;

import com.pkgs.model.request.ApplyRequest;
import com.pkgs.model.response.BaseResponse;
import com.pkgs.service.ApplyService;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
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
@RequestMapping("/apply")
public class ApplyController {
    @Resource
    private ApplyService applyService;

    @PostMapping("/start")
    @ResponseBody
    public BaseResponse apply(@RequestBody ApplyRequest request) {
        log.info("Function[apply] request:{}", request);
        BaseResponse response = applyService.apply(request);
        log.info("Function[apply] request:{},response:{}", request, response);

        return response;
    }
}
