package com.learning.enterprisepatterns.functionaltests.activerecord;

import com.learning.enterprisepatterns.activerecord.model.Person;
import com.learning.enterprisepatterns.registry.Registry;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@DirtiesContext
public class PersonIt {

    @Test
    void testPersons() {
        Registry.start();
        val person = new Person("Alex", "Waxer", 2);
        val expectedPerson = new Person("NEW_LAST_NAME", "NEW_FIRST_NAME", 3);
        expectedPerson.setId(1L);

        person.insert();

        person.setFirstName("NEW_FIRST_NAME");
        person.setLastName("NEW_LAST_NAME");
        person.setNumberOfDependents(3);

        person.update();
        Registry.end();
        Registry.start();

        assertThat(Person.findById(1L)).isEqualTo(expectedPerson);
        Registry.end();
    }
}
