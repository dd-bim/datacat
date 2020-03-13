package de.bentrm.datacat.util;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IfcGuidTest {

    @Test
    public void compressesUUIDs() {
        // test values generated according to IfcOpenShell algorithm
        Map<String, String> ids = Map.of(
                "ab26db2a-fd33-4b4e-95af-e41be2d9e4fa", "2h9jig$JDBJfMlv1lYsUJw",
                "05c218b2-5c1e-4d3b-be0a-16e26b1b321a", "05mXYoN1vDExuA5k9h6p8Q",
                "582d8665-723f-4cc5-b17c-829f2bfa9c3f", "1OBOPbSZzCnR5yWfyh_fm$",
                "7afd6e65-a7b1-4985-92c7-750c2d9c14d2", "1w$Mvbfx59XPB7TGmjd1JI",
                "bce6cf95-ef56-4f99-af9d-6afa1bf3531e", "2yvi_LxrPFcQ_TQleRyrCU",
                "a9a102f7-b661-4405-b841-c6a8c445338b", "2feGBtjc541RX1ngZ4HJEB"
        );

        ids.forEach((rfc, ifc) -> {
            UUID uuid = UUID.fromString(rfc);
            String compressed = IfcGuid.compress(uuid);
            UUID uncompressed = IfcGuid.decompress(compressed);

            assertEquals(ifc, compressed);
            assertEquals(uncompressed, uuid);
        });
    }

}
