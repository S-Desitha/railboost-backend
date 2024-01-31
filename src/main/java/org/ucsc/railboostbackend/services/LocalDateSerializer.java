package org.ucsc.railboostbackend.services;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateSerializer implements JsonSerializer<LocalDate> {

    public static final LocalDateSerializer INSTANCE = new LocalDateSerializer();

    @Override
    public JsonElement serialize(LocalDate localDate, Type type, JsonSerializationContext jsonSerializationContext) {
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        return new JsonPrimitive(formattedDate);
    }
}
