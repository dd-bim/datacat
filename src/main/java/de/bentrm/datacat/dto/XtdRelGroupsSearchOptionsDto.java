package de.bentrm.datacat.dto;

public class XtdRelGroupsSearchOptionsDto extends SearchOptionsDto {

    private String relatingObject;

    public String getRelatingObject() {
        return relatingObject;
    }

    public boolean hasRelatingObject() {
        return relatingObject != null && !relatingObject.isBlank();
    }
}
