package de.bentrm.datacat.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.SortedSet;
import java.util.TreeSet;

@NodeEntity(label = "XtdEntity")
// TODO: Revise subgraph selection to be more specific to actual query and selected entity
@PropertyQueryHint({
        "(root)<-[:IS_NAME_OF|IS_DESCRIPTION_OF*0..1]-()",
        "(root)-[:DOCUMENTS]-()<-[:IS_NAME_OF|IS_DESCRIPTION_OF*0..1]-()",
        "(root)-[:COLLECTS]-()<-[:IS_NAME_OF|IS_DESCRIPTION_OF*0..1]-()",
        "(root)-[:ASSOCIATES]-()<-[:IS_NAME_OF|IS_DESCRIPTION_OF*0..1]-()",
        "(root)-[:COMPOSES]-()<-[:IS_NAME_OF|IS_DESCRIPTION_OF*0..1]-()",
        "(root)-[:GROUPS]-()<-[:IS_NAME_OF|IS_DESCRIPTION_OF*0..1]-()",
        "(root)-[:SPECIALIZES]-()<-[:IS_NAME_OF|IS_DESCRIPTION_OF*0..1]-()",
        "(root)-[:ACTS_UPON]-()<-[:IS_NAME_OF|IS_DESCRIPTION_OF*0..1]-()",
        "(root)-[:HAS_UNIT_COMPONENT|HAS_VALUE_DOMAIN*0..1]->()<-[:IS_NAME_OF|IS_DESCRIPTION_OF*0..1]-()"
})
public abstract class CatalogItem extends Entity {

    @Relationship(type = XtdName.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    protected SortedSet<XtdName> names = new TreeSet<>();

    public SortedSet<XtdName> getNames() {
        return this.names;
    }

    public void setNames(SortedSet<XtdName> names) {
        this.names = names;
    }

    public String getLabel() {
        return this.getNames().stream()
                .map(XtdName::getValue)
                .reduce((a, b) -> a + ", " + b)
                .orElseGet(() -> String.format("<%s>", this.getId()));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("label", this.getLabel())
                .append("names", names)
                .toString();
    }
}
