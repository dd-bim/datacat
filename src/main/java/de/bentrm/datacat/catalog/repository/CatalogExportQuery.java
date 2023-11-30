package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.domain.ExportItemResult;
import de.bentrm.datacat.catalog.domain.ExportRelationshipResult;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.neo4j.driver.Result;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public interface CatalogExportQuery extends EntityRepository<XtdRoot> {
    
    /* gibt alle Katalogeinträge mit ihren Eigenschaften aus */
    @Query("""
    MATCH(x)-[:NAMED]->(t:Translation) 
    WHERE (x:XtdRoot OR x:XtdExternalDocument) 
    AND t.languageCode="de" 
    OPTIONAL MATCH(x)-[:NAMED]->(ten:Translation)
    WHERE ten.languageCode="en" 
    OPTIONAL MATCH(x)-[:TAGGED]->(tag)
    OPTIONAL MATCH(x)-[:DESCRIBED]->(d) 
    WHERE d.languageCode="de"
    CALL apoc.case([
    "XtdBag" IN(LABELS(x)), 'RETURN "XtdBag" AS type',
    "XtdNest" IN(LABELS(x)), 'RETURN "XtdNest" AS type',
    "XtdProperty" IN(LABELS(x)), 'RETURN "XtdProperty" AS type',
    "XtdMeasureWithUnit" IN(LABELS(x)), 'RETURN "XtdMeasureWithUnit" AS type',
    "XtdUnit" IN(LABELS(x)), 'RETURN "XtdUnit" AS type',
    "XtdValue" IN(LABELS(x)), 'RETURN "XtdValue" AS type',
    "XtdSubject" IN(LABELS(x)), 'RETURN "XtdSubject" AS type',
    "XtdExternalDocument" IN(LABELS(x)), 'RETURN "XtdExternalDocument" AS type'
    ], 'RETURN null AS type') YIELD value
    RETURN DISTINCT 
    x.id AS id,  
    value.type AS typ, 
    collect(tag.name) AS tags , 
    t.label AS name,
    ten.label AS name_en,
    d.label AS description,
    x.versionId AS versionId,
    x.createdBy AS createdBy,
    x.created AS created,
    x.lastModified AS lastModified,
    x.lastModifiedBy AS lastModifiedBy
    """)
    List<ExportItemResult> findExportCatalogItems();


    /* gibt alle Relationen zwischen Katalogeinträgen aus */
    @Query("""
    MATCH(x)-[a]->(y:XtdRelationship)-[b]->(z) 
    WHERE type(a)=type(b) 
    CALL apoc.case([
    "XtdRelAssignsCollections" IN(LABELS(y)), 'RETURN "XtdRelAssignsCollections" AS type',
    "XtdRelAssignsMeasures" IN(LABELS(y)), 'RETURN "XtdRelAssignsMeasures" AS type',
    "XtdRelAssignsProperties" IN(LABELS(y)), 'RETURN "XtdRelAssignsProperties" AS type',
    "XtdRelAssignsValues" IN(LABELS(y)), 'RETURN "XtdRelAssignsValues" AS type',
    "XtdRelAssignsUnits" IN(LABELS(y)), 'RETURN "XtdRelAssignsUnits" AS type',
    "XtdRelCollects" IN(LABELS(y)), 'RETURN "XtdRelCollects" AS type',
    "XtdRelDocuments" IN(LABELS(y)), 'RETURN "XtdRelDocuments" AS type'
    ], 'RETURN null AS type') YIELD value
    CALL apoc.case([
    "XtdBag" IN(LABELS(x)), 'RETURN "XtdBag" AS type',
    "XtdNest" IN(LABELS(x)), 'RETURN "XtdNest" AS type',
    "XtdProperty" IN(LABELS(x)), 'RETURN "XtdProperty" AS type',
    "XtdMeasureWithUnit" IN(LABELS(x)), 'RETURN "XtdMeasureWithUnit" AS type',
    "XtdUnit" IN(LABELS(x)), 'RETURN "XtdUnit" AS type',
    "XtdValue" IN(LABELS(x)), 'RETURN "XtdValue" AS type',
    "XtdSubject" IN(LABELS(x)), 'RETURN "XtdSubject" AS type',
    "XtdExternalDocument" IN(LABELS(x)), 'RETURN "XtdExternalDocument" AS type'
    ], 'RETURN null AS type') YIELD value AS entity1Type
    CALL apoc.case([
    "XtdBag" IN(LABELS(z)), 'RETURN "XtdBag" AS type',
    "XtdNest" IN(LABELS(z)), 'RETURN "XtdNest" AS type',
    "XtdProperty" IN(LABELS(z)), 'RETURN "XtdProperty" AS type',
    "XtdMeasureWithUnit" IN(LABELS(z)), 'RETURN "XtdMeasureWithUnit" AS type',
    "XtdUnit" IN(LABELS(z)), 'RETURN "XtdUnit" AS type',
    "XtdValue" IN(LABELS(z)), 'RETURN "XtdValue" AS type',
    "XtdSubject" IN(LABELS(z)), 'RETURN "XtdSubject" AS type',
    "XtdExternalDocument" IN(LABELS(z)), 'RETURN "XtdExternalDocument" AS type'
    ], 'RETURN null AS type') YIELD value AS entity2Type
    RETURN DISTINCT
    x.id AS entity1,
    entity1Type.type AS entity1Type, 
    y.id AS relationId,
    value.type AS relationshipType, 
    z.id AS entity2,
    entity2Type.type AS entity2Type
    """)
    List<ExportRelationshipResult> findExportCatalogItemsRelationships();
}
