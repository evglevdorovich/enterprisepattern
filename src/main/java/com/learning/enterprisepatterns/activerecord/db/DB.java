package com.learning.enterprisepatterns.activerecord.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

@Service
public class DB {
    private static JdbcTemplate jdbcTemplate;

    public DB(JdbcTemplate jdbcTemplate) {
        DB.jdbcTemplate = jdbcTemplate;
    }

    public static void update(String sql, Object... args) {
        validateInitialisation();
        jdbcTemplate.update(sql, args);
    }

    public static SqlRowSet queryForRowSet(String sql, Object... args) {
        validateInitialisation();
        return jdbcTemplate.queryForRowSet(sql, args);
    }

    private static void validateInitialisation() {
        if (jdbcTemplate == null) {
            throw new RuntimeException("jdbcTemplate is null");
        }
    }
}
