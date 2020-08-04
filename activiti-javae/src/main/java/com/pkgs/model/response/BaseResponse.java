package com.pkgs.model.response;

import com.alibaba.fastjson.JSON;

import lombok.Data;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-08-02 12:06
 */
@Data
public class BaseResponse {

    private Integer status;
    private String message;
    private Object value;

    public static BaseResponse success(Object value) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(1);
        baseResponse.setValue(value);

        return baseResponse;
    }

    public static BaseResponse failure(String message) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(0);
        baseResponse.setMessage(message);

        return baseResponse;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
