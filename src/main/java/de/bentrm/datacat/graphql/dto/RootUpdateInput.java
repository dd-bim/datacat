package de.bentrm.datacat.graphql.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class RootUpdateInput extends EntityUpdateInput {

    @NotNull
    private String versionId;

    @NotNull
    private String versionDate;

    private final List<@Valid @NotNull TextInput> descriptions = new ArrayList<>();

    public String getVersionId() {
        return versionId;
    }

    public String getVersionDate() {
        return versionDate;
    }

    public List<TextInput> getDescriptions() {
        return descriptions;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("versionId", versionId)
                .append("versionDate", versionDate)
                .append("descriptions", descriptions)
                .toString();
    }
}
