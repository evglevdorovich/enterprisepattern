package com.learning.enterprisepatterns.unitofwork.model;

import com.learning.enterprisepatterns.unitofwork.UnitOfWork;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class DomainObject {

    @Id
    public abstract Long getId();

    protected void markDirty() {
        UnitOfWork.getCurrent().registerDirty(this);
    }

    protected void markRemoved() {
        UnitOfWork.getCurrent().registerRemoved(this);
    }

    protected void markNew() {
        UnitOfWork.getCurrent().registerNew(this);
    }

    protected void markClean() {
        UnitOfWork.getCurrent().registerClean(this);
    }
}
