package com.learning.enterprisepatterns.registry;

import com.learning.enterprisepatterns.activerecord.model.Person;
import com.learning.enterprisepatterns.unitofwork.model.Album;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class Registry {

    private static final ThreadLocal<Map<Class<?>, Map<Object, Object>>> MAPPED_OBJECT_BY_ID_BY_CLASS = new ThreadLocal<>();

    public static void start() {
        MAPPED_OBJECT_BY_ID_BY_CLASS.set(new HashMap<>());
    }

    public static Person getPerson(Object key) {
        return (Person) MAPPED_OBJECT_BY_ID_BY_CLASS.get().getOrDefault(Person.class, Map.of()).get(key);
    }

    //Used in Repositories to get the object from registry (To work with UnitOfWork + Registry pattern)
    public static Album getAlbum(Object key) {
        return (Album) MAPPED_OBJECT_BY_ID_BY_CLASS.get().getOrDefault(Album.class, Map.of()).get(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Object key, Class<?> clazz) {
        return (T) MAPPED_OBJECT_BY_ID_BY_CLASS.get().getOrDefault(clazz, Map.of()).get(key);
    }

    public static void register(Object key, Object value, Class<?> clazz) {
        MAPPED_OBJECT_BY_ID_BY_CLASS.get().getOrDefault(clazz, new HashMap<>()).put(key, value);
    }

    public static void unregister(Object key, Class<?> clazz) {
        MAPPED_OBJECT_BY_ID_BY_CLASS.get().getOrDefault(clazz, new HashMap<>()).remove(key);
    }

    public static void end() {
        MAPPED_OBJECT_BY_ID_BY_CLASS.remove();
    }

    //start() and end() could be with some annotation like @Transactional or something like this
}
