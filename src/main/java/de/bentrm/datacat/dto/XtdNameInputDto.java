package de.bentrm.datacat.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotBlank;

public class XtdNameInputDto {

    private String id;

    @NotBlank
    private String languageCode;

    @NotBlank
    private String name;

    private Integer sortOrder;

    private Boolean ignoreDuplicate;

    public String getId() {
        return id;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getName() {
        return name;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public Boolean isIgnoreDuplicate() {
        return ignoreDuplicate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("languageCode", languageCode)
                .append("name", name)
                .append("sortOrder", sortOrder)
                .append("ignoreDuplicate", ignoreDuplicate)
                .toString();
    }
}
