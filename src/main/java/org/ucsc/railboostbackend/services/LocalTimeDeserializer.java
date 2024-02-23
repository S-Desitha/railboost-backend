package org.ucsc.railboostbackend.services;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeDeserializer implements JsonDeserializer<LocalTime> {
//    public static final LocalTimeDeserializer INSTANCE = new LocalTimeDeserializer();

//    private final DateTimeFormatter formatter;
//
//    public LocalTimeDeserializer() {
//        this(DateTimeFormatter.ISO_LOCAL_TIME);
//    }
//
//    public LocalTimeDeserializer(DateTimeFormatter formatter) {
//        this.formatter = formatter;
//    }

    @Override
    public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//        try {
//            return formatter.parse(json.getAsString()).query(LocalTime::from);
//        } catch (Exception e) {
//            throw new JsonParseException(e);
//        }
        return LocalTime.parse(json.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ISO_LOCAL_TIME);
    }
}
