MATCH (x:XtdValueList)-[:ASSIGNS_VALUE]->(a:XtdRelAssignsValues)-[:ASSIGNS_VALUE]->(y:XtdValue) 
WITH x, y 
ORDER BY x, y.name
WITH x, collect(y) AS values
UNWIND range(1, size(values)) AS idx
WITH x, values[idx - 1] AS y, idx
MERGE (s:XtdOrderedValue:XtdObject:XtdRoot:CatalogRecord:Entity {id: randomUUID(), created: toString(datetime()), createdBy: "SYSTEM", lastModified: toString(datetime()), lastModifiedBy: "SYSTEM",majorVersion: 1, minorVersion: 0, status: "XTD_ACTIVE"}) 
SET s.order = idx
MERGE (x)-[:VALUES]->(s)
MERGE (s)-[:ORDERED_VALUE]->(y);
MATCH (x:XtdValueList)-[:ASSIGNS_VALUE]->(a:XtdRelAssignsValues)-[:ASSIGNS_VALUE]->(y:XtdValue)
DETACH DELETE a;