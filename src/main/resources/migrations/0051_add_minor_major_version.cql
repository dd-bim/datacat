MATCH (n:CatalogRecord) WHERE NOT EXISTS(n.majorVersion) SET n.majorVersion = 1;
MATCH (n:CatalogRecord) WHERE NOT EXISTS(n.minorVersion) SET n.minorVersion = 0;
