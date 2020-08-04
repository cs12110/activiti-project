package com.pkgs.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huapeng.huang
 * @version V1.0
 * @since 2020-08-02 13:30
 */
public class LocalCacheUtil {

    private static Map<String, String> map = new ConcurrentHashMap<>();

    public static void put(String key, String value) {
        map.put(key, value);
    }

    public static String get(String key) {
        return map.get(key);
    }
}
