package de.bentrm.datacat.domain;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity(label = Catalog.LABEL)
public class Catalog {

    public static final String LABEL = "CATALOG";

    @Relationship(type = "READS")
    private Set<User> readers;

    @Relationship(type = "WRITES")
    private Set<User> writers;

    public Set<User> getReaders() {
        return readers;
    }

    public void setReaders(Set<User> readers) {
        this.readers = readers;
    }

    public Set<User> getWriters() {
        return writers;
    }

    public void setWriters(Set<User> writers) {
        this.writers = writers;
    }
}
