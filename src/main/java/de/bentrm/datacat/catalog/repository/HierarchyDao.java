package de.bentrm.datacat.catalog.repository;

import java.util.List;

public interface HierarchyDao {

    /**
     * Returns a list of node paths originating from nodes marked with the given label
     * and tagged with the given tag.
     * The structure can be mapped to a tree.
     *
     * @param label The label of the root nodes.
     * @param depth The maximum depth of the tree. Should be an even number.
     * @return A list of node id paths.
     */
    List<List<String>> getHierarchyPaths(List<String> rootNodeIds, int depth);

}
