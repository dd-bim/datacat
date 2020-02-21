package de.bentrm.datacat.repository.relationship;

import de.bentrm.datacat.domain.relationship.XtdRelAssociates;
import de.bentrm.datacat.repository.NamedEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelAssociatesRepository extends NamedEntityRepository<XtdRelAssociates> {
}
