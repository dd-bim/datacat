package de.bentrm.datacat.dto;

import java.util.List;

public interface XtdRootDto {

    String getUniqueId();
    List<XtdNameInputDto> getNames();
    List<XtdDescriptionInputDto> getDescriptions();

}
