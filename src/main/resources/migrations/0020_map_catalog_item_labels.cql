MATCH (t:Translation)<-[:NAMED]-(i:CatalogItem) WITH t, i, "labels." + t.languageCode AS key CALL apoc.create.setProperty(i, key, t.label) YIELD node RETURN COUNT(*)
