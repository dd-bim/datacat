package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = XtdObject.LABEL)
public abstract class XtdObject extends XtdRoot {

    public static final String LABEL = "XtdObject";

    // // bei CatalogRecord bereits vorhanden
    // // Set of names of the object in different languages. Each object may have multiple names, and this allows for its expression in terms of synonyms. At least a name shall be provided in international English and in the original language of its creator.
    // @Relationship(type = "NAMES")
    // private final Set<XtdMultiLanguageText> names = new HashSet<>();

    // // Data dictionary to which the object belongs to.
    // private XtdDictionary dictionary;

    // // Konzept in Metadaten vorhanden
    // // Date of creation of the concept.
    // private String XtdDateTime dateOfCreation;

    // major und minor Version in CatalogRecord

    // Status of the object during its life cycle.
    // private XtdStatusOfActivationEnum status;

    // // List of objects replaced by the current object.
    // @Relationship(type = "REPLACED_OBJECT")
    // private final Set<XtdObject> replacedObjects = new HashSet<>();

    // Incoming relations of above relation
    // @Relationship(type = "REPLACED_OBJECT", direction = Relationship.INCOMING)
    // private final Set<XtdObject> replacingObjects = new HashSet<>();

    // // Sentence explaining the reason of the deprecation, which can explain how to convert values to conform to the new object.
    // private XtdMultiLanguageText deprecationExplanation;

    @Relationship(type = XtdRelAssignsCollections.RELATIONSHIP_TYPE)
    private final Set<XtdRelAssignsCollections> assignedCollections = new HashSet<>();

    // @Relationship(type = XtdRelAssignsProperties.RELATIONSHIP_TYPE)
    // private final Set<XtdRelAssignsProperties> assignedProperties = new HashSet<>();

    @Override
    public List<XtdRelationship> getOwnedRelationships() {
        return Stream
                .of(
                    super.getOwnedRelationships(),
                    assignedCollections
                    // ,
                    // assignedProperties
                )
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
