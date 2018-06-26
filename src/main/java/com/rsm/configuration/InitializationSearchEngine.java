package com.rsm.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dawid on 26.06.2018 at 17:08.
 */

@Component
@RequiredArgsConstructor
public class InitializationSearchEngine implements InitializingBean {

    @Value("${elastic.api.url}")
    private String serverUrl;
    @Value("${estactic.api.server.port}")
    private Long port;

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String fooResourceUrl = serverUrl + ":" + port + "/_all/_settings";
            Map<String, Object> settings = new HashMap<>();
            settings.put("index.blocks.read_only_allow_delete", null);
            restTemplate.put(fooResourceUrl, settings);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
