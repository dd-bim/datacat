MATCH (x:XtdRoot) WHERE x.version IS NOT NULL REMOVE x.version RETURN x;
MATCH (x:XtdRoot) WHERE x.versionId IS NOT NULL REMOVE x.versionId RETURN x;
MATCH (x:XtdRoot) WHERE x.versionDate IS NOT NULL REMOVE x.versionDate RETURN x;