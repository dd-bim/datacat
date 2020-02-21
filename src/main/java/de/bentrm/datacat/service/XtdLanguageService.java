package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdLanguage;
import de.bentrm.datacat.dto.XtdLanguageInputDto;
import org.springframework.data.domain.Page;

public interface XtdLanguageService {

    XtdLanguage findByUniqueId(String uniqueId);
    XtdLanguage findByLanguageRepresentationUniqueId(String uniqueId);
    Page<XtdLanguage> findAll();
    Page<XtdLanguage> findAll(int pageNumber, int pageSize);
    XtdLanguage create(XtdLanguageInputDto dto);

}
