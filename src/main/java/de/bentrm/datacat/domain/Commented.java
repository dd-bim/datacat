package de.bentrm.datacat.domain;

import java.util.SortedSet;

public interface Commented {

    SortedSet<Comment> getComments();
    void setComments(SortedSet<Comment> comments);

}
