package de.bentrm.datacat.graphql.input;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateTagInput {

    @NotBlank
    private String tagId;

    @NotBlank
    private String name;

}
