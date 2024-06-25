package com.learning.enterprisepatterns.datamapper.model;

public interface StatementSource {
    String getSql();

    Object[] getArguments();
}
