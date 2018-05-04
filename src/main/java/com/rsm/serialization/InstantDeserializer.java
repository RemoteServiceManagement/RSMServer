package com.rsm.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.Instant;

/**
 * Created by Dawid on 04.05.2018 at 12:42.
 */
public class InstantDeserializer implements JsonDeserializer<Instant> {
    @Override
    public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonNull()) {
            return null;
        }

        return Instant.ofEpochSecond(json.getAsLong());
    }
}
