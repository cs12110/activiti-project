package com.pkgs.model.response;

import com.alibaba.fastjson.JSON;

import java.util.Date;

import lombok.Data;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-08-02 12:52
 */
@Data
public class TaskInfoResponse {

    private String id;
    private String applyStudent;
    private Integer days;
    private String reason;
    private Date applyDate;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
