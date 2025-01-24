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
    List<List<String>> findModelWithoutGroup();

    /* Finde Gruppe ohne Klasse */
    @Query("""
    MATCH (x:XtdSubject)-[:TAGGED]->(y:Tag {name: "Gruppe"})
    MATCH (z:Tag {name: "Klasse"})
    WHERE NOT (x)-[:CONNECTED_SUBJECTS]->()-[:TARGET_SUBJECTS]->()-[:TAGGED]->(z)
    RETURN DISTINCT  x.id AS paths
    """)
    List<List<String>> findGroupWithoutSubject();

    /* Finde Klasse ohne Merkmale/Merkmalsgruppe */
    @Query("""
    MATCH (x:XtdSubject)-[:TAGGED]->(y:Tag {name: "Klasse"})
    MATCH (z:Tag {name: "Merkmalsgruppe"})
    WHERE NOT (x)-[:CONNECTED_SUBJECTS]->()-[:TARGET_SUBJECTS]->()-[:TAGGED]->(z) AND NOT (x)-[:PROPERTIES]->()
    RETURN DISTINCT x.id AS paths
    """)
    List<List<String>> findSubjectWithoutProp();

    /* Finde Merkmalsgruppen ohne Merkmal */
    @Query("""
    MATCH (x:XtdSubject)-[:TAGGED]->(y:Tag {name: "Merkmalsgruppe"})
    WHERE NOT (x)-[:PROPERTIES]->()
    RETURN DISTINCT x.id AS paths
    """)
    List<List<String>> findPropGroupWithoutProp();

    /* Finde Merkmale die weder einer Klasse noch einer Merkmalsgruppe zugeordnet sind */
    @Query("""
    MATCH (x:XtdProperty)
    MATCH (z:Tag {name: "Merkmalsgruppe"})
    WHERE NOT (x)<-[:TARGET_SUBJECTS]-()<-[:CONNECTED_SUBJECTS]-()-[:TAGGED]->(z) AND NOT ()-[:PROPERTIES]->(x)
    RETURN DISTINCT x.id AS paths
    """)
    List<List<String>> findPropWithoutSubjectOrPropGroup();

    /* Finde Wertelisten die keinem Merkmal zugeordnet sind */
    @Query("""
    MATCH (x:XtdValueList)
    WHERE NOT (x)<-[:POSSIBLE_VALUES]-()
    RETURN DISTINCT x.id AS paths
    """)
    List<List<String>> findMeasureWithoutProp();

    /* Finde Maßeinheiten die keiner Werteliste zugeordnet sind */
    @Query("""
    MATCH (x:XtdUnit)
    WHERE NOT (x)<-[:UNIT]-()
    RETURN DISTINCT x.id AS paths
    """)
    List<List<String>> findUnitWithoutMeasure();

    // TODO
    /* Finde Werte die keiner Werteliste zugeordnet sind */
    @Query("""
    MATCH (x:XtdValue)
    WITH x.id AS paths
    WHERE NOT (x)-[:ASSIGNS_VALUE]-()
    RETURN paths
    """)
    List<List<String>> findValueWithoutMeasure();

    /* ---- Prüfen auf Inkonsistenz ---- */

    /* Finde Elemente ohne Tag */
    /* Nicht weiter beschrieben, da Tags nur bei xtdBag und xtdSubject eine Rolle spielen und momentan nicht durch den datacat editor eingesehen werden können */
    @Query("""
    MATCH (x)
    WITH x.id AS paths
    AND NOT (x)-[:TAGGED]-() 
    RETURN paths
    """)
    List<List<String>> findMissingTags();

    /* ---- Prüfen auf Eindeutigkeit ---- */

    /* Finde Elemente mit identischer GUID */
    @Query("""
    MATCH (x) 
    WITH x.id AS paths, count(*) AS IDCount 
    WHERE IDCount > 1
    RETURN paths
    """)
    List<List<String>> findMultipleIDs();

    /* Finde Elemente mit identischer Bezeichnung innerhalb eines Types*/
    @Query("""
    MATCH (x)-[:NAMED]->(v)
    MATCH (y)-[:NAMED]->(z)
    WHERE v.label = z.label
    AND v.languageCode = z.languageCode
    AND (x)-[:TAGGED]->()<-[:TAGGED]-(y) 
    WITH x.id AS paths ORDER BY v.label ASC
    RETURN paths;
    """)
    List<List<String>> findMultipleNames();

    /* Finde Elemente mit identischer Bezeichnung im gesamten Datenbestand */
    // @Query("""
    // MATCH (x:XtdRoot)-[:NAMED]->(v:Translation)
    // MATCH (y:XtdRoot)-[:NAMED]->(z:Translation)
    // WHERE v.label = z.label
    // AND x.id <> y.id
    // AND v.languageCode = z.languageCode
    // WITH x.id AS paths
    // RETURN paths
    // """)
    @Query("""
        MATCH (x:XtdRoot)-[:NAMED]->(v:Translation)
        MATCH (y:XtdRoot)-[:NAMED]->(z:Translation)
        WHERE v.label = z.label
        AND x.id <> y.id
        AND v.languageCode= z.languageCode
        WITH x.id AS paths ORDER BY v.label ASC
        RETURN paths
        """)
    List<List<String>> findMultipleNamesAcrossClasses();

    /* ---- Prüfen auf Verständlichkeit ---- */

    /* Finde Elemente ohne englische Namens-Übersetzung */ 
    /* bei großen Datensätzen führt Filterung nach allen Xtd zu Absturz, daher folgendes ergänzen: AND x:XtdSubject */
    // @Query("""
    // MATCH (t_de:Translation) 
    // WHERE t_de.languageCode='de'
    // MATCH (t_en:Translation) 
    // WHERE t_en.languageCode='en'
    // MATCH (x:XtdRoot)
    // WHERE EXISTS(x.`labels.de`)
    // AND (x)-[:NAMED]->(t_de)
    // AND NOT EXISTS(x.`labels.en`)
    // AND NOT (x)-[:NAMED]->(t_en)
    // WITH x.id AS paths
    // RETURN DISTINCT paths
    // """)

    // deutlich effizienter und schließt andere Deutsch-Labels de-DE, de-AT und de-CH mit ein
    @Query("""
    MATCH (x:XtdRoot)-[:NAMED]->(t:Translation) 
    WITH x.id AS paths, collect(t.languageCode) AS sum
    WHERE ("de" IN(sum) OR "de-DE" IN(sum) OR "de-CH" IN(sum) OR "de-AT" IN(sum))
    AND NOT "en" IN(sum)
    RETURN DISTINCT paths
    """)
    List<List<String>> findMissingEnglishName();
    
    /* Finde Elemente ohne Beschreibung */
    /* bei großen Datensätzen führt Filterung nach allen Xtd zu Absturz, daher folgendes ergänzen: AND x:XtdSubject */
    @Query("""
    MATCH (x:XtdRoot)
    WHERE NOT (x)-[:DESCRIPTIONS]->()
    WITH x.id AS paths
    RETURN paths
    """)
    List<List<String>> findMissingDescription();

    /* Finde Elemente ohne englische Beschreibung */
    /* bei großen Datensätzen führt Filterung nach allen Xtd zu Absturz, daher folgendes ergänzen: AND x:XtdSubject */    
    // @Query("""
    // MATCH (t_de:Translation) 
    // WHERE t_de.languageCode='de'
    // MATCH (t_en:Translation) 
    // WHERE t_en.languageCode='en'
    // MATCH (x:XtdRoot)
    // WHERE (x)-[:DESCRIPTIONS]->(t_de)
    // AND NOT (x)-[:DESCRIPTIONS]->(t_en)
    // WITH x.id AS paths
    // RETURN DISTINCT paths
    // """)

    // deutlich effizienter und Ergebnis richtiger
    @Query("""
    MATCH (x:XtdRoot)-[:DESCRIPTIONS]->(t:Translation) 
    WITH x.id AS paths, collect(t.languageCode) AS sum
    WHERE ("de" IN(sum) OR "de-DE" IN(sum) OR "de-CH" IN(sum) OR "de-AT" IN(sum))
    AND NOT "en" IN(sum)
    RETURN paths
    """)
    List<List<String>> findMissingEnglishDescription();
}
