/*
 *
 * This software is the confidential and proprietary information of sipai Company.
 *
 */
package com.pkgs;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 *
 * @author huapeng.huang@medbanks.cn
 * @version V1.0
 * @since 2020-08-02 11:55
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
