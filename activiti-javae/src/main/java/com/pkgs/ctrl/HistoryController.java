/*
 *
 * This software is the confidential and proprietary information of sipai Company.
 *
 */
package com.pkgs.ctrl;

import com.pkgs.model.response.BaseResponse;
import com.pkgs.service.ApplyHistoryService;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/history")
public class HistoryController {

    @Resource
    private ApplyHistoryService applyHistoryService;

    @GetMapping("/trace")
    @ResponseBody
    public BaseResponse trace(String processId) {
        return applyHistoryService.trace(processId);
    }

}
