MATCH(t:Tag) WHERE t.name="Referenzdokument" MATCH(x:XtdExternalDocument) CREATE (x)-[:TAGGED]->(t) RETURN x;
MATCH(t:Tag) WHERE t.name="Merkmal" MATCH(x:XtdProperty) CREATE (x)-[:TAGGED]->(t) RETURN x;
MATCH(t:Tag) WHERE t.name="Werteliste" MATCH(x:XtdXtdValueList) CREATE (x)-[:TAGGED]->(t) RETURN x;
MATCH(t:Tag) WHERE t.name="Maßeinheit" MATCH(x:XtdUnit) CREATE (x)-[:TAGGED]->(t) RETURN x;
MATCH(t:Tag) WHERE t.name="Wert" MATCH(x:XtdValue) CREATE (x)-[:TAGGED]->(t) RETURN x;