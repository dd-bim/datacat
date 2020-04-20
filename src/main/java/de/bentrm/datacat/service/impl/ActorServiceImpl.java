package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdActor;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.repository.ActorRepository;
import de.bentrm.datacat.service.ActorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional(readOnly = true)
public class ActorServiceImpl
		extends CrudRootServiceImpl<XtdActor, RootInput, RootUpdateInput, ActorRepository>
		implements ActorService {

	public ActorServiceImpl(ActorRepository repository) {
		super(repository);
	}

	@Override
	protected XtdActor newEntityInstance() {
		return new XtdActor();
	}
}
