/*
 *
 * This software is the confidential and proprietary information of sipai Company.
 *
 */
package com.pkgs.util;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author huapeng.huang@medbanks.cn
 * @version V1.0
 * @since 2020-08-02 12:48
 */
public class ListUtil {

    public static boolean isEmpty(List<?> list) {
        return null == list || list.isEmpty();
    }

    public static int size(List<?> list) {
        return isEmpty(list) ? 0 : list.size();
    }

    public static <T, E> List<E> parse(List<T> list, Function<T, E> function) {
        if (isEmpty(list) || Objects.isNull(function)) {
            return Collections.emptyList();
        }

        return list.stream().map(function).collect(Collectors.toList());
    }
}
