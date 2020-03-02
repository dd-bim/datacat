package de.bentrm.datacat.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class XtdRelGroupsInputDto implements XtdRootDto {

    private String id;
    private List<XtdNameInputDto> names = new ArrayList<>();
    private List<XtdDescriptionInputDto> descriptions = new ArrayList<>();
    private String relatingObjectId;
    private List<String> relatedObjectsIds;

    public String getId() {
        return id;
    }

    public List<XtdNameInputDto> getNames() {
        return names;
    }

    public List<XtdDescriptionInputDto> getDescriptions() {
        return descriptions;
    }

    public String getRelatingObjectId() {
        return relatingObjectId;
    }

    public List<String> getRelatedObjectsIds() {
        return relatedObjectsIds;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("names", names)
                .append("descriptions", descriptions)
                .append("relatingObjectId", relatingObjectId)
                .append("relatedObjectsIds", relatedObjectsIds)
                .toString();
    }
}
