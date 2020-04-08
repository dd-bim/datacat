package de.bentrm.datacat.domain;

import org.neo4j.ogm.annotation.RelationshipEntity;

@RelationshipEntity
public class Authorization {

    public enum Type {
        READ,
        WRITE
    }

    private Type type;

}
