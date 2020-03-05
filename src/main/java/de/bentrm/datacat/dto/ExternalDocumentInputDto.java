package de.bentrm.datacat.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ExternalDocumentInputDto {

    private String id;

    private @NotEmpty List<@Valid @NotNull TextInputDto> names = new ArrayList<>();

    public String getId() {
        return id;
    }

    public List<TextInputDto> getNames() {
        return names;
    }
}
