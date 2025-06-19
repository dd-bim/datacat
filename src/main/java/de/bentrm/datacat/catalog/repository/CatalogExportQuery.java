package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.domain.ExportItemResult;
import de.bentrm.datacat.catalog.domain.ExportRelationshipResult;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatalogExportQuery extends EntityRepository<XtdRoot> {
    
    // gives important catalog records with some properties
    // could be extended 
    @Query("""
    MATCH (x:XtdObject|XtdDictionary)
    OPTIONAL MATCH (x)-[:NAMES]->()-[:TEXTS]->(t)-[:LANGUAGE]->(l {code: "de"}) 
    OPTIONAL MATCH (x)-[:NAMES]->()-[:TEXTS]->(ten)-[:LANGUAGE]->(len {code: "en"}) 
    OPTIONAL MATCH (x)-[:DESCRIPTIONS]->()-[:TEXTS]->(d)-[:LANGUAGE]->(l {code: "de"}) 
    OPTIONAL MATCH (x)-[:DESCRIPTIONS]->()-[:TEXTS]->(den)-[:LANGUAGE]->(len {code: "en"}) 
    OPTIONAL MATCH (x)-[:TAGGED]->(tag)
    OPTIONAL MATCH (x)-[:DEFINITION]->()-[:TEXTS]->(def)-[:LANGUAGE]->(l {code: "de"})
    OPTIONAL MATCH (x)-[:DEFINITION]->()-[:TEXTS]->(defen)-[:LANGUAGE]->(len {code: "en"})
    OPTIONAL MATCH (x)-[:DEPRECATION_EXPLANATION]->()-[:TEXTS]->(depex)-[:LANGUAGE]->(l {code: "de"})
    OPTIONAL MATCH (x)-[:EXAMPLES]->()-[:TEXTS]->(ex)-[:LANGUAGE]->(l {code: "de"})
    OPTIONAL MATCH (x)-[:LANGUAGE_OF_CREATOR]->(loc)
    OPTIONAL MATCH (x)-[:COUNTRY_OF_ORIGIN]->(coo)
    OPTIONAL MATCH (x)-[:LANGUAGE|LANGUAGES]->(langs)
    WITH x, t, ten, d, den, def, defen, depex, ex, loc, coo, collect(tag.name) AS tags, collect(langs.code) AS langs,
        CASE
            WHEN "XtdExternalDocument" IN LABELS(x) THEN "XtdExternalDocument"
            WHEN "XtdValueList" IN LABELS(x) THEN "XtdValueList"
            WHEN "XtdProperty" IN LABELS(x) THEN "XtdProperty"
            WHEN "XtdUnit" IN LABELS(x) THEN "XtdUnit"
            WHEN "XtdValue" IN LABELS(x) THEN "XtdValue"
            WHEN "XtdSubject" IN LABELS(x) THEN "XtdSubject"
            WHEN "XtdDictionary" IN LABELS(x) THEN "XtdDictionary"
            ELSE null
        END AS type
    WHERE type IS NOT NULL
    RETURN DISTINCT 
        x.id AS id,  
        type, 
        tags AS tags,
        t.text AS name,
        ten.text AS name_en,
        d.text AS description,
        den.text AS description_en,
        def.text AS definition,
        defen.text AS definition_en,
        ex.text AS examples,
        loc.code AS languageOfCreator,
        coo.code AS countryOfOrigin,
        langs AS languages,
        x.createdBy AS createdBy,
        x.created AS created,
        x.lastModified AS lastModified,
        x.lastModifiedBy AS lastModifiedBy,
        x.majorVersion as majorVersion, x.minorVersion AS minorVersion, x.status AS status,
        depex.text AS deprecationExplanation,
        x.dataType AS dataType, x.dataFormat AS dataFormat, 
        x.scale AS scale, x.base AS base, 
        x.uri AS uri, x.author AS author, x.isbn AS isbn, x.publisher AS publisher, x.dateOfPublication AS dateOfPublication
    """)
    List<ExportItemResult> findExportCatalogRecords();


    // gives relationships between catalog records
    // could be extended
    @Query("""
    MATCH (x:XtdObject)-[y]->(z:XtdObject|XtdDictionary)
    WHERE NOT type(y) IN ["SCOPE_SUBJECTS", "CONNECTED_SUBJECTS", "CONNECTED_PROPERTIES", "TARGET_SUBJECTS", "TARGET_PROPERTIES", "RELATIONSHIP_TYPE", "ORDERED_VALUE", "DIMENSION"]
    RETURN DISTINCT 
    x.id AS entity1, 
    type(y) AS relationship, 
    z.id AS entity2
    UNION
    MATCH (x:XtdConcept)-[:CONNECTED_SUBJECTS|CONNECTED_PROPERTIES]->()-[:TARGET_SUBJECTS|TARGET_PROPERTIES]->(z:XtdConcept)
    RETURN DISTINCT 
    x.id AS entity1, 
    "GROUPS" AS relationship, 
    z.id AS entity2
    UNION
    MATCH (x:XtdValueList)-[y:VALUES]->()-[:ORDERED_VALUE]->(z:XtdValue)
    RETURN DISTINCT 
    x.id AS entity1, 
    type(y) AS relationship, 
    z.id AS entity2
    """)
    List<ExportRelationshipResult> findExportCatalogRecordsRelationships();
}
