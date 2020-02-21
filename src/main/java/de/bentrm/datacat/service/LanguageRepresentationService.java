package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdObject;
import de.bentrm.datacat.dto.XtdDescriptionInputDto;
import de.bentrm.datacat.dto.XtdNameInputDto;

public interface LanguageRepresentationService {

    XtdObject addName(String parentUniqueId, XtdNameInputDto dto);
    XtdObject updateName(String parentUniqueId, String uniqueId, String newName);
    XtdObject deleteName(String parentUniqueId, String uniqueId);

    XtdObject addDescription(String parentUniqueId, XtdDescriptionInputDto dto);
    XtdObject updateDescription(String parentUniqueId, String uniqueId, String newDescription);
    XtdObject deleteDescription(String parentUniqueId, String uniqueId);

}
