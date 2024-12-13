MERGE (:Tag:Entity { id: "992c8887-301e-4764-891c-ae954426fc22", version: 0, created: "2022-05-23T15:00:00.00000Z", createdBy: "Schilling", lastModified: "2022-05-23T15:00:00.00000Z", lastModifiedBy: "Schilling", scope: "ISO:23387:2020", `names.de-DE`: "Referenzdokument" })
MERGE (:Tag:Entity { id: "d4b0ba83-eb40-4997-85e0-9d6181e85639", version: 0, created: "2022-05-24T09:00:00.00000Z", createdBy: "Schilling", lastModified: "2022-05-24T09:00:00.00000Z", lastModifiedBy: "Schilling", scope: "ISO:23387:2020", `names.de-DE`: "Merkmal" })
MERGE (:Tag:Entity { id: "57172977-a42f-4e05-8109-cd906ec7f43c", version: 0, created: "2022-05-24T09:00:00.00000Z", createdBy: "Schilling", lastModified: "2022-05-24T09:00:00.00000Z", lastModifiedBy: "Schilling", scope: "ISO:23387:2020", `names.de-DE`: "Größe" })
MERGE (:Tag:Entity { id: "a5d13c88-7d83-42c1-8da2-5dc6d8e8a749", version: 0, created: "2022-05-24T09:00:00.00000Z", createdBy: "Schilling", lastModified: "2022-05-24T09:00:00.00000Z", lastModifiedBy: "Schilling", scope: "ISO:23387:2020", `names.de-DE`: "Wert" })
MERGE (:Tag:Entity { id: "09da1ebb-8641-47fa-b82e-8588c7fef09e", version: 0, created: "2022-05-24T09:00:00.00000Z", createdBy: "Schilling", lastModified: "2022-05-24T09:00:00.00000Z", lastModifiedBy: "Schilling", scope: "ISO:23387:2020", `names.de-DE`: "Maßeinheit" })
MATCH (t:Tag) SET t.name = t.`names.de-DE`
MATCH(t:Tag) WHERE t.name="Referenzdokument" MATCH(x:XtdExternalDocument) CREATE (x)-[:TAGGED]->(t) RETURN x
MATCH(t:Tag) WHERE t.name="Merkmal" MATCH(x:XtdProperty) CREATE (x)-[:TAGGED]->(t) RETURN x
MATCH(t:Tag) WHERE t.name="Größe" MATCH(x:XtdMeasureWithUnit) CREATE (x)-[:TAGGED]->(t) RETURN x
MATCH(t:Tag) WHERE t.name="Maßeinheit" MATCH(x:XtdUnit) CREATE (x)-[:TAGGED]->(t) RETURN x
MATCH(t:Tag) WHERE t.name="Wert" MATCH(x:XtdValue) CREATE (x)-[:TAGGED]->(t) RETURN x