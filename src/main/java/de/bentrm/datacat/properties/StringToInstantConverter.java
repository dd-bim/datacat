package de.bentrm.datacat.properties;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.neo4j.driver.Value;
import org.neo4j.driver.Values;

import java.time.Instant;

@ReadingConverter
public class StringToInstantConverter implements Converter<Value, Instant> {
    @Override
    public Instant convert(Value source) {
        return Instant.parse(source.asString());
    }
}