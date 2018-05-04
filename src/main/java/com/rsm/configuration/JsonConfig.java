package com.rsm.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rsm.serialization.InstantDeserializer;
import com.rsm.serialization.InstantSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;

/**
 * Created by Dawid on 04.05.2018 at 13:09.
 */

@Configuration
public class JsonConfig {
    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantSerializer())
                .registerTypeAdapter(Instant.class, new InstantDeserializer())
                .create();
    }

}
