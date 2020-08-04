package com.pkgs.service;

import com.alibaba.fastjson.JSON;
import com.pkgs.model.request.MessageRequest;
import com.pkgs.model.response.BaseResponse;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-08-02 21:58
 */
@Slf4j
@Service
public class MessageService {

    public BaseResponse send(MessageRequest request) {
        log.info("Function[send] message:{}", JSON.toJSONString(request, true));

        return BaseResponse.success("发送成功");
    }
}
