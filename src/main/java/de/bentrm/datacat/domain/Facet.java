package de.bentrm.datacat.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NodeEntity("Facet")
public class Facet extends Entity {

    @Relationship(type = "NAMED")
    protected Set<Translation> names = new HashSet<>();

    @Relationship(type = "DESCRIBED")
    private final Set<Translation> descriptions = new HashSet<>();

    private final List<EntityType> targets = new ArrayList<>();

    public String getLabel() {
        return this.getNames().stream()
                .map(Translation::getLabel)
                .reduce((a, b) -> a + ", " + b)
                .orElseGet(() -> String.format("<%s>", this.getId()));
    }

    public Set<Translation> getNames() {
        return names;
    }

    public Set<Translation> getDescriptions() {
        return descriptions;
    }

    public List<EntityType> getTargets() {
        return targets;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("names", names)
                .append("descriptions", descriptions)
                .append("targets", targets)
                .toString();
    }
}
