MATCH (x:XtdSubject) - [r:COLLECTS] -> (y:XtdRelCollects) CREATE (x) - [:CONNECTED_SUBJECTS] -> (y) DELETE r  RETURN x, y;
MATCH (x:XtdSubject) - [:TAGGED] -> (y:Tag {name: "Merkmalsgruppe"}) WITH x MATCH (x) - [:CONNECTED_SUBJECTS] -> (z:XtdRelCollects) - [:COLLECTS] -> (a:XtdProperty) CREATE (x) - [:PROPERTIES] -> (a) RETURN x, z, a;
MATCH (x:XtdSubject) - [:TAGGED] -> (y:Tag {name: "Merkmalsgruppe"}) WITH x MATCH (x) - [:CONNECTED_SUBJECTS] -> (a:XtdRelCollects) DETACH DELETE a;
MATCH (n:XtdRelCollects) SET n:XtdRelationshipToSubject:XtdRoot:Relationship:XtdObject REMOVE n:XtdRelCollects RETURN n;
MATCH (x:XtdRelationshipToSubject) - [r:COLLECTS] -> (y:XtdSubject) CREATE (x) - [:TARGET_SUBJECTS] -> (y) DELETE r  RETURN x,y;