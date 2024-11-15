package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import de.bentrm.datacat.util.LocalizationUtils;

import java.util.*;

import jakarta.validation.constraints.NotNull;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@Node(XtdMultiLanguageText.LABEL)
public class XtdMultiLanguageText extends XtdRoot {

    public static final String LABEL = "XtdMultiLanguageText";

    @Relationship(type = "TEXTS")
    private final Set<XtdText> texts = new HashSet<>();
}
