package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.relationship.XtdRelAssignsCollections;
import de.bentrm.datacat.graphql.dto.AssignsCollectionsInput;
import de.bentrm.datacat.graphql.dto.AssignsCollectionsUpdateInput;

public interface RelAssignsCollectionsService extends CrudEntityService<XtdRelAssignsCollections, AssignsCollectionsInput, AssignsCollectionsUpdateInput> {

}
