package de.bentrm.datacat.util;

public interface QueryStatistics {

    boolean containsUpdates();

    int getNodesCreated();

    int getNodesDeleted();

    int getPropertiesSet();

    int getRelationshipsCreated();

    int getRelationshipsDeleted();

    int getLabelsAdded();

    int getLabelsRemoved();

    int getIndexesAdded();

    int getIndexesRemoved();

    int getConstraintsAdded();

    int getConstraintsRemoved();
}
