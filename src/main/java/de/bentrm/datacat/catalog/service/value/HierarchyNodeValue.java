package de.bentrm.datacat.catalog.service.value;

import de.bentrm.datacat.catalog.domain.Translation;
import de.bentrm.datacat.service.dto.TagDto;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
@Builder
public class HierarchyNodeValue {
    String entityType;
    String id;
    Instant created;
    String createdBy;
    Instant lastModified;
    String lastModifiedBy;
    String versionId;
    String versionDate;
    List<TagDto> tags;
    String name;
    List<Translation> names;
    String description;
    List<Translation> descriptions;
}
