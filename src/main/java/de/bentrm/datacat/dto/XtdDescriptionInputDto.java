package de.bentrm.datacat.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotBlank;

public class XtdDescriptionInputDto {

    private String uniqueId;

    @NotBlank
    private String languageCode;

    @NotBlank
    private String description;

    private Integer sortOrder;

    public String getUniqueId() {
        return uniqueId;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getDescription() {
        return description;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("uniqueId", uniqueId)
                .append("languageCode", languageCode)
                .append("description", description)
                .append("sortOrder", sortOrder)
                .toString();
    }

}
