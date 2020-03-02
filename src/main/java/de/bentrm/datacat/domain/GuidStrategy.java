package de.bentrm.datacat.domain;

import org.neo4j.ogm.id.IdStrategy;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

public class GuidStrategy implements IdStrategy {
    @Override
    public String generateId(Object entity) {
        UUID uuid = UUID.randomUUID();
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return Base64.getUrlEncoder()
                .encodeToString(bb.array())
                .substring(0, 22)
                .replace('_', '$');
    }
}
