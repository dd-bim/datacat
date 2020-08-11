package de.bentrm.datacat.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
@NodeEntity(label = "Translation")
public class Translation extends Entity {

    @EqualsAndHashCode.Include
    @NotBlank
    private final String languageCode;

    @Index
    @NotBlank
    private String label;

    @EqualsAndHashCode.Include
    @NotEmpty
    private final List<@NotBlank String> values = new ArrayList<>();

    public Translation(@Nullable String id, @NotBlank String languageCode, @NotEmpty List<@NotBlank String> values) {
        this.id = id != null && !id.isBlank() ? id : null;
        this.languageCode = languageCode;
        this.setValues(values);
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getLabel() {
        return label;
    }

    public List<String> getValues() {
        return List.copyOf(values);
    }

    void setValues(List<String> values) {
        this.values.clear();
        this.values.addAll(values);
        this.label = String.join(", ", this.values);
    }
}
