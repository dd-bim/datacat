package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.validation.IdConstraint;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class RootInput {

    @IdConstraint
    private String id;

    @NotNull
    private String versionId;

    @NotNull
    private String versionDate;

    private @NotEmpty List<@Valid @NotNull TextInput> names = new ArrayList<>();

    private List<@Valid @NotNull TextInput> descriptions = new ArrayList<>();

    public String getId() {
        return id;
    }

    public String getVersionId() {
        return versionId;
    }

    public String getVersionDate() {
        return versionDate;
    }

    public List<TextInput> getNames() {
        return names;
    }

    public List<TextInput> getDescriptions() {
        return descriptions;
    }
}
