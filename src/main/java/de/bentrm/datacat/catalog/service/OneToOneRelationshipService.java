package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.XtdRelationship;
import de.bentrm.datacat.catalog.service.value.OneToOneRelationshipValue;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Deprecated
public interface OneToOneRelationshipService<T extends XtdRelationship> {

    @PreAuthorize("hasRole('USER')")
    @NotNull T create(@Valid OneToOneRelationshipValue value);

}
