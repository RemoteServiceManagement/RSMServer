package com.rsm.elesticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Dawid on 04.05.2018 at 10:42.
 */
@Configuration
public class ElasticSearchConfig {
    @Value("${elasticsearch.name}")
    private String name;
    @Value("${elasticsearch.port}")
    private Integer port;


    @Bean
    public Client client() throws UnknownHostException {
        return new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(name), port));
    }
}
