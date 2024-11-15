package de.bentrm.datacat.graphql.input;

import lombok.Data;

import jakarta.validation.Valid;

@Data
public class EntryFilterInput {

    @Valid EntryTypeFilterInput entryType;
    @Valid TagFilterInput tags;
}
