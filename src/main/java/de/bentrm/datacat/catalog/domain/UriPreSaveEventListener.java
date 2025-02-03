package de.bentrm.datacat.catalog.domain;

import org.springframework.data.neo4j.core.mapping.callback.BeforeBindCallback;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import de.bentrm.datacat.properties.AppProperties;
import lombok.extern.slf4j.Slf4j;
/**
 * This class supports the creation of an uri for each new catalog record with type XtdObject.
 */
@Slf4j
@Component
public class UriPreSaveEventListener implements BeforeBindCallback<XtdObject> {

private final AppProperties properties;

public UriPreSaveEventListener(AppProperties properties) {
    this.properties = properties;
}

    /**
     * @param XtdObject The object to persist.
     */
    @Override
    public @NonNull XtdObject onBeforeBind(@NonNull XtdObject record) {
   
            String http = properties.getClient().getUrl();
            String  type = CatalogRecordType.getByDomainClass(record).toString().toLowerCase();
            record.setUri(http + "/" + type + "/" + record.getId());

            return record;
    }

}
