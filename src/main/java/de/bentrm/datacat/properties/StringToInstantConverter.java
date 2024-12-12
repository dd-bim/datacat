package de.bentrm.datacat.properties;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.NonNull;
import org.neo4j.driver.Value;
import java.time.Instant;

@ReadingConverter
public class StringToInstantConverter implements Converter<Value, Instant> {
    @Override
    public Instant convert(@NonNull Value source) {
        return Instant.parse(source.asString());
    }
}