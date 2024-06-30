package com.learning.enterprisepatterns.datamapper.model.thirdpackage.mapper;

public interface StatementSource {
    String getSql();

    Object[] getArguments();
}
