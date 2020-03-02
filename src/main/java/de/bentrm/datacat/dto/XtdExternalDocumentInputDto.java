package de.bentrm.datacat.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class XtdExternalDocumentInputDto {

    private String id;
    private List<XtdNameInputDto> names;

    public String getId() {
        return id;
    }

    public List<XtdNameInputDto> getNames() {
        return names;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("names", names)
                .toString();
    }
}
