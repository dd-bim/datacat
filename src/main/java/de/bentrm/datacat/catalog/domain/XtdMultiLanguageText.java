package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import de.bentrm.datacat.util.LocalizationUtils;

import java.util.*;

import javax.validation.constraints.NotNull;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = XtdMultiLanguageText.LABEL)
public class XtdMultiLanguageText extends XtdRoot {

    public static final String LABEL = "XtdMultiLanguageText";

    @Relationship(type = "TEXTS")
    private final Set<XtdText> texts = new HashSet<>();
}
