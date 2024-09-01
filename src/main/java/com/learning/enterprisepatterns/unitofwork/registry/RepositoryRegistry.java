package com.learning.enterprisepatterns.unitofwork.registry;

import com.learning.enterprisepatterns.unitofwork.model.DomainObject;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class RepositoryRegistry {
    private final Map<String, JpaRepository> repositories;

    public JpaRepository getRepository(Class<? extends DomainObject> clazz) {
        return repositories.get(clazz.getSimpleName().toLowerCase() + "Repository");
    }
}
