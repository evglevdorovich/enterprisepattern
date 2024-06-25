package com.learning.enterprisepatterns.datamapper.model;

import com.learning.enterprisepatterns.common.model.Money;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private long id;
    private String lastName;
    private String firstName;
    private int numberOfDependents;

    public Person(String lastName, String firstName, int numberOfDependents) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.numberOfDependents = numberOfDependents;
    }

    //business logic
    public Money getExemption() {
        Money baseExemption = Money.dollars(1500);
        Money dependentExemption = Money.dollars(750);
        return baseExemption.add(dependentExemption.multiply(this.getNumberOfDependents()));
    }
}
