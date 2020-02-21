package de.bentrm.datacat.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class XtdRelGroupsInputDto implements XtdRootDto {

    private String uniqueId;
    private List<XtdNameInputDto> names = new ArrayList<>();
    private List<XtdDescriptionInputDto> descriptions = new ArrayList<>();
    private String relatingObjectUniqueId;
    private List<String> relatedObjectsUniqueIds;

    public String getUniqueId() {
        return uniqueId;
    }

    public List<XtdNameInputDto> getNames() {
        return names;
    }

    public List<XtdDescriptionInputDto> getDescriptions() {
        return descriptions;
    }

    public String getRelatingObjectUniqueId() {
        return relatingObjectUniqueId;
    }

    public List<String> getRelatedObjectsUniqueIds() {
        return relatedObjectsUniqueIds;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("uniqueId", uniqueId)
                .append("names", names)
                .append("descriptions", descriptions)
                .append("relatingObjectUniqueId", relatingObjectUniqueId)
                .append("relatedObjectsUniqueIds", relatedObjectsUniqueIds)
                .toString();
    }
}
