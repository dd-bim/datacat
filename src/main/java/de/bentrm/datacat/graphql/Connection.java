package de.bentrm.datacat.graphql;

import org.springframework.data.domain.Page;

import java.util.List;

public class Connection<T> {

    private List<T> nodes;
    private PageInfo page;

    public Connection(Page<T> page) {
        this.nodes = page.getContent();
        this.page = new PageInfo(page);
    }

    public List<T> getNodes() {
        return nodes;
    }

    public PageInfo getPage() {
        return page;
    }
}
