package de.bentrm.datacat.graphql.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class TagFilterInput {

    List<@NotNull String> in;
}
