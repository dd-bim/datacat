package de.bentrm.datacat.graphql;

import de.bentrm.datacat.domain.Entity;

import java.util.List;

public class Connection<T extends Entity> {

    private List<T> nodes;
    private PageInfo page;

    public List<T> getNodes() {
        return nodes;
    }

    public void setNodes(List<T> nodes) {
        this.nodes = nodes;
    }

    public PageInfo getPage() {
        return page;
    }

    public void setPage(PageInfo page) {
        this.page = page;
    }
}
