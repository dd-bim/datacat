package de.bentrm.datacat.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.neo4j.ogm.annotation.Relationship;

import java.util.*;

public abstract class NamedEntity extends UniqueEntity {

    @Relationship(type = XtdName.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    protected SortedSet<XtdName> names = new TreeSet<>();

    public SortedSet<XtdName> getNames() {
        return this.names;
    }

    public void setNames(SortedSet<XtdName> names) {
        this.names = names;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("names", names)
                .toString();
    }
}
