package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.Facet;
import de.bentrm.datacat.service.dto.FacetInput;
import de.bentrm.datacat.service.dto.FacetUpdateInput;

public interface FacetService extends CrudEntityService<Facet, FacetInput, FacetUpdateInput> {
}
