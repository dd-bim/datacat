package de.bentrm.datacat.repository.relationship;

import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import de.bentrm.datacat.repository.NamedEntityRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelGroupsRepository extends NamedEntityRepository<XtdRelGroups> {

    List<XtdRelGroups> findAllByRelatingObjectUniqueId(String uniqueId, int depth);

}
