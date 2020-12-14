package de.bentrm.datacat.catalog.service.value;

import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Value
public class RelationshipProperties {
    String id;
    VersionValue version;
    List<@NotNull @Valid TranslationValue> names;
    List<@NotNull @Valid TranslationValue> descriptions;

    public String getId() {
        return StringUtils.isNoneBlank(this.id) ? this.id : null;
    }
}
