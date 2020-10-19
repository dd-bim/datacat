package de.bentrm.datacat.util;

import org.neo4j.ogm.id.IdStrategy;

import java.util.UUID;

@Deprecated
public class IfcGuidStrategy implements IdStrategy {
    @Override
    public String generateId(Object entity) {
        UUID uuid = UUID.randomUUID();
        return IfcGuid.compress(uuid);
    }
}
