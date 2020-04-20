package de.bentrm.datacat.graphql.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class RootInput extends EntityInput {

    @NotNull
    private String versionId;

    @NotNull
    private String versionDate;

    private List<@Valid @NotNull TextInput> descriptions = new ArrayList<>();

    public String getVersionId() {
        return versionId;
    }

    public String getVersionDate() {
        return versionDate;
    }

    public List<TextInput> getDescriptions() {
        return descriptions;
    }
}
