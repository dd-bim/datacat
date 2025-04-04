package de.bentrm.datacat.graphql.input;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Data
public class SymbolInput {
    @NotNull @Valid TranslationInput symbol;
}
