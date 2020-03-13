package de.bentrm.datacat.domain;

import de.bentrm.datacat.util.IfcGuid;
import org.neo4j.ogm.id.IdStrategy;

import java.util.UUID;

public class IfcGuidStrategy implements IdStrategy {
    @Override
    public String generateId(Object entity) {
        UUID uuid = UUID.randomUUID();
        return IfcGuid.compress(uuid);
    }
}
