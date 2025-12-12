package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = false, onlyExplicitlyIncluded = true)
@Node(XtdDictionary.LABEL)
public class XtdDictionary extends XtdRoot {

    public static final String LABEL = "XtdDictionary";

    @Relationship(type = "NAME")
    private XtdMultiLanguageText name;

    @Lazy
    @Relationship(type = "DICTIONARY", direction = Relationship.Direction.INCOMING)
    private Set<XtdObject> concepts = new HashSet<>();
}
