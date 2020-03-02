package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.collection.*;
import de.bentrm.datacat.repository.LanguageRepository;
import de.bentrm.datacat.repository.collection.CollectionRepository;
import de.bentrm.datacat.service.XtdCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class XtdCollectionServiceImpl extends NamedEntityServiceImpl<XtdCollection, CollectionRepository>
        implements XtdCollectionService {

    @Autowired
    public XtdCollectionServiceImpl(LanguageRepository languageRepository, CollectionRepository repository) {
        super(languageRepository, repository);
    }

    public XtdCollection create(NamedEntityInput inputDto) {
        XtdCollection newEntity;

        String label = inputDto.getLabel();
        if (label == null) {
            throw new IllegalArgumentException("No entity label defined.");
        }

        switch (label) {
            case "XtdBag": {
                newEntity = new XtdBag();
                break;
            }
            case "XtdNest": {
                newEntity = new XtdNest();
                break;
            }
            default:
                throw new UnsupportedOperationException("Unsupported input: " + inputDto);
        }

        if (inputDto.getId() != null && repository.existsById(inputDto.getId())) {
            throw new IllegalArgumentException("Unique ID is already in use.");
        }
        newEntity.setId(inputDto.getId());

        newEntity.setVersionId(inputDto.getVersionId());
        newEntity.setVersionDate(inputDto.getVersionDate());

//        inputDto.getNames().forEach(x -> newEntity.getNames().add(x));
//        inputDto.getDescriptions().forEach(newEntity::addDescription);

        return repository.save(newEntity);
    }
}
