package com.learning.enterprisepatterns.lazyload.virtualProxy;

import com.learning.enterprisepatterns.activerecord.db.DB;
import com.learning.enterprisepatterns.lazyload.model.Producer;
import lombok.val;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class ProducerMapper implements ProducerFinder {

    private static final String COLUMNS = "id, name";

    private static final String FIND_STATEMENT = """
            SELECT %s FROM producers
            WHERE ID = ?
            """.formatted(COLUMNS);

    @Override
    public Producer findById(int id) {
        val rowSet = DB.queryForRowSet(FIND_STATEMENT, id);
        rowSet.next();
        return load(rowSet);
    }

    private static Producer load(SqlRowSet rowSet) {
        val result = new Producer();
        result.setId(rowSet.getInt("id"));
        result.setName(rowSet.getString("name"));

        return result;
    }
}
