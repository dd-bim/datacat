package de.bentrm.datacat.catalog.service.value;

import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Value
public class OneToOneRelationshipValue {
    String id;
    VersionValue version;
    List<@NotNull @Valid TranslationValue> names;
    List<@NotNull @Valid TranslationValue> descriptions;
    List<@NotNull @Valid TranslationValue> comments;
    @NotBlank String from;
    @NotBlank String to;

    public String getId() {
        return StringUtils.isNoneBlank(id) ? id : null;
    }
}
