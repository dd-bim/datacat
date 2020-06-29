package de.bentrm.datacat.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity(label = "Facet")
@PropertyQueryHint({
        "(root)-[:NAMED|DESCRIBED*0..1]->(:Translation)"
})
public class Facet extends Entity {

    @Relationship(type = "NAMED")
    protected Set<Translation> names = new HashSet<>();

    @Relationship(type = "DESCRIBED")
    private final Set<Translation> descriptions = new HashSet<>();

    private final Set<EntityType> targets = new HashSet<>();

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

    public Set<EntityType> getTargets() {
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
