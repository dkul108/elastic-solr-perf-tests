package com.att.snr;

import org.apache.http.HttpHost;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.net.UnknownHostException;

import static java.net.InetAddress.getByName;

@Configuration
//@PropertySource(value = "classpath:config/elasticsearch.properties")
//@EnableElasticsearchRepositories(basePackages = "repository")
//@Service("searchConfiguration")
public class ElasticConfig {

    @Resource
    private Environment environment;

//    @Bean
//    public ElasticsearchOperations elasticsearchTemplate() throws IOException {
//        return new ElasticsearchTemplate(client());
//    }

    @Bean
    public Client client() throws UnknownHostException {
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
        TransportAddress address = new InetSocketTransportAddress(
                getByName(environment.getProperty("elasticsearch.host")),
                Integer.parseInt(environment.getProperty("elasticsearch.transport.port"))
        );
        client.addTransportAddress(address);
        return client;
    }

    @Bean
    public RestHighLevelClient highLevelClient() throws UnknownHostException {
        return new RestHighLevelClient(lowLevelClient());
    }

    @Bean
    public RestClient lowLevelClient() throws UnknownHostException {
        return RestClient.builder(
                new HttpHost(
                        environment.getProperty("elasticsearch.host"),
                        Integer.parseInt(environment.getProperty("elasticsearch.http.port")),
                        "http"
                )
        ).build();
    }
}
