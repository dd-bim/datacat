package de.bentrm.datacat.graphql.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RootInput extends EntityInput {

    @NotNull
    private String versionId;

    @NotNull
    private String versionDate;

    private final List<@Valid @NotNull TextInput> descriptions = new ArrayList<>();
}
