package com.pkgs.model.request;

import com.alibaba.fastjson.JSON;

import lombok.Data;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-08-02 12:46
 */
@Data
public class AgreeRequest {

    private String id;
    private Boolean agree;
    private String comment;
    private Integer level;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
