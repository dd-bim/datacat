package de.bentrm.datacat.query;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface EntityQuery<T> {

    @NotNull Optional<T> execute();

}
