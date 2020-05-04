package de.bentrm.datacat.query;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class FilterOptions<ID extends Serializable> {

    private final String term;

    private final @NotEmpty Set<@NotBlank String> labels = new HashSet<>();

    private final Set<@NotBlank String> excludedLabels = new HashSet<>();

    private final Set<@NotNull ID> excludedIds = new HashSet<>();

    public FilterOptions() {
        this(null, null, null, null);
    }

    public FilterOptions(String term) {
        this(term, null, null, null);
    }

    public FilterOptions(Iterable<String> labels, Iterable<String> excludedLabels, Iterable<ID> excludedIds) {
        this(null, labels, excludedLabels, excludedIds);
    }

    public FilterOptions(String term, Iterable<String> labels, Iterable<String> excludedLabels, Iterable<ID> excludedIds) {
        this.term = term;

        if (labels != null) {
            labels.forEach(this.labels::add);
        }
        if (excludedLabels != null) {
            excludedLabels.forEach(this.excludedLabels::add);
        }
        if (excludedIds != null) {
            excludedIds.forEach(this.excludedIds::add);
        }
    }

    public String getTerm() {
        return term;
    }

    public Set<String> getLabels() {
        return new HashSet<>(labels);
    }

    public Set<String> getExcludedLabels() {
        return new HashSet<>(excludedLabels);
    }

    public Set<ID> getExcludedIds() {
        return new HashSet<>(excludedIds);
    }
}
