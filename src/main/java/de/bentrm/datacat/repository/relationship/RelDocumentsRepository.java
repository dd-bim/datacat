package de.bentrm.datacat.repository.relationship;

import de.bentrm.datacat.domain.relationship.XtdRelDocuments;
import de.bentrm.datacat.repository.NamedEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelDocumentsRepository extends NamedEntityRepository<XtdRelDocuments> {

}
