/*
 *
 * This software is the confidential and proprietary information of sipai Company.
 *
 */
package com.pkgs.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author huapeng.huang@medbanks.cn
 * @version V1.0
 * @since 2020-08-02 13:11
 */
@AllArgsConstructor
@Getter
public enum LevelEnum {
    /**
     * 班主任
     */
    CLASS_MONITOR(1, "班主任"),
    /**
     * 级主任
     */
    GRADE_MONITOR(2, "级主任");

    private final Integer value;
    private final String label;
}
