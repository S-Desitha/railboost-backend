package org.ucsc.railboostbackend.services;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateDeserializer implements JsonDeserializer<LocalDate> {
    public static final LocalDateDeserializer INSTANCE = new LocalDateDeserializer();
    @Override
    public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return LocalDate.parse(json.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }


}
