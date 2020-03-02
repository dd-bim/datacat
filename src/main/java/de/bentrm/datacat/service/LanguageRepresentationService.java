package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdObject;
import de.bentrm.datacat.dto.XtdDescriptionInputDto;
import de.bentrm.datacat.dto.XtdNameInputDto;

public interface LanguageRepresentationService {

    XtdObject addName(String parentId, XtdNameInputDto dto);
    XtdObject updateName(String parentId, String id, String newName);
    XtdObject deleteName(String parentId, String id);

    XtdObject addDescription(String parentId, XtdDescriptionInputDto dto);
    XtdObject updateDescription(String parentId, String id, String newDescription);
    XtdObject deleteDescription(String parentId, String id);

}
