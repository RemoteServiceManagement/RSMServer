package com.rsm.elesticsearch;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.script.Script;
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
}
