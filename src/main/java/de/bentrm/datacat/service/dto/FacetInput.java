package de.bentrm.datacat.service.dto;

import de.bentrm.datacat.domain.EntityType;
import de.bentrm.datacat.graphql.dto.TextInput;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class FacetInput {

    private String id;

    @NotEmpty
    private List<@NotNull @Valid TextInput> names = new ArrayList<>();

    @NotNull
    private List<@NotNull @Valid TextInput> descriptions = new ArrayList<>();

    @NotEmpty
    private List<@NotNull EntityType> targets = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<TextInput> getNames() {
        return names;
    }

    public void setNames(List<TextInput> names) {
        this.names = names;
    }

    public List<TextInput> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<TextInput> descriptions) {
        this.descriptions = descriptions;
    }

    public List<EntityType> getTargets() {
        return targets;
    }

    public void setTargets(List<EntityType> targets) {
        this.targets = targets;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("names", names)
                .append("descriptions", descriptions)
                .append("targets", targets)
                .toString();
    }
}
