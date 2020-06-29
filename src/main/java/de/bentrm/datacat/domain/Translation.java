package de.bentrm.datacat.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.neo4j.ogm.annotation.NodeEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@NodeEntity(label = "Translation")
public class Translation extends Entity {

    @NotBlank
    private String languageCode;

    @NotBlank
    private String label;

    @NotEmpty
    private final List<@NotBlank String> values = new ArrayList<>();

    public Translation() {
    }

    public Translation(@NotBlank String languageCode, @NotEmpty List<@NotBlank String> values) {
        this.setLanguageCode(languageCode);
        this.setValues(values);
    }

    public Translation(@NotBlank String id, @NotBlank String languageCode, @NotEmpty List<@NotBlank String> values) {
        this.setId(id);
        this.setLanguageCode(languageCode);
        this.setValues(values);
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLabel() {
        return label;
    }

    public List<String> getValues() {
        return List.copyOf(values);
    }

    public void setValue(String value) {
        final String[] strings = value.split(",");
        final List<String> trimmed = Arrays.stream(strings)
                .map(String::trim)
                .filter(x -> !x.isEmpty())
                .collect(Collectors.toList());
        this.setValues(trimmed);
    }

    public void setValues(List<String> values) {
        this.values.clear();
        this.values.addAll(values);
        this.label = String.join(", ", this.values);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("id", id)
                .append("languageCode", languageCode)
                .append("label", label)
                .append("values", values)
                .toString();
    }
}
