package com.pkgs.util;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-08-02 12:30
 */
@Slf4j
public class BeanMapUtil {

    public static Map<String, Object> bean2Map(Object value) {
        if (Objects.isNull(value)) {
            return Collections.emptyMap();
        }

        Class<?> clazz = value.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Object> map = new HashMap<>();
        try {
            for (Field f : fields) {
                f.setAccessible(true);
                map.put(f.getName(), f.get(value));
            }
        } catch (Exception e) {
            log.error("Function[bean2Map]value:{}" + JSON.toJSONString(value), e);
        }

        return map;
    }
}
