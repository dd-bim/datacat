package de.bentrm.datacat.graphql.input;

import lombok.Data;

@Data
public class IntervalInput {
    Boolean minimumIncluded;
    Boolean maximumIncluded;
}
