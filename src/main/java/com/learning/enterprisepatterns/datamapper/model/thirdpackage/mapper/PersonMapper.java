package com.learning.enterprisepatterns.datamapper.model.thirdpackage.mapper;

import com.learning.enterprisepatterns.datamapper.model.Person;
import com.learning.enterprisepatterns.datamapper.model.PersonFinder;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonMapper extends AbstractMapper<Person> implements PersonFinder {
    private static final String COLUMNS = "id, first_name, last_name, number_of_dependents";

    private static final String FIND_STATEMENT = """
            SELECT %s FROM people
            WHERE ID = ?
            """.formatted(COLUMNS);

    private static final String FIND_LAST_NAME_STATEMENT =
            "SELECT " + COLUMNS +
                    " FROM people " +
                    " WHERE last_name = ?" +
                    " ORDER BY last_name";

    private static final String UPDATE_STATEMENT_STRING =
            "UPDATE people " +
                    " SET last_name = ?, first_name = ?, number_of_dependents = ? " +
                    " WHERE id = ?";

    private static final String INSERT_PERSON = """
            INSERT INTO
                people (last_name, first_name, number_of_dependents)
            VALUES (?, ?, ?)
            RETURNING id;
            """;

    public Person findById(Long id) {
        return abstractFind(id);
    }

    public List<Person> findByLastName(String lastName) {
        return findMany(new FindByLastName(lastName));
    }

    public void update(Person person) {
        update(new UpdatePerson(person));
    }

    public Person insert(Person person) {
        val id = insert(new InsertPerson(person), person);
        person.setId(id);
        return person;
    }

    @Override
    protected String findStatement() {
        return FIND_STATEMENT;
    }

    @Override
    protected Class<Person> getMappedClass() {
        return Person.class;
    }

    @Override
    protected Person doLoad(SqlRowSet rowSet) {
        val firstName = rowSet.getString("first_name");
        val lastName = rowSet.getString("last_name");
        val numberOfDependents = rowSet.getInt("number_of_dependents");
        val id = rowSet.getLong("id");
        return new Person(id, lastName, firstName, numberOfDependents);
    }

    @AllArgsConstructor
    private static class FindByLastName implements StatementSource {

        private String lastName;

        @Override
        public String getSql() {
            return FIND_LAST_NAME_STATEMENT;
        }

        @Override
        public Object[] getArguments() {
            return new Object[]{lastName};
        }
    }

    @AllArgsConstructor
    private static class UpdatePerson implements StatementSource {

        private Person person;

        @Override
        public String getSql() {
            return UPDATE_STATEMENT_STRING;
        }

        @Override
        public Object[] getArguments() {
            return new Object[]{person.getLastName(), person.getFirstName(), person.getNumberOfDependents(), person.getId()};
        }
    }

    @AllArgsConstructor
    private static class InsertPerson implements StatementSource {

        private Person person;

        @Override
        public String getSql() {
            return INSERT_PERSON;
        }

        @Override
        public Object[] getArguments() {
            return new Object[]{person.getLastName(), person.getFirstName(), person.getNumberOfDependents()};
        }
    }
}
