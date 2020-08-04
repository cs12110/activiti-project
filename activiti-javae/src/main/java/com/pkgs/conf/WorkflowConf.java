package com.pkgs.conf;

import com.alibaba.fastjson.JSON;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-08-02 22:17
 */
@Data
@Component
public class WorkflowConf {

    @Value("${workflow.vacation.process-definition-key}")
    private String vacationKey;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
