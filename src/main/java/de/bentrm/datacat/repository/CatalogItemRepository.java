package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.CatalogItem;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogItemRepository extends GraphEntityRepository<CatalogItem>, CatalogItemRepositoryExtension {
}
