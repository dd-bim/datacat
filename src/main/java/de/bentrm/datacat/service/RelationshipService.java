package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.relationship.XtdRelationship;
import de.bentrm.datacat.graphql.dto.RelationshipInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface RelationshipService<T extends XtdRelationship> extends EntityService<T> {

    @NotNull T create(@Valid RelationshipInput input);
    @NotNull T update(@Valid RootUpdateInput input);
    Optional<T> delete(@NotNull String id);

    T addRelatedObjects(String id, List<String> relatedObjectsIds);
    T removeRelatedObjects(String id, List<String> relatedObjectsIds);
}
