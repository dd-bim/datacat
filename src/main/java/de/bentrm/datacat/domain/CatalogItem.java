package de.bentrm.datacat.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity(label = "CatalogItem")
@PropertyQueryHint({
        "(root)-[:NAMED|DESCRIBED*0..1]->(:Translation)",
        "(root)-[:HAS_FACET]->(:Facet)",
        "(root)-[:DOCUMENTS]-()-[:NAMED|DESCRIBED*0..1]->(:Translation)",
        "(root)-[:COLLECTS]-()-[:NAMED|DESCRIBED*0..1]->(:Translation)",
        "(root)-[:ASSIGNS_COLLECTIONS]-()-[:NAMED|DESCRIBED*0..1]->(:Translation)",
        "(root)-[:ASSIGNS_PROPERTY_WITH_VALUES]-()-[:NAMED|DESCRIBED*0..1]->(:Translation)",
        "(root)-[:ASSOCIATES]-()-[:NAMED|DESCRIBED*0..1]->(:Translation)",
        "(root)-[:COMPOSES]-()-[:NAMED|DESCRIBED*0..1]->(:Translation)",
        "(root)-[:GROUPS]-()-[:NAMED|DESCRIBED*0..1]->(:Translation)",
        "(root)-[:SPECIALIZES]-()-[:NAMED|DESCRIBED*0..1]->(:Translation)",
        "(root)-[:ACTS_UPON]-()-[:NAMED|DESCRIBED*0..1]->(:Translation)",
        "(root)-[:HAS_UNIT_COMPONENT|HAS_VALUE_DOMAIN*0..1]->()-[:NAMED|DESCRIBED*0..1]->(:Translation)",
})
public abstract class CatalogItem extends Entity {

    @Relationship(type = "NAMED")
    private final Set<Translation> names = new HashSet<>();

    @Relationship(type = "HAS_FACET")
    private final Set<Facet> facets = new HashSet<>();

    public Set<Translation> getNames() {
        return this.names;
    }

    public Set<Facet> getFacets() {
        return this.facets;
    }

    public String getLabel() {
        return this.getNames().stream()
                .map(Translation::getLabel)
                .reduce((a, b) -> a + ", " + b)
                .orElseGet(() -> String.format("<%s>", this.getId()));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("label", this.getLabel())
                .append("names", names)
                .append("facets", facets)
                .toString();
    }
}
