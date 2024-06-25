package com.learning.enterprisepatterns.functionaltests.datamapper;

import com.learning.enterprisepatterns.datamapper.model.Person;
import com.learning.enterprisepatterns.datamapper.model.PersonMapper;
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
public class DataMapperIt {

    @Test
    void testDataMapper() {
        Registry.start();

        val personMapper = new PersonMapper();
        val person1 = new Person("last_name", "first_name", 2);
        val updatedPerson1 = personMapper.insert(person1);

        Registry.end();
        Registry.start();
        val person1AfterReset = personMapper.findByLastName("last_name").get(0);

        assertThat(person1AfterReset).isEqualTo(updatedPerson1);

        personMapper.update(person1AfterReset);
        Registry.end();
    }
}
