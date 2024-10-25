package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdDictionary;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.ObjectRecordService;
import de.bentrm.datacat.graphql.fetcher.delegate.ObjectFetchersDelegate;
import de.bentrm.datacat.graphql.fetcher.delegate.RootFetchersDelegate;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ObjectFetchers extends AbstractFetchers<XtdObject> {

    private final RootFetchersDelegate rootFetchersDelegate;
    private final ObjectFetchersDelegate objectFetchersDelegate;
    private final DataFetcher<XtdDictionary> dictionary;
    private final DataFetcher<XtdMultiLanguageText> deprecationExplanation;
    private final DataFetcher<List<XtdObject>> replacedObjects;
    private final DataFetcher<List<XtdObject>> replacingObjects;
    private final DataFetcher<List<XtdMultiLanguageText>> names;

    public ObjectFetchers(ObjectRecordService queryService,
                           RootFetchersDelegate rootFetchersDelegate,
                           ObjectFetchersDelegate objectFetchersDelegate,
                           CatalogService catalogService) {
        super(queryService);
        this.rootFetchersDelegate = rootFetchersDelegate;
        this.objectFetchersDelegate = objectFetchersDelegate;

        this.dictionary = environment -> {
            final XtdObject object = environment.getSource();
            return queryService.getDictionary(object);
        };

        this.deprecationExplanation = environment -> {
            final XtdObject object = environment.getSource();
            return queryService.getDeprecationExplanation(object);
        };

        this.replacedObjects = environment -> {
            final XtdObject object = environment.getSource();
            return queryService.getReplacedObjects(object);
        };

        this.replacingObjects = environment -> {
            final XtdObject object = environment.getSource();
            return queryService.getReplacingObjects(object);
        };

        // this.replacedObjects = environment -> {
        //     final XtdObject object = environment.getSource();
        //     final List<String> replacedObjectsIds = object.getReplacedObjects().stream().map(XtdObject::getId).collect(Collectors.toList());
        //     return catalogService.getAllObjectsById(replacedObjectsIds);
        // };

        // this.replacingObjects = environment -> {
        //     final XtdObject object = environment.getSource();
        //     final List<String> replacingObjectsIds = object.getReplacingObjects().stream().map(XtdObject::getId).collect(Collectors.toList());
        //     return catalogService.getAllObjectsById(replacingObjectsIds);
        // };

        this.names = environment -> {
            final XtdObject object = environment.getSource();
            return queryService.getNames(object);
        };
    }

    @Override
    public String getTypeName() {
        return "XtdObject";
    }

    @Override
    public String getFetcherName() {
        return "getObject";
    }

    @Override
    public String getListFetcherName() {
        return "findObjects";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        Map<String, DataFetcher> fetchers = new HashMap<>();
        fetchers.putAll(super.getAttributeFetchers());
        // fetchers.putAll(rootFetchersDelegate.getFetchers());
        // fetchers.putAll(objectFetchersDelegate.getFetchers());
        fetchers.put("dictionary", dictionary);
        fetchers.put("deprecationExplanation", deprecationExplanation);
        fetchers.put("replacedObjects", replacedObjects);
        fetchers.put("replacingObjects", replacingObjects);
        fetchers.put("names", names);
        return fetchers;
    }
}
