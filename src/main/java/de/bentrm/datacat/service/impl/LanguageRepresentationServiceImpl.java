package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdDescription;
import de.bentrm.datacat.domain.XtdLanguage;
import de.bentrm.datacat.domain.XtdName;
import de.bentrm.datacat.domain.XtdObject;
import de.bentrm.datacat.dto.XtdDescriptionInputDto;
import de.bentrm.datacat.dto.XtdNameInputDto;
import de.bentrm.datacat.repository.LanguageRepository;
import de.bentrm.datacat.repository.ObjectRepository;
import de.bentrm.datacat.service.LanguageRepresentationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.SortedSet;

@Service
@Transactional
class LanguageRepresentationServiceImpl implements LanguageRepresentationService {

    private final LanguageRepository languageRepository;
    private final ObjectRepository repository;

    @Autowired
    public LanguageRepresentationServiceImpl(LanguageRepository languageRepository, ObjectRepository repository) {
        this.languageRepository = languageRepository;
        this.repository = repository;
    }

    public XtdObject addName(@NotBlank String parentUniqueId, @NotNull XtdNameInputDto dto) {
        XtdObject parent = repository.findByUniqueId(parentUniqueId, 2);
        if (parent == null) {
            throw new IllegalArgumentException("Parent not found.");
        }

        XtdLanguage language = languageRepository.findByLanguageCode(dto.getLanguageCode());
        if (language == null) {
            throw new IllegalArgumentException("Unknown language code provided.");
        }

        XtdName newName = new XtdName();
        newName.setUniqueId(dto.getUniqueId());
        newName.setLanguageName(language);
        newName.setName(dto.getName());

        SortedSet<XtdName> existingNamesSet = parent.getNames();
        int newSortOrder = existingNamesSet.size(); // default to add element at the add of the sorted set

        existingNamesSet.forEach(name -> {
            if (name.getName().equals(dto.getName()) && name.getLanguageName().equals(newName.getLanguageName())) {
                throw new IllegalArgumentException("Exact duplicate found.");
            }
        });

        if (dto.getSortOrder() != null) {
            newSortOrder = dto.getSortOrder();

            if (newSortOrder < 0) {
                throw new IllegalArgumentException("Invalid parameter sortOrder. Minimum index is 0.");
            }
            if (newSortOrder > existingNamesSet.size()) {
                throw new IllegalArgumentException("Invalid parameter sortOrder. Maximum index is " + existingNamesSet.size());
            }

            // update position of tailing elements in sorted set
            for (XtdName name : existingNamesSet) {
                int currentSortOrder = name.getSortOrder();
                if (currentSortOrder >= newSortOrder) {
                    name.setSortOrder(currentSortOrder + 1);
                }
            }
        }

        newName.setSortOrder(newSortOrder);
        existingNamesSet.add(newName);
        return repository.save(parent);
    }

    public XtdObject updateName(String parentUniqueId, String uniqueId, String newName) {
        XtdObject parent = repository.findByUniqueId(parentUniqueId, 2);
        if (parent == null) {
            throw new IllegalArgumentException("Parent not found.");
        }

        if (uniqueId == null) {
            throw new IllegalArgumentException("Missing unique id of name.");
        }

        XtdName name = null;
        for (XtdName cur : parent.getNames()) {
            if (cur.getUniqueId().equals(uniqueId)) {
                name = cur;
            }
        }
        if (name == null) {
            throw new IllegalArgumentException("No name with given unique id found for parent " + parentUniqueId);
        }

        name.setName(newName);
        return repository.save(parent);
    }

    public XtdObject deleteName(String parentUniqueId, String uniqueId) {
        XtdObject parent = repository.findByUniqueId(parentUniqueId, 2);

        if (parent.getNames().size() <= 1) {
            throw new IllegalArgumentException("Parent only has one name and may not be unnamed.");
        }

        parent.getNames().removeIf(cur -> cur.getUniqueId().equals(uniqueId));
        return repository.save(parent);
    }

    public XtdObject addDescription(String parentUniqueId, XtdDescriptionInputDto dto) {
        XtdObject parent = repository.findByUniqueId(parentUniqueId, 2);
        if (parent == null) {
            throw new IllegalArgumentException("Parent not found.");
        }

        XtdLanguage language = languageRepository.findByLanguageCode(dto.getLanguageCode());
        if (language == null) {
            throw new IllegalArgumentException("Unknown language code provided.");
        }

        XtdDescription newDescription = new XtdDescription();
        newDescription.setUniqueId(dto.getUniqueId());
        newDescription.setLanguageName(language);
        newDescription.setDescription(dto.getDescription());

        SortedSet<XtdDescription> existingDescriptionsSet = parent.getDescriptions();
        int newSortOrder = existingDescriptionsSet.size(); // default to add element at the add of the sorted set

        existingDescriptionsSet.forEach(name -> {
            if (name.getDescription().equals(dto.getDescription()) && name.getLanguageName().equals(newDescription.getLanguageName())) {
                throw new IllegalArgumentException("Exact duplicate found.");
            }
        });

        if (dto.getSortOrder() != null) {
            newSortOrder = dto.getSortOrder();

            if (newSortOrder < 0) {
                throw new IllegalArgumentException("Invalid parameter sortOrder. Minimum index is 0.");
            }
            if (newSortOrder > existingDescriptionsSet.size()) {
                throw new IllegalArgumentException("Invalid parameter sortOrder. Maximum index is " + existingDescriptionsSet.size());
            }

            // update position of tailing elements in sorted set
            for (var desc : existingDescriptionsSet) {
                int currentSortOrder = desc.getSortOrder();
                if (currentSortOrder >= newSortOrder) {
                    desc.setSortOrder(currentSortOrder + 1);
                }
            }
        }

        newDescription.setSortOrder(newSortOrder);
        existingDescriptionsSet.add(newDescription);
        return repository.save(parent);
    }

    public XtdObject updateDescription(String parentUniqueId, String uniqueId, String newDescription) {
        XtdObject parent = repository.findByUniqueId(parentUniqueId, 2);
        if (parent == null) {
            throw new IllegalArgumentException("Parent not found.");
        }

        if (uniqueId == null) {
            throw new IllegalArgumentException("Missing unique id of description.");
        }

        XtdDescription description = null;
        for (XtdDescription cur : parent.getDescriptions()) {
            if (cur.getUniqueId().equals(uniqueId)) {
                description = cur;
            }
        }
        if (description == null) {
            throw new IllegalArgumentException("No description with given unique id found for parent " + parentUniqueId);
        }

        description.setDescription(newDescription);
        return repository.save(parent);
    }

    public XtdObject deleteDescription(String parentUniqueId, String uniqueId) {
        XtdObject parent = repository.findByUniqueId(parentUniqueId, 2);
        parent.getDescriptions().removeIf(cur -> cur.getUniqueId().equals(uniqueId));
        return repository.save(parent);
    }
}
