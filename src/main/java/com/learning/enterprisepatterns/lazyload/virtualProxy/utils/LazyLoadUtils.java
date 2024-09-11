package com.learning.enterprisepatterns.lazyload.virtualProxy.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.springframework.util.ReflectionUtils;

@UtilityClass
public class LazyLoadUtils {

    @SneakyThrows
    public static <T> T unproxy(T object) {
        val source = ReflectionUtils.findField(object.getClass(), "source");
        source.setAccessible(true);
        return (T) source.get(object);
    }
}
