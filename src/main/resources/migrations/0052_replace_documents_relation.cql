MATCH (x:XtdExternalDocument) - [:DOCUMENTS] -> (y:XtdRelDocuments) - [:DOCUMENTS] -> (z:XtdBag) CREATE (z) - [:REFERENCE_DOCUMENTS] -> (x) RETURN x,y,z
MATCH (a:XtdRelDocuments) - [:NAMED] -> (b) DETACH DELETE a,b
MATCH (n:XtdExternalDocument) SET n:XtdRoot:XtdObject:XtdConcept RETURN n 