/*
 *
 * This software is the confidential and proprietary information of sipai Company.
 *
 */
package com.pkgs.model.request;

import com.alibaba.fastjson.JSON;

import lombok.Data;

/**
 * @author huapeng.huang@medbanks.cn
 * @version V1.0
 * @since 2020-08-02 21:58
 */
@Data
public class MessageRequest {
    private String taskName;
    private String assignee;
    private String studentNo;
    private Boolean agree;
    private String comment;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
