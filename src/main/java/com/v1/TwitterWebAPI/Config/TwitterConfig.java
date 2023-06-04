package com.v1.TwitterWebAPI.Config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = {"com.v1.TwitterWebAPI.repositories"})
//@ComponentScan(basePackages = {"com.v1.TwitterWebAPI"})
public class TwitterConfig {
    RestClient restClient = RestClient.builder(
            new HttpHost("localhost", 9200)).build();
    ElasticsearchTransport transport = new RestClientTransport(
            restClient, new JacksonJsonpMapper());
    ElasticsearchClient client = new ElasticsearchClient(transport);
}
