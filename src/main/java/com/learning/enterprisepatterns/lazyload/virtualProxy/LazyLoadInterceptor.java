package com.learning.enterprisepatterns.lazyload.virtualProxy;

import com.learning.enterprisepatterns.lazyload.model.Producer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

@RequiredArgsConstructor
public class LazyLoadInterceptor {
    private final Finder<?> finder;

    @RuntimeType
    @SneakyThrows
    public Object intercept(@Origin Method method, @AllArguments @RuntimeType Object[] args, @This Object instance) {
        var sourceField = instance.getClass().getDeclaredField("source");
        sourceField.setAccessible(true);

        var id = instance.getClass().getDeclaredField("id");
        id.setAccessible(true);

        if (sourceField.get(instance) == null) {
            sourceField.set(instance, finder.findById((Integer) id.get(instance)));
        }

        var methodName = method.getName();

        return ReflectionUtils.findMethod(Producer.class, methodName).invoke(sourceField.get(instance), args);
    }
}
