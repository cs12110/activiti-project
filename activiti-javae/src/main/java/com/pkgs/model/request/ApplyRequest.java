package com.pkgs.model.request;

import com.alibaba.fastjson.JSON;

import lombok.Data;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-08-02 12:05
 */
@Data
public class ApplyRequest {

    private String applyStudent;
    private Integer days;
    private String reason;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
