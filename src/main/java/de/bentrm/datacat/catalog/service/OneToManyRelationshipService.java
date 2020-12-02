package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.XtdRelationship;
import de.bentrm.datacat.catalog.service.value.OneToManyRelationshipValue;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface OneToManyRelationshipService<T extends XtdRelationship> {

    @PreAuthorize("hasRole('USER')")
    @NotNull T create(@Valid OneToManyRelationshipValue value);

}
