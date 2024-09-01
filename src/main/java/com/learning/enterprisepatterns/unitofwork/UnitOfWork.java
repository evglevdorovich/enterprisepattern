package com.learning.enterprisepatterns.unitofwork;

import com.learning.enterprisepatterns.registry.Registry;
import com.learning.enterprisepatterns.unitofwork.model.DomainObject;
import com.learning.enterprisepatterns.unitofwork.registry.RepositoryRegistry;
import lombok.SneakyThrows;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

// Unit of Work (Object registration version) with Registry pattern.
// Unit of work controller and Hybrid version are not considered (As Hibernate works - EntityManager(controller)), it's different implementation
public class UnitOfWork implements AutoCloseable {
    private static final ThreadLocal<UnitOfWork> CURRENT_UNIT_OF_WORK = new ThreadLocal<>();

    public static UnitOfWork getCurrent() {
        return CURRENT_UNIT_OF_WORK.get();
    }

    private final List<DomainObject> newObjects;
    private final List<DomainObject> dirtyObjects;
    private final List<DomainObject> removedObjects;
    private final RepositoryRegistry registry;

    private UnitOfWork(RepositoryRegistry registry) {
        newObjects = new ArrayList<>();
        dirtyObjects = new ArrayList<>();
        removedObjects = new ArrayList<>();
        this.registry = registry;
    }

    public static UnitOfWork startCurrent(RepositoryRegistry registry) {
        Registry.start();
        var unitOfWork = new UnitOfWork(registry);
        CURRENT_UNIT_OF_WORK.set(unitOfWork);

        return unitOfWork;
    }


    public void registerNew(DomainObject obj) {
        if (obj.getId() != null) {
            Assert.isTrue(!dirtyObjects.contains(obj), "object is in dirty");
            Assert.isTrue(!removedObjects.contains(obj), "object is in removed");
            register(obj);
        }

        Assert.isTrue(!newObjects.contains(obj), "object is already in new");

        newObjects.add(obj);
    }

    public void registerDirty(DomainObject obj) {
        if (isNew(obj)) {
            return;
        }

        Assert.isTrue(!removedObjects.contains(obj), "object not removed");

        if (!newObjects.contains(obj) && !dirtyObjects.contains(obj)) {
            dirtyObjects.add(obj);
            register(obj);
        }
    }

    public void registerRemoved(DomainObject obj) {
        // cover case without id
        if (isNew(obj)) {
            newObjects.remove(obj);
            return;
        }

        // cover case with id
        if (newObjects.remove(obj)) {
            return;
        }

        if (!removedObjects.contains(obj)) {
            removedObjects.add(obj);
            unregister(obj);
        }
    }

    private static boolean isNew(DomainObject obj) {
        return obj.getId() == null;
    }

    public void registerClean(DomainObject obj) {
        Assert.notNull(obj.getId(), "id is null");
        register(obj);
    }

    public void commit() {
        insertNew();
        updateDirty();
        removeRemoved();
    }

    private static void register(DomainObject obj) {
        Registry.register(obj.getId(), obj, obj.getClass());
    }

    private static void unregister(DomainObject obj) {
        Registry.unregister(obj, obj.getClass());
    }

    @Override
    @SneakyThrows
    public void close() {
        CURRENT_UNIT_OF_WORK.remove();
        Registry.end();
    }

    // all of this could be optimized and insert in batches, for example collecting by class first.
    // suppose that saveAndFlush = simple insert
    private void insertNew() {
        newObjects.forEach(obj -> registry.getRepository(obj.getClass()).saveAndFlush(obj));
    }

    // suppose that saveAndFlush = simple update
    private void updateDirty() {
        //update instead of save
        dirtyObjects.forEach(obj -> registry.getRepository(obj.getClass()).saveAndFlush(obj));
    }

    // suppose that delete = delete query
    private void removeRemoved() {
        removedObjects.forEach(obj -> registry.getRepository(obj.getClass()).delete(obj));
    }
}
