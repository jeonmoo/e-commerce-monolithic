package com.test.bootcamp.common;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Util {

    public static <T, K> Map<K, T> toMap(List<T> list, Function<T, K> keyMapper) {
        return list.stream()
                .collect(Collectors.toMap(keyMapper, Function.identity()));
    }
}
