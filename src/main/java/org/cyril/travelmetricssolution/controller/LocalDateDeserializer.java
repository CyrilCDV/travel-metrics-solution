package org.cyril.travelmetricssolution.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.joda.time.LocalDate;

import java.io.IOException;

public class LocalDateDeserializer extends StdDeserializer<LocalDate> {


    protected LocalDateDeserializer() {
        super(LocalDate.class);
    }

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final String valueAsString = jsonParser.getValueAsString();
        return new LocalDate(valueAsString);
    }
}
