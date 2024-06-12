package com.learning.enterprisepatterns.activerecord.model;

import com.learning.enterprisepatterns.activerecord.db.DB;
import com.learning.enterprisepatterns.registry.Registry;
import com.learning.enterprisepatterns.common.model.Money;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;
import org.springframework.jdbc.support.rowset.SqlRowSet;

@Data
@NoArgsConstructor
public class Person {
    private String lastName;
    private String firstName;
    private int numberOfDependents;
    private long id;

    public Person(String lastName, String firstName, int numberOfDependents) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.numberOfDependents = numberOfDependents;
    }

    private static final String FIND_PERSON_BY_ID = """
            SELECT
                id, last_name, first_name, number_of_dependents
            FROM
                people
            WHERE
                id = ?
            """;

    private static final String INSERT_PERSON = """
            INSERT INTO
                people (last_name, first_name, number_of_dependents)
            VALUES (?, ?, ?)
            RETURNING id;
            """;

    private static final String UPDATE_PERSON = """
            UPDATE
                people
            SET
                last_name=?, first_name=?, number_of_dependents=?
            WHERE
                id=?
            """;

    public static Person findById(Long id) {
        var registeredPerson = Registry.getPerson(id);
        if (registeredPerson != null) {
            return registeredPerson;
        }

        val rowSet = DB.queryForRowSet(FIND_PERSON_BY_ID, new Object[]{id});
        rowSet.next();
        return load(rowSet);
    }

    public Long insert() {
        val rowSet = DB.queryForRowSet(INSERT_PERSON, lastName, firstName, numberOfDependents);
        rowSet.next();
        id = rowSet.getLong("id");
        registerPerson();
        return id;
    }

    public void update() {
        DB.update(UPDATE_PERSON, lastName, firstName, numberOfDependents, id);
        registerPerson();
    }

    private void registerPerson() {
        Registry.register(id, this, Person.class);
    }

    private static Person load(SqlRowSet rowSet) {
        val person = new Person();
        person.firstName = rowSet.getString("first_name");
        person.lastName = rowSet.getString("last_name");
        person.numberOfDependents = rowSet.getInt("number_of_dependents");
        person.id = rowSet.getLong("id");
        Registry.register(person.id, person, Person.class);
        return person;
    }

    //business logic
    public Money getExemption() {
        Money baseExemption = Money.dollars(1500);
        Money dependentExemption = Money.dollars(750);
        return baseExemption.add(dependentExemption.multiply(this.getNumberOfDependents()));
    }
}
