package com.learning.enterprisepatterns.activerecord.registry;

import com.learning.enterprisepatterns.activerecord.model.Person;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Registry {
    private static final Map<Class<?>, Map<Object, Object>> REGISTRY = new ConcurrentHashMap<>();

    public static Person getPerson(Object key) {
        return (Person) REGISTRY.getOrDefault(Person.class, Map.of()).get(key);
    }

    public static void register(Object key, Object value, Class<?> clazz) {
        REGISTRY.getOrDefault(clazz, new HashMap<>()).put(key, value);
    }

    public static void evict() {
        REGISTRY.clear();
    }
}
