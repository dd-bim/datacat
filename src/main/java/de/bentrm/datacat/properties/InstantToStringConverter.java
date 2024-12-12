package de.bentrm.datacat.properties;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.NonNull;
import org.neo4j.driver.Value;
import org.neo4j.driver.Values;

import java.time.Instant;

@WritingConverter
public class InstantToStringConverter implements Converter<Instant, Value> {
    @Override
    public Value convert(@NonNull Instant source) {
        return Values.value(source.toString());
    }
}