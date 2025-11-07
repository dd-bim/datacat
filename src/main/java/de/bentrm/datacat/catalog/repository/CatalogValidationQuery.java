package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatalogValidationQuery extends EntityRepository<XtdRoot> {

        /* ---- Prüfen von Vollständigkeit ---- */

        /* Finde Thema ohne Klasse */
        @Query("""
                        MATCH (x:XtdSubject)-[:TAGGED]->(y:Tag {name: "Thema"})
                        MATCH (z:Tag {name: "Klasse"})
                        WHERE NOT (x)-[:CONNECTED_SUBJECTS]->()-[:TARGET_SUBJECTS]->()-[:TAGGED]->(z)
                        ORDER BY x.`labels.de`
                        RETURN DISTINCT  x.id AS paths
                        """)
        List<String> findThemeWithoutSubject();

        /* Finde Klasse ohne Merkmale/Merkmalsgruppe */
        @Query("""
                        MATCH (x:XtdSubject)-[:TAGGED]->(y:Tag {name: "Klasse"})
                        MATCH (z:Tag {name: "Merkmalsgruppe"})
                        WHERE NOT (x)-[:CONNECTED_SUBJECTS]->()-[:TARGET_SUBJECTS]->()-[:TAGGED]->(z) AND NOT (x)-[:PROPERTIES]->()
                        ORDER BY x.`labels.de`
                        RETURN DISTINCT x.id AS paths
                        """)
        List<String> findSubjectWithoutProp();

        /* Finde Merkmalsgruppen ohne Merkmal */
        @Query("""
                        MATCH (x:XtdSubject)-[:TAGGED]->(y:Tag {name: "Merkmalsgruppe"})
                        WHERE NOT (x)-[:PROPERTIES]->()
                        ORDER BY x.`labels.de`
                        RETURN DISTINCT x.id AS paths
                        """)
        List<String> findPropGroupWithoutProp();

        /*
         * Finde Merkmale die weder einer Klasse noch einer Merkmalsgruppe zugeordnet
         * sind
         */
        @Query("""
                        MATCH (x:XtdProperty)
                        MATCH (z:Tag {name: "Merkmalsgruppe"})
                        WHERE NOT (x)<-[:TARGET_SUBJECTS]-()<-[:CONNECTED_SUBJECTS]-()-[:TAGGED]->(z) AND NOT ()-[:PROPERTIES]->(x)
                        ORDER BY x.`labels.de`
                        RETURN DISTINCT x.id AS paths
                        """)
        List<String> findPropWithoutSubjectOrPropGroup();

        /* Finde Wertelisten die keinem Merkmal zugeordnet sind */
        @Query("""
                        MATCH (x:XtdValueList)
                        WHERE NOT (x)<-[:POSSIBLE_VALUES]-()
                        ORDER BY x.`labels.de`
                        RETURN DISTINCT x.id AS paths
                        """)
        List<String> findValueListWithoutProp();

        /*
         * Finde Maßeinheiten die keiner Werteliste und keinem Merkmal zugeordnet sind
         */
        @Query("""
                        MATCH (x:XtdUnit)
                        WHERE NOT (x)<-[:UNIT|UNITS]-()
                        ORDER BY x.`labels.de`
                        RETURN DISTINCT x.id AS paths
                        """)
        List<String> findUnitWithoutValueList();

        /* Finde Werte die keiner Werteliste zugeordnet sind */
        @Query("""
                        MATCH (x:XtdValue)
                        WHERE NOT (x)<-[:ORDERED_VALUE]-()<-[:VALUES]-()
                        ORDER BY x.`labels.de`
                        RETURN DISTINCT x.id AS paths
                        """)
        List<String> findValueWithoutValueList();

        /* Finde Elemente ohne Wörterbuch */
        @Query("""
                        MATCH(x:XtdObject)
                        WHERE NOT (x)-[:DICTIONARY]->()
                        AND ANY(label IN labels(x) WHERE label IN ["XtdSubject", "XtdProperty", "XtdValueList", "XtdValue", "XtdExternalDocument"])
                        ORDER BY x.`labels.de`
                        RETURN x.id as paths
                            """)
        List<String> findMissingDictionary();

        /* Finde Elemente ohne Referenzdokument */
        @Query("""
                        MATCH(x:XtdConcept) 
                        WHERE NOT (x)-[:REFERENCE_DOCUMENTS]->() 
                        AND ANY(label IN labels(x) WHERE label IN ["XtdSubject", "XtdProperty", "XtdValueList", "XtdValue"]) 
                        ORDER BY x.`labels.de` 
                        RETURN x.id as paths
                                """)
        List<String> findMissingReferenceDocument();

        /* Finde inaktive Konzepte */
        @Query("""
                        MATCH(x:XtdObject) 
                        WHERE x.status="XTD_INACTIVE" 
                        AND ANY(label IN labels(x) WHERE label IN ["XtdSubject", "XtdProperty", "XtdValueList", "XtdValue", "XtdExternalDocument", "XtdUnit"]) 
                        ORDER BY x.`labels.de` 
                        RETURN x.id as paths
                                """)
        List<String> findInactiveConcepts();
        
        /* ---- Prüfen auf Inkonsistenz ---- */

        /* Finde Elemente ohne Tag */
        @Query("""
                        MATCH (x)
                        WHERE ANY(label IN labels(x) WHERE label IN ["XtdSubject", "XtdProperty", "XtdUnit", "XtdValue"])
                        AND NOT (x)-[:TAGGED]-()
                        ORDER BY x.`labels.de`
                        RETURN x.id AS paths
                        """)
        List<String> findMissingTags();

        /* ---- Prüfen auf Eindeutigkeit ---- */

        /* Finde Elemente mit identischer GUID */
        @Query("""
                        MATCH (x)
                        WITH x.id AS paths, count(*) AS IDCount
                        WHERE IDCount > 1
                        RETURN paths
                        """)
        List<String> findMultipleIDs();

        /* Finde Elemente mit identischer Bezeichnung innerhalb eines Types */
        @Query("""
                MATCH (x)-[:TAGGED]->(tag:Tag)
                WHERE x.`labels.de` IS NOT NULL
                WITH tag, x.`labels.de` AS labelDe, collect(x.id) AS ids
                WHERE size(ids) > 1
                UNWIND ids AS paths
                RETURN paths
                ORDER BY labelDe ASC
                UNION
                MATCH (x)-[:TAGGED]->(tag:Tag)
                WHERE x.`labels.en` IS NOT NULL
                WITH tag, x.`labels.en` AS labelEn, collect(x.id) AS ids
                WHERE size(ids) > 1
                UNWIND ids AS paths
                RETURN DISTINCT paths;
                """)
        List<String> findMultipleNames();

        /* Finde Elemente mit identischer Bezeichnung im gesamten Datenbestand */
        @Query("""
                MATCH (x)-[:TAGGED]->()
                WHERE x.`labels.de` IS NOT NULL
                WITH x.`labels.de` AS labelDe, collect(DISTINCT x) AS nodes
                WHERE size(nodes) > 1
                UNWIND nodes AS node
                RETURN DISTINCT node.id AS paths
                UNION
                MATCH (x)-[:TAGGED]->()
                WHERE x.`labels.en` IS NOT NULL
                WITH x.`labels.en` AS labelEn, collect(DISTINCT x) AS nodes
                WHERE size(nodes) > 1
                UNWIND nodes AS node
                RETURN DISTINCT node.id AS paths
                """)
        List<String> findMultipleNamesAcrossClasses();

        /* ---- Prüfen auf Verständlichkeit ---- */

        /* Finde Elemente ohne englische Namens-Übersetzung */
        @Query("""
                        MATCH (x:XtdObject)
                        WHERE x.`labels.de` IS NOT NULL
                        AND x.`labels.en` IS NULL
                        ORDER BY x.`labels.de`
                        RETURN DISTINCT x.id AS paths
                        """)
        List<String> findMissingEnglishName();

        /* Finde Elemente ohne Beschreibung */
        @Query("""
                        MATCH (x)
                        WHERE ANY(l IN LABELS(x) WHERE l IN ["XtdSubject", "XtdProperty", "XtdUnit", "XtdValueList", "XtdExternalDocument"])
                        AND NOT (x)-[:DESCRIPTIONS]->()
                        ORDER BY x.`labels.de`
                        RETURN x.id AS paths
                        """)
        List<String> findMissingDescription();

        /* Finde Elemente ohne englische Beschreibung */
        @Query("""
                        MATCH (x:XtdConcept)-[:DESCRIPTIONS]->()-[:TEXTS]->()-[:LANGUAGE]->(z )
                        WITH x.id AS paths, collect(z.code) AS sum, x.`labels.de` AS name
                        WHERE ("de" IN(sum) OR "de-DE" IN(sum) OR "de-CH" IN(sum) OR "de-AT" IN(sum))
                        AND NOT "en" IN(sum)
                        ORDER BY name
                        RETURN paths
                        """)
        List<String> findMissingEnglishDescription();
}
