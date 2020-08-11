package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.validation.IdConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DocumentsUpdateInput extends RootUpdateInput {

    @NotBlank @IdConstraint
    private String relatingDocument;

    private @NotNull
    final List<@NotBlank @IdConstraint String> relatedThings = new ArrayList<>();
}
