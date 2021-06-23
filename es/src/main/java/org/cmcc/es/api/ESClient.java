package org.cmcc.es.api;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

public class ESClient {
    public static RestHighLevelClient getClient() {
        //创建HttpHost对象
        HttpHost httpHost = new HttpHost("101.200.86.90", 9200);
        //创建RestClientBuilder
        RestClientBuilder builder = RestClient.builder(httpHost);
        //创建RestHighLevelClient
        return new RestHighLevelClient(builder);
    }
}
