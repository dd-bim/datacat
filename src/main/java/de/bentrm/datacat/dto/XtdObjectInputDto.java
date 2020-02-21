package de.bentrm.datacat.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class XtdObjectInputDto implements XtdRootDto {

    private String uniqueId;
    private List<XtdNameInputDto> names = new ArrayList<>();
    private List<XtdDescriptionInputDto> descriptions = new ArrayList<>();

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public List<XtdNameInputDto> getNames() {
        return names;
    }

    public void setNames(List<XtdNameInputDto> names) {
        this.names = names;
    }

    public List<XtdDescriptionInputDto> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<XtdDescriptionInputDto> descriptions) {
        this.descriptions = descriptions;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("uniqueId", uniqueId)
                .append("names", names)
                .append("descriptions", descriptions)
                .toString();
    }

}
