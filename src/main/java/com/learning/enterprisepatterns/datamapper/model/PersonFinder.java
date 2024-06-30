package com.learning.enterprisepatterns.datamapper.model;

import java.util.List;

public interface PersonFinder {
    Person findById(Long id);

    List<Person> findByLastName(String lastName);
}
