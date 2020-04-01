package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.XtdSubject;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends GraphEntityRepository<XtdSubject, String> { }
