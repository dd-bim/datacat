package de.bentrm.datacat.graphql.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class XtdRelGroupsInput {

    private String id;
    private final List<TextInput> names = new ArrayList<>();
    private final List<TextInput> descriptions = new ArrayList<>();
    private String relatingObjectId;
    private List<String> relatedObjectsIds;

    public String getId() {
        return id;
    }

    public List<TextInput> getNames() {
        return names;
    }

    public List<TextInput> getDescriptions() {
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
