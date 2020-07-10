package de.bentrm.datacat.util;

import lombok.Value;

@Value
public class QueryStatisticsWrapper {
    int nodesCreated;
    int nodesDeleted;
    int propertiesSet;
    int relationshipsCreated;
    int relationshipsDeleted;
    int labelsAdded;
    int labelsRemoved;
    int indexesAdded;
    int indexesRemoved;
    int constraintsAdded;
    int constraintsRemoved;
}
