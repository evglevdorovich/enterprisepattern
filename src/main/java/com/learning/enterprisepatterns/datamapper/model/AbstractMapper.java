package com.learning.enterprisepatterns.datamapper.model;

import com.learning.enterprisepatterns.activerecord.db.DB;
import com.learning.enterprisepatterns.registry.Registry;
import lombok.val;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMapper<T> {

    abstract String findStatement();

    abstract Class<T> getMappedClass();

    abstract T doLoad(SqlRowSet sqlRowSet);

    protected T abstractFind(Long id) {

        T result = Registry.get(id, getMappedClass());

        if (result == null) {
            val rowSet = DB.queryForRowSet(findStatement(), id);
            rowSet.next();
            result = load(rowSet);
        }

        return result;
    }

    protected T load(SqlRowSet rowSet) {
        val id = rowSet.getLong("id");
        T result = Registry.get(id, getMappedClass());

        if (result == null) {
            result = doLoad(rowSet);
            Registry.register(id, result, getMappedClass());
        }

        return result;
    }

    protected List<T> loadAll(SqlRowSet rowSet) {
        val result = new ArrayList<T>();

        while (rowSet.next()) {
            result.add(load(rowSet));
        }

        return result;
    }

    protected List<T> findMany(StatementSource statementSource) {
        return loadAll(DB.queryForRowSet(statementSource.getSql(), statementSource.getArguments()));
    }

    protected void update(StatementSource statementSource) {
        DB.update(statementSource.getSql(), statementSource.getArguments());
    }

    protected Long insert(StatementSource statementSource, T object) {
        val rowSet = DB.queryForRowSet(statementSource.getSql(), statementSource.getArguments());
        rowSet.next();
        val id = rowSet.getLong("id");
        Registry.register(id, object, getMappedClass());
        return id;
    }
}
