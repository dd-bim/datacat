package de.bentrm.datacat.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class RootInputDto {

    private String id;

    private String versionId;

    private String versionDate;

    private @NotEmpty List<@Valid @NotNull TextInputDto> names = new ArrayList<>();

    private List<TextInputDto> descriptions;

    public String getId() {
        return id;
    }

    public String getVersionId() {
        return versionId;
    }

    public String getVersionDate() {
        return versionDate;
    }

    public List<TextInputDto> getNames() {
        return names;
    }

    public List<TextInputDto> getDescriptions() {
        return descriptions;
    }
}
