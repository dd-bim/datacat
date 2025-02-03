MATCH (x:XtdRoot) WHERE EXISTS(x.version) REMOVE x.version RETURN x;
MATCH (x:XtdRoot) WHERE EXISTS(x.versionId) REMOVE x.versionId RETURN x;
MATCH (x:XtdRoot) WHERE EXISTS(x.versionDate) REMOVE x.versionDate RETURN x;