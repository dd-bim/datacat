MATCH (n:CatalogItem) SET n :CatalogRecord REMOVE n :CatalogItem RETURN n
