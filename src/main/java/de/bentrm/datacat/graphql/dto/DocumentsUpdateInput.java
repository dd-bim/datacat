package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.validation.IdConstraint;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class DocumentsUpdateInput extends RootUpdateInput {

    @NotBlank @IdConstraint
    private String relatingDocument;

    private @NotNull
    final List<@NotBlank @IdConstraint String> relatedThings = new ArrayList<>();

    public String getRelatingDocument() {
        return relatingDocument;
    }

    public List<String> getRelatedThings() {
        return relatedThings;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("relatingDocument", relatingDocument)
                .append("relatedThings", relatedThings)
                .toString();
    }
}
