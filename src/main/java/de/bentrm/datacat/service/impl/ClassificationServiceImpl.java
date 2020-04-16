package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdClassification;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.repository.ClassificationRepository;
import de.bentrm.datacat.service.ClassificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional(readOnly = true)
public class ClassificationServiceImpl
        extends AbstractRootServiceImpl<XtdClassification, RootInput, RootUpdateInput, ClassificationRepository>
        implements ClassificationService {

    public ClassificationServiceImpl(ClassificationRepository repository) {
    	super(repository);
	}

    protected XtdClassification newEntityInstance() {
        return new XtdClassification();
    }
}
