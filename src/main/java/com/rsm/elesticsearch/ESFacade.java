package com.rsm.elesticsearch;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by Dawid on 04.05.2018 at 11:11.
 */

@Component
@RequiredArgsConstructor
public class ESFacade {
    private final Client client;
    private final Gson gson;

    public <T extends Serializable> void putIndex(String index, String type, T t, String id) {
        client.prepareIndex(index, type, id).setSource(gson.toJson(t), XContentType.JSON).get();
    }

    public void putIfMappingDoesNotExist(String index, String type, XContentBuilder mapping) {
        if (!indexExist(index))
            client.admin().indices().prepareCreate(index)
                    .addMapping(type, mapping).get();
    }

    private boolean indexExist(String index) {
        return client.admin().indices()
                .prepareExists(index)
                .execute().actionGet().isExists();
    }

    public <T> T get(String index, String type, String id, Class<T> clazz) {
        String result = client.prepareGet(index, type, id).get().getSourceAsString();
        return gson.fromJson(result, clazz);
    }
}
