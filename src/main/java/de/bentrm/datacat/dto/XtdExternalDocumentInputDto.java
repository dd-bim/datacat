package de.bentrm.datacat.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class XtdExternalDocumentInputDto {

    private String uniqueId;
    private List<XtdNameInputDto> names;

    public String getUniqueId() {
        return uniqueId;
    }

    public List<XtdNameInputDto> getNames() {
        return names;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("uniqueId", uniqueId)
                .append("names", names)
                .toString();
    }
}
