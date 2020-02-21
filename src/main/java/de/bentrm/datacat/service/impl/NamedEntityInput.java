package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdDescription;
import de.bentrm.datacat.domain.XtdName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.*;

public class NamedEntityInput {

    private String label;
    private String uniqueId;
    private String versionId;
    private String versionDate;
    private List<XtdName> names = new ArrayList<>();
    private SortedSet<XtdDescription> descriptions = new TreeSet<>();

    public NamedEntityInput(Map<String, Object> input) {

        label = (String) input.get("label");

        uniqueId = (String) input.getOrDefault("uniqueId", null);
        if (uniqueId != null) {
            uniqueId = uniqueId.isBlank() ? null : uniqueId.trim();
        }

        versionId = (String) input.get("versionId");
        versionDate = (String) input.get("versionDate");

        // TODO: move into service layer
        if (!input.containsKey("names")) {
            throw new IllegalArgumentException("Entity may not be unnamed.");
        }
        List<Map<String, String>> namesInput = (List<Map<String, String>>) input.get("names");
        for (int i = 0; i < namesInput.size(); i++) {
            var item = namesInput.get(i);
            XtdName newName = new XtdName();
            newName.setUniqueId(item.get("uniqueId"));
            newName.setName(item.get("value"));
//            newName.setLanguage(item.get("language"));
            newName.setSortOrder(i);
            names.add(newName);
        }

        if (input.containsKey("descriptions")) {
            List<Map<String, String>> descriptionsInput = (List<Map<String, String>>) input.get("descriptions");
            for (int i = 0; i < descriptionsInput.size(); i++) {
                var item = descriptionsInput.get(i);
                XtdDescription newDescription = new XtdDescription();
                newDescription.setUniqueId(item.get("uniqueId"));
                newDescription.setDescription(item.get("value"));
//                newDescription.setLanguage(item.get("language"));
                newDescription.setSortOrder(i);
                descriptions.add(newDescription);
            }
        }
    }

    public String getLabel() {
        return label;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public String getVersionId() {
        return versionId;
    }

    public String getVersionDate() {
        return versionDate;
    }

    public List<XtdName> getNames() {
        return names;
    }

    public SortedSet<XtdDescription> getDescriptions() {
        return descriptions;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("label", label)
                .append("uniqueId", uniqueId)
                .append("versionId", versionId)
                .append("versionDate", versionDate)
                .append("names", names)
                .append("descriptions", descriptions)
                .toString();
    }
}
