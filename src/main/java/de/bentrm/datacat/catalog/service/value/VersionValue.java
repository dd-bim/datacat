package de.bentrm.datacat.catalog.service.value;

import lombok.Value;
import org.apache.commons.lang3.StringUtils;

@Value
public class VersionValue {
    String versionId;
    String versionDate;

    public String getVersionId() {
        return StringUtils.isNoneBlank(this.versionId) ? this.versionId : null;
    }

    public String getVersionDate() {
        return StringUtils.isNoneBlank(this.versionDate) ? this.versionDate : null;
    }
}
