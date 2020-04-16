package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdSubject;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.repository.SubjectRepository;
import de.bentrm.datacat.service.SubjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional(readOnly = true)
public class SubjectServiceImpl
        extends CrudEntityServiceImpl<XtdSubject, RootInput, RootUpdateInput, SubjectRepository>
        implements SubjectService {

    public SubjectServiceImpl(SubjectRepository repository) {
    	super(repository);
	}

    protected XtdSubject newEntityInstance() {
        return new XtdSubject();
    }
}
