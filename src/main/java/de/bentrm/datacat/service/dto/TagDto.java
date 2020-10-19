package de.bentrm.datacat.service.dto;

import de.bentrm.datacat.graphql.dto.LocalizedTextDto;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class TagDto {

    private String id;
    private Instant created;
    private String createdBy;
    private Instant lastModified;
    private String lastModifiedBy;
    private String scope;
    private String localizedName;
    private List<LocalizedTextDto> names;
    private String localizedDescription;
    private List<LocalizedTextDto> descriptions;

}
