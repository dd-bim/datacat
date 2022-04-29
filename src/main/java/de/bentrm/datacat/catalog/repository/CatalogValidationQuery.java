package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.catalog.domain.CatalogRecordType;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatalogValidationQuery extends EntityRepository<XtdRoot> {

    /* ---- Prüfen von Vollständigkeit ---- */

    /* Finde Fachmodell ohne Gruppe */
    @Query("""
    MATCH (y:Tag)
    WHERE y.name = "Fachmodell"
    MATCH (x:XtdBag)
    WITH x.id AS paths
    WHERE NOT (x)-[:COLLECTS]-()
    AND (x)-[:TAGGED]-(y)
    RETURN paths
    """)
    List<List<String>> findModelWithoutGroup();

    /* Finde Gruppe ohne Klasse */
    @Query("""
    MATCH (y:Tag)
    WHERE y.name = "Gruppe"
    MATCH (x:XtdBag)
    WITH x.id AS paths
    WHERE NOT (x)-[:COLLECTS]-()
    AND (x)-[:TAGGED]-(y)
    RETURN paths
    """)
    List<List<String>> findGroupWithoutSubject();

    /* Finde Klasse ohne Merkmale/Merkmalsgruppe */
    @Query("""
    MATCH (x:XtdSubject)
    WITH x.id AS paths
    WHERE NOT (x)-[:ASSIGNS_COLLECTIONS|:ASSIGNS_PROPERTY]-() 
    RETURN paths
    """)
    List<List<String>> findSubjectWithoutProp();

    /* Finde Merkmalsgruppen ohne Merkmal */
    @Query("""
    MATCH (x:XtdNest)
    WITH x.id AS paths
    WHERE NOT (x)-[:COLLECTS]-() 
    RETURN paths
    """)
    List<List<String>> findPropGroupWithoutProp();

    /* Finde Merkmale die weder einer Klasse noch einer Merkmalsgruppe zugeordnet sind */
    @Query("""
    MATCH (x:XtdProperty) 
    WITH x.id AS paths
    WHERE NOT (x)-[:COLLECTS|:ASSIGNS_PROPERTY]-()
    RETURN paths
    """)
    List<List<String>> findPropWithoutSubjectOrPropGroup();

    /* Finde Größen die keinem Merkmal zugeordnet sind */
    @Query("""
    MATCH (x:XtdMeasureWithUnit)
    WITH x.id AS paths
    WHERE NOT (x)-[:ASSIGNS_MEASURE]-() 
    RETURN paths
    """)
    List<List<String>> findMeasureWithoutProp();

    /* Finde Maßeinheiten die keiner Größe zugeordnet sind */
    @Query("""
    MATCH (x:XtdUnit)
    WITH x.id AS paths
    WHERE NOT (x)-[:ASSIGNS_UNIT]-() 
    RETURN paths
    """)
    List<List<String>> findUnitWithoutMeasure();

    /* Finde Werte die keiner Größe zugeordnet sind */
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

    /* Finde Elemente mit identischer Bezeichnung */
    // AND (x)-[:TAGGED]->()<-[:TAGGED]-(y) bei Fachmodell Gruppe Klasse Merkmalsgruppe Datenvorlage
    @Query("""
    MATCH (x)-[:NAMED]->(v)
    MATCH (y)-[:NAMED]->(z)
    WHERE v.label = z.label
    AND v.languageCode = z.languageCode
    AND (x)-[:TAGGED]->()<-[:TAGGED]-(y) 
    WITH x.id AS paths
    RETURN paths;
    """)
    List<List<String>> findMultipleNames();

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
    WHERE NOT (x)-[:DESCRIBED]->()
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
    // WHERE (x)-[:DESCRIBED]->(t_de)
    // AND NOT (x)-[:DESCRIBED]->(t_en)
    // WITH x.id AS paths
    // RETURN DISTINCT paths
    // """)

    // deutlich effizienter und Ergebnis richtiger
    @Query("""
    MATCH (x:XtdRoot)-[:DESCRIBED]->(t:Translation) 
    WITH x.id AS paths, collect(t.languageCode) AS sum
    WHERE ("de" IN(sum) OR "de-DE" IN(sum) OR "de-CH" IN(sum) OR "de-AT" IN(sum))
    AND NOT "en" IN(sum)
    RETURN paths
    """)
    List<List<String>> findMissingEnglishDescription();
}
