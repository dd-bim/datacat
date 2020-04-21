package de.bentrm.datacat.query;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class SearchOptions<ID extends Serializable> {

    private String term;

    private final Set<@NotBlank String> labels = new HashSet<>();

    private final Set<@NotBlank String> excludedLabels = new HashSet<>();

    private final Set<@NotNull ID> excludedIds = new HashSet<>();

    public SearchOptions() {}

    public SearchOptions(String term, Iterable<String> labels, Iterable<String> excludedLabels, Iterable<ID> excludedIds) {
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

    public void setTerm(String term) {
        this.term = term;
    }

    public Set<String> getLabels() {
        return labels;
    }

    public Set<String> getExcludedLabels() {
        return excludedLabels;
    }

    public Set<ID> getExcludedIds() {
        return excludedIds;
    }
}