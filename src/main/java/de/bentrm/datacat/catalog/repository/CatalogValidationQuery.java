package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatalogValidationQuery extends EntityRepository<XtdRoot> {

    /* ---- Prüfen von Vollständigkeit ---- */

    /* Finde Fachmodell ohne Gruppe */
    @Query("""
            MATCH (x:XtdSubject)-[:TAGGED]->(y:Tag {name: "Fachmodell"})
            MATCH (z:Tag {name: "Gruppe"})
            WHERE NOT (x)-[:CONNECTED_SUBJECTS]->()-[:TARGET_SUBJECTS]->()-[:TAGGED]->(z)
            RETURN DISTINCT  x.id AS paths
            """)
    List<String> findModelWithoutGroup();

    /* Finde Gruppe ohne Klasse */
    @Query("""
            MATCH (x:XtdSubject)-[:TAGGED]->(y:Tag {name: "Gruppe"})
            MATCH (z:Tag {name: "Klasse"})
            WHERE NOT (x)-[:CONNECTED_SUBJECTS]->()-[:TARGET_SUBJECTS]->()-[:TAGGED]->(z)
            RETURN DISTINCT  x.id AS paths
            """)
    List<String> findGroupWithoutSubject();

    /* Finde Klasse ohne Merkmale/Merkmalsgruppe */
    @Query("""
            MATCH (x:XtdSubject)-[:TAGGED]->(y:Tag {name: "Klasse"})
            MATCH (z:Tag {name: "Merkmalsgruppe"})
            WHERE NOT (x)-[:CONNECTED_SUBJECTS]->()-[:TARGET_SUBJECTS]->()-[:TAGGED]->(z) AND NOT (x)-[:PROPERTIES]->()
            RETURN DISTINCT x.id AS paths
            """)
    List<String> findSubjectWithoutProp();

    /* Finde Merkmalsgruppen ohne Merkmal */
    @Query("""
            MATCH (x:XtdSubject)-[:TAGGED]->(y:Tag {name: "Merkmalsgruppe"})
            WHERE NOT (x)-[:PROPERTIES]->()
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
            RETURN DISTINCT x.id AS paths
            """)
    List<String> findPropWithoutSubjectOrPropGroup();

    /* Finde Wertelisten die keinem Merkmal zugeordnet sind */
    @Query("""
            MATCH (x:XtdValueList)
            WHERE NOT (x)<-[:POSSIBLE_VALUES]-()
            RETURN DISTINCT x.id AS paths
            """)
    List<String> findValueListWithoutProp();

    /* Finde Maßeinheiten die keiner Werteliste zugeordnet sind */
    @Query("""
            MATCH (x:XtdUnit)
            WHERE NOT (x)<-[:UNIT]-()
            RETURN DISTINCT x.id AS paths
            """)
    List<String> findUnitWithoutValueList();

    /* Finde Werte die keiner Werteliste zugeordnet sind */
    @Query("""
            MATCH (x:XtdValue)
            WHERE NOT (x)<-[:ORDERED_VALUE]-()<-[:VALUES]-()
            RETURN DISTINCT x.id AS paths
            """)
    List<String> findValueWithoutValueList();

    /* ---- Prüfen auf Inkonsistenz ---- */

    /* Finde Elemente ohne Tag */
    @Query("""
            MATCH (x)
            WHERE ANY(label IN labels(x) WHERE label IN ["XtdSubject", "XtdProperty", "XtdUnit", "XtdValue"])
            AND NOT (x)-[:TAGGED]-()
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
            MATCH (x)-[:TAGGED]->()<-[:TAGGED]-(y)
            WHERE x.`labels.de` = y.`labels.de`
            OR x.`labels.en` = y.`labels.en`
            WITH x.id AS paths ORDER BY x.`labels.de` ASC
            RETURN paths;
            """)
    List<String> findMultipleNames();

    /* Finde Elemente mit identischer Bezeichnung im gesamten Datenbestand */
    @Query("""
            MATCH (x)-[:TAGGED]->(),()<-[:TAGGED]-(y)
            WHERE (x.`labels.de` = y.`labels.de`
            OR x.`labels.en` = y.`labels.en`) AND x.id <> y.id
            WITH x.id AS paths ORDER BY x.`labels.de` ASC
            RETURN paths;
                """)
    List<String> findMultipleNamesAcrossClasses();

    /* ---- Prüfen auf Verständlichkeit ---- */

    /* Finde Elemente ohne englische Namens-Übersetzung */
    @Query("""
            MATCH (x:XtdObject)
            WHERE x.`labels.de` IS NOT NULL
            AND x.`labels.en` IS NULL
            RETURN DISTINCT x.id AS paths
            """)
    List<String> findMissingEnglishName();

    /* Finde Elemente ohne Beschreibung */
    @Query("""
            MATCH (x)
            WHERE ANY(l IN LABELS(x) WHERE l IN ["XtdSubject", "XtdProperty", "XtdUnit", "XtdValueList", "XtdExternalDocument"])
            AND NOT (x)-[:DESCRIPTIONS]->()
            RETURN x.id AS paths
            """)
    List<String> findMissingDescription();

    /* Finde Elemente ohne englische Beschreibung */
    @Query("""
            MATCH (x:XtdConcept)-[:DESCRIPTIONS]->()-[:TEXTS]->()-[:LANGUAGE]->(z )
            WITH x.id AS paths, collect(z.code) AS sum
            WHERE ("de" IN(sum) OR "de-DE" IN(sum) OR "de-CH" IN(sum) OR "de-AT" IN(sum))
            AND NOT "en" IN(sum)
            RETURN paths
            """)
    List<String> findMissingEnglishDescription();
}
