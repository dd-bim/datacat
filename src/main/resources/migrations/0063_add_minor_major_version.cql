MATCH (n:XtdObject) WHERE n.majorVersion IS NULL SET n.majorVersion = 1;
MATCH (n:XtdObject) WHERE n.minorVersion IS NULL SET n.minorVersion = 0;
