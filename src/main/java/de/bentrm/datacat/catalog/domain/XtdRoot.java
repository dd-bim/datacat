package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.util.Assert;

import de.bentrm.datacat.util.LocalizationUtils;

import java.util.*;

import jakarta.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@Node(XtdRoot.LABEL)
public abstract class XtdRoot extends CatalogRecord {

    public static final String LABEL = "XtdRoot";

}
