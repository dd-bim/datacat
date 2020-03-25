package de.bentrm.datacat.graphql.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class RootUpdateInput {

    @NotBlank
    private String id;

    @NotNull
    private String versionId;

    @NotNull
    private String versionDate;

    public String getId() {
        return id;
    }

    private @NotEmpty List<@Valid @NotNull TextInput> names = new ArrayList<>();

    private List<@Valid @NotNull TextInput> descriptions = new ArrayList<>();

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
