package de.bentrm.datacat.graphql.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ExternalDocumentInput {

    private String id;
    private @NotEmpty List<@Valid @NotNull TextInput> names = new ArrayList<>();

    public String getId() {
        return id != null ? id.trim() : null;
    }

    public boolean hasId() {
        return id != null && id.isBlank();
    }

    public List<TextInput> getNames() {
        return names;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("names", names)
                .toString();
    }
}
