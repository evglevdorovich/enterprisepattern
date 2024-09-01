package com.learning.enterprisepatterns.unitofwork.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Getter
@Entity
@NoArgsConstructor
@Data
public class Album extends DomainObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    public Album(String title) {
        this.title = title;
        markNew();
    }

    public void setTitle(String title) {
        this.title = title;
        markDirty();
    }

    public void remove() {
        markRemoved();
    }

}
