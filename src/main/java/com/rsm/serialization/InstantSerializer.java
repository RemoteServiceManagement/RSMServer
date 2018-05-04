package com.rsm.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.Instant;

/**
 * Created by Dawid on 04.05.2018 at 12:32.
 */
public class InstantSerializer implements JsonSerializer<Instant> {
    @Override
    public JsonElement serialize(Instant instant, Type type, JsonSerializationContext jsonSerializationContext) {
        if (instant != null) {
            return new JsonPrimitive(instant.getEpochSecond());
        }

        return JsonNull.INSTANCE;
    }
}
