package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.domain.XtdText;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.MultiLanguageTextRecordService;
import de.bentrm.datacat.catalog.service.TextRecordService;
import de.bentrm.datacat.graphql.fetcher.delegate.ObjectFetchersDelegate;
import de.bentrm.datacat.graphql.fetcher.delegate.RootFetchersDelegate;
import de.bentrm.datacat.graphql.Connection;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MultiLanguageTextFetchers extends AbstractFetchers<XtdMultiLanguageText> {

    private final RootFetchersDelegate rootFetchersDelegate;
    private final ObjectFetchersDelegate objectFetchersDelegate;
    private final DataFetcher<List<XtdText>> texts;
    // private final DataFetcher<List<XtdRoot>> texts;


    public MultiLanguageTextFetchers(MultiLanguageTextRecordService queryService,
                           RootFetchersDelegate rootFetchersDelegate,
                           ObjectFetchersDelegate objectFetchersDelegate,
                           CatalogService catalogService) {
        super(queryService);
        this.rootFetchersDelegate = rootFetchersDelegate;
        this.objectFetchersDelegate = objectFetchersDelegate;

        this.texts = environment -> {
            final XtdMultiLanguageText source = environment.getSource();
            return queryService.getTexts(source);
        };
        // this.texts = environment -> {
        //     final XtdMultiLanguageText source = environment.getSource();
        //     final List<String> textIds = source.getTexts().stream().map(XtdText::getId).collect(Collectors.toList());
        //     textIds.forEach(id -> {log.info("TextIds: " + id);});
        //     return catalogService.getAllTextsById(textIds);
        //     // return catalogService.getAllRootItemsById(textIds);
        // };
    }

    @Override
    public String getTypeName() {
        return "XtdMultiLanguageText";
    }

    @Override
    public String getFetcherName() {
        return "getMultiLanguageText";
    }

    @Override
    public String getListFetcherName() {
        return "findMultiLanguageTexts";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        Map<String, DataFetcher> fetchers = new HashMap<>();
        fetchers.putAll(super.getAttributeFetchers());
        // fetchers.putAll(rootFetchersDelegate.getFetchers());
        // fetchers.putAll(objectFetchersDelegate.getFetchers());
        fetchers.put("texts", texts);
        return fetchers;
    }
}
