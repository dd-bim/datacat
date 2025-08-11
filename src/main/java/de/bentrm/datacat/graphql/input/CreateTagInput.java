package de.bentrm.datacat.graphql.input;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class CreateTagInput {
    private String tagId;

    @NotBlank
    private String name;
}
