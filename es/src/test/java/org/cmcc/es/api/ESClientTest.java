package org.cmcc.es.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Arrays;
import org.cmcc.es.EsApplication;
import org.cmcc.es.bean.Person;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {EsApplication.class})
@Slf4j
public class ESClientTest {
    RestHighLevelClient client = ESClient.getClient();
    String index = "person";
    String type = "man";

    ObjectMapper mapper = new ObjectMapper();

    // ==============================start 操作索引==================================//

    /**
     * 创建索引
     *
     * @throws IOException
     */
    @Test
    public void createIndex() throws IOException {
        //1.准备关于索引的settings
        Settings.Builder settings = Settings.builder()
                .put("number_of_shards", 3)
                .put("number_of_replicas", 1);
        //2.准备关于索引的结构mappings
        XContentBuilder mappings = JsonXContent.contentBuilder()
                .startObject().startObject("properties")
                .startObject("name").field("type", "text").endObject()
                .startObject("age").field("type", "integer").endObject()
                .startObject("birthday").field("type", "date").field("format", "yyyy-MM-dd").endObject()
                .endObject().endObject();
        //3.将settings和mappings封装到一个Request对象中
        CreateIndexRequest request = new CreateIndexRequest(index).settings(settings).mapping(type, mappings);
        //4.通过client对象去连接ES并执行创建索引
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        //5.输出
        System.out.println("resp" + createIndexResponse);
    }

    // ==============================end 操作索引==================================//

    /**
     * 检索是否存在
     *
     * @throws IOException
     */
    @Test
    public void exist() throws IOException {
        //1.主备request对象
        GetIndexRequest request = new GetIndexRequest().indices(index);
        //2.通过client操作
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        //3.输出
        System.out.println(exists);
    }

    /**
     * 删除索引
     *
     * @throws IOException
     */
    @Test
    public void delete() throws IOException {
        //1.准备request对象
        DeleteIndexRequest request = new DeleteIndexRequest().indices(index);
        //2.通过client操作
        AcknowledgedResponse delete = client.indices().delete(request, RequestOptions.DEFAULT);
        //3.输出
        System.out.println(delete.isAcknowledged());
    }

    // ==============================start 操作文档==================================//

    /**
     * 创建文档
     *
     * @throws IOException
     */
    @Test
    public void createDoc() throws IOException {
        //1.准备一个json数据
        Person person = new Person(1L, "小刚子", 35, new Date());
        String json = mapper.writeValueAsString(person);
        //2.准备一个request对象（手动指定id）
        IndexRequest request = new IndexRequest(index, type, person.getId().toString());
        request.source(json, XContentType.JSON);
        //3.通过client操作
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        //4.输出
        System.out.println(response.getResult().toString());//CREATED
    }

    /**
     * 文档修改
     *
     * @throws IOException
     */
    @Test
    public void updateDoc() throws IOException {
        //1.创建一个Map，指定需要修改的内容
        HashMap<Object, Object> doc = new HashMap<>();
        doc.put("name", "小凯子");
        String docId = "1";
        //2.将数据封装到request对象
        UpdateRequest updateRequest = new UpdateRequest(index, type, docId);
        updateRequest.doc(doc);
        //3.通过client执行操作
        UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        //4.输出
        System.out.println(updateResponse.getResult().toString());//UPDATED
    }

    /**
     * 删除文档
     */
    @Test
    public void deleteDoc() throws IOException {
        //1.封装request对象
        DeleteRequest deleteRequest = new DeleteRequest(index, type, "1");
        //2.client执行
        DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
        //3.输出
        System.out.println(deleteResponse.getResult().toString());//DELETED
    }

    /**
     * 批量添加文档
     */
    @Test
    public void bulkCreateDoc() throws IOException {
        //准备Request数据
        BulkRequest bulkRequest = new BulkRequest();
        for (int i = 0; i < 100; i++) {
            Person person = new Person((long) i, "测试" + i, i * 10, new Date());
            IndexRequest indexRequest = new IndexRequest(index, type, person.getId().toString());
            IndexRequest source = indexRequest.source(mapper.writeValueAsString(person), XContentType.JSON);
            bulkRequest.add(source);
        }
        //3.client执行
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        //4.输出
        System.out.println(bulkResponse.toString());
    }

    /**
     * 批量删除文档
     */
    @Test
    public void bulkDeleteDoc() throws IOException {
        //准备Request数据
        BulkRequest bulkRequest = new BulkRequest();
        for (int i = 0; i < 100; i++) {
            Person person = new Person((long) i, "测试" + i, i * 10, new Date());
            DeleteRequest deleteRequest = new DeleteRequest(index, type, person.getId().toString());
            bulkRequest.add(deleteRequest);
        }
        //3.client执行
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        //4.输出
        System.out.println(bulkResponse.toString());
    }
    // ==============================end 操作文档==================================//

    // ==============================start 复杂查询==================================//

    /**
     * term查询
     */
    @Test
    public void termQuery() throws IOException {
        index = "book";
        type = "novel";
        //1.创建Request对象
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        //2.指定查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(5);
        searchSourceBuilder.query(QueryBuilders.termQuery("name", "西游记"));
        searchRequest.source(searchSourceBuilder);
        //3.执行查询
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        //4.输出结果
        SearchHit[] hits = search.getHits().getHits();
        for (SearchHit hit : hits) {
            String id = hit.getId();
            System.out.println(id + ":" + hit.getSourceAsMap());
            /*
            ftqNN3oBwiPJbe8gGw2O:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
            gNqNN3oBwiPJbe8gPw0j:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
            f9qNN3oBwiPJbe8gOw0r:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
            gdqNN3oBwiPJbe8gQw0t:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
            gtqNN3oBwiPJbe8gRw1M:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
             */
        }
    }

    /**
     * terms查询
     */
    @Test
    public void termsQuery() throws IOException {
        index = "book";
        type = "novel";
        //1.创建Request对象
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        //2.指定查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termsQuery("name", "西游记", "红楼梦"));
        searchRequest.source(searchSourceBuilder);
        //3.执行查询
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        //4.输出结果
        SearchHit[] hits = search.getHits().getHits();
        for (SearchHit hit : hits) {
            String id = hit.getId();
            System.out.println(id + ":" + hit.getSourceAsMap());
            /*
            f9qNN3oBwiPJbe8gOw0r:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
            gdqNN3oBwiPJbe8gQw0t:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
            ftqNN3oBwiPJbe8gGw2O:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
            gtqNN3oBwiPJbe8gRw1M:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
            g9qNN3oBwiPJbe8gSg3s:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
            1:{descr=刘姥姥进大观园, author=施耐庵, name=红楼梦, count=1000001, on-sale=2001-01-10}
            gNqNN3oBwiPJbe8gPw0j:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
             */
        }
    }

    /**
     * match_all查询
     */
    @Test
    public void matchAllQuery() throws IOException {
        index = "book";
        type = "novel";
        //1.创建Request对象
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        //2.指定查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        //ES默认查询十条 如果想查询多条需要设置size
        searchSourceBuilder.size(20);
        searchRequest.source(searchSourceBuilder);
        //3.执行查询
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        //4.输出结果
        SearchHit[] hits = search.getHits().getHits();
        System.out.println(hits.length);
        for (SearchHit hit : hits) {
            String id = hit.getId();
            System.out.println(id + ":" + hit.getSourceAsMap());
            /*
            7
            f9qNN3oBwiPJbe8gOw0r:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
            gdqNN3oBwiPJbe8gQw0t:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
            ftqNN3oBwiPJbe8gGw2O:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
            gtqNN3oBwiPJbe8gRw1M:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
            g9qNN3oBwiPJbe8gSg3s:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
            1:{descr=刘姥姥进大观园, author=施耐庵, name=红楼梦, count=1000001, on-sale=2001-01-10}
            gNqNN3oBwiPJbe8gPw0j:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
             */
        }
    }

    /**
     * match查询
     */
    @Test
    public void matchQuery() throws IOException {
        index = "book";
        type = "novel";
        //1.创建Request对象
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        //2.指定查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("descr", "九九八十一"));
        searchRequest.source(searchSourceBuilder);
        //3.执行查询
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        //4.输出结果
        SearchHit[] hits = search.getHits().getHits();
        System.out.println(hits.length);
        for (SearchHit hit : hits) {
            String id = hit.getId();
            System.out.println(id + ":" + hit.getSourceAsMap());
            /*
            6
            gtqNN3oBwiPJbe8gRw1M:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
            g9qNN3oBwiPJbe8gSg3s:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
            ftqNN3oBwiPJbe8gGw2O:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
            gNqNN3oBwiPJbe8gPw0j:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
            f9qNN3oBwiPJbe8gOw0r:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
            gdqNN3oBwiPJbe8gQw0t:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
             */
        }
    }

    /**
     * 布尔match查询
     */
    @Test
    public void booleanMatchQuery() throws IOException {
        index = "book";
        type = "novel";
        //1.创建Request对象
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        //2.指定查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //searchSourceBuilder.query(QueryBuilders.matchQuery("descr","刘姥姥 四大名著").operator(Operator.AND));
        searchSourceBuilder.query(QueryBuilders.matchQuery("descr", "刘姥姥 四大名著").operator(Operator.OR));
        searchRequest.source(searchSourceBuilder);
        //3.执行查询
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        //4.输出结果
        SearchHit[] hits = search.getHits().getHits();
        System.out.println(hits.length);
        for (SearchHit hit : hits) {
            String id = hit.getId();
            System.out.println(id + ":" + hit.getSourceAsMap());
            /*
            2
            1:{descr=四大名著刘姥姥进大观园, author=施耐庵, name=红楼梦, count=1000001, on-sale=2001-01-10}
            2:{descr=刘姥姥进大观园。, author=曹雪芹, name=红楼梦, count=123456789, on-sale=2011-12-13}
             */
        }
    }

    /**
     * multi_match查询
     */
    @Test
    public void multiMatchQuery() throws IOException {
        index = "book";
        type = "novel";
        //1.创建Request对象
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        //2.指定查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery("四大名著 红楼梦", "descr", "name"));
        searchRequest.source(searchSourceBuilder);
        //3.执行查询
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        //4.输出结果
        SearchHit[] hits = search.getHits().getHits();
        System.out.println(hits.length);
        for (SearchHit hit : hits) {
            String id = hit.getId();
            System.out.println(id + ":" + hit.getSourceAsMap());
            /*
            2
            1:{descr=四大名著刘姥姥进大观园, author=施耐庵, name=红楼梦, count=1000001, on-sale=2001-01-10}
            2:{descr=刘姥姥进大观园。, author=曹雪芹, name=红楼梦, count=123456789, on-sale=2011-12-13}
             */
        }
    }

    /**
     * id的查询
     */
    @Test
    public void queryById() throws IOException {
        index = "book";
        type = "novel";
        //1.创建Request对象
        GetRequest getRequest = new GetRequest(index, type, "1");
        //2.执行查询
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        //3.输出结果
        System.out.println(getResponse.getSourceAsMap());
        /*
        {descr=四大名著刘姥姥进大观园, author=施耐庵, name=红楼梦, count=1000001, on-sale=2001-01-10}
         */
    }

    /**
     * ids的查询
     */
    @Test
    public void queryByIds() throws IOException {
        index = "book";
        type = "novel";
        //1.创建Request对象
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        //2.指定查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.idsQuery().addIds("1", "2"));
        searchRequest.source(searchSourceBuilder);
        //3.执行查询
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        //4.输出结果
        SearchHit[] hits = search.getHits().getHits();
        System.out.println(hits.length);
        for (SearchHit hit : hits) {
            String id = hit.getId();
            System.out.println(id + ":" + hit.getSourceAsMap());
            /*
            2
            2:{descr=刘姥姥进大观园。, author=曹雪芹, name=红楼梦, count=123456789, on-sale=2011-12-13}
            1:{descr=四大名著刘姥姥进大观园, author=施耐庵, name=红楼梦, count=1000001, on-sale=2001-01-10}
            */
        }
    }

    /**
     * prefix查询
     */
    @Test
    public void queryByPrefix() throws IOException {
        index = "book";
        type = "novel";
        //1.创建Request对象
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        //2.指定查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.prefixQuery("descr", "大名"));
        searchRequest.source(searchSourceBuilder);
        //3.执行查询
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        //4.输出结果
        SearchHit[] hits = search.getHits().getHits();
        System.out.println(hits.length);
        for (SearchHit hit : hits) {
            String id = hit.getId();
            System.out.println(id + ":" + hit.getSourceAsMap());
            /*
            1
            1:{descr=四大名著刘姥姥进大观园, author=施耐庵, name=红楼梦, count=1000001, on-sale=2001-01-10}
            */
        }
    }

    /**
     * fuzzy查询
     */
    @Test
    public void queryByFuzzy() throws IOException {
        index = "book";
        type = "novel";
        //1.创建Request对象
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        //2.指定查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.fuzzyQuery("descr", "lao").prefixLength(2));
        searchRequest.source(searchSourceBuilder);
        //3.执行查询
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        //4.输出结果
        SearchHit[] hits = search.getHits().getHits();
        System.out.println(hits.length);
        for (SearchHit hit : hits) {
            String id = hit.getId();
            System.out.println(id + ":" + hit.getSourceAsMap());
            /*
            1
            4:{descr=liu lao lao。, author=曹雪芹, name=红楼梦, count=123456789, on-sale=2011-12-13}
            */
        }
    }

    /**
     * wildcard查询
     */
    @Test
    public void queryByWildCard() throws IOException {
        index = "book";
        type = "novel";
        //1.创建Request对象
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        //2.指定查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.wildcardQuery("descr", "*laolao"));
        searchRequest.source(searchSourceBuilder);
        //3.执行查询
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        //4.输出结果
        SearchHit[] hits = search.getHits().getHits();
        System.out.println(hits.length);
        for (SearchHit hit : hits) {
            String id = hit.getId();
            System.out.println(id + ":" + hit.getSourceAsMap());
            /*
            1
            3:{descr=liulaolao进大观园。, author=曹雪芹, name=红楼梦, count=123456789, on-sale=2011-12-13}
            */
        }
    }

    /**
     * range查询
     */
    @Test
    public void queryByRange() throws IOException {
        index = "book";
        type = "novel";
        //1.创建Request对象
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        //2.指定查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.rangeQuery("count").gte(1000001).lte(1000002));
        searchRequest.source(searchSourceBuilder);
        //3.执行查询
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        //4.输出结果
        SearchHit[] hits = search.getHits().getHits();
        System.out.println(hits.length);
        for (SearchHit hit : hits) {
            String id = hit.getId();
            System.out.println(id + ":" + hit.getSourceAsMap());
            /*
            1
            1:{descr=四大名著刘姥姥进大观园, author=施耐庵, name=红楼梦, count=1000001, on-sale=2001-01-10}
            */
        }
    }


    /**
     * Regexp查询
     */
    @Test
    public void queryByRegexp() throws IOException {
        index = "book";
        type = "novel";
        //1.创建Request对象
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        //2.指定查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.regexpQuery("descr", "123[0-9]{6}"));
        searchRequest.source(searchSourceBuilder);
        //3.执行查询
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        //4.输出结果
        SearchHit[] hits = search.getHits().getHits();
        System.out.println(hits.length);
        for (SearchHit hit : hits) {
            String id = hit.getId();
            System.out.println(id + ":" + hit.getSourceAsMap());
            /*
            1
            5:{descr=123000000liu lao lao。, author=曹雪芹, name=红楼梦, count=123456789, on-sale=2011-12-13}
            */
        }
    }


    /**
     * Scroll分页查询
     */
    @Test
    public void queryByScroll() throws IOException {
        index = "book";
        type = "novel";
        //1.创建Request对象
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        //2.指定scroll信息
        searchRequest.scroll(TimeValue.timeValueMillis(10L));
        //3.指定查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(6);
        searchSourceBuilder.sort("count", SortOrder.DESC);
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        //4.执行查询
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        //5.得到scrollId 并输出首页结果
        String scrollId = search.getScrollId();
        System.out.println("========首页========");
        SearchHit[] hits = search.getHits().getHits();
        for (SearchHit hit : hits) {
            System.out.println(hit.getId() + ":" + hit.getSourceAsMap());
        }
        int page = 1;
        while (true) {
            //6.循环 - 创建SearchScrollRequest
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            //7.指定scrollId的生存时间
            scrollRequest.scroll(TimeValue.timeValueMinutes(10L));
            //8.执行查询获取返回结果
            SearchResponse searchResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
            //9.判断结果 输出结果
            SearchHit[] searchHits = searchResponse.getHits().getHits();
            if (!Arrays.isNullOrEmpty(searchHits)) {
                page += 1;
                System.out.println("======第" + page + "页=======");
                for (SearchHit searchHit : searchHits) {
                    System.out.println(searchHit.getId() + ":" + searchHit.getSourceAsMap());
                }
            } else {
                //10.数据结果已经为空终止循环
                System.out.println("=======结束========");
                break;
            }
        }
        //11.创建ClearScrollRequest对象
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        //12.指定scrollId
        clearScrollRequest.addScrollId(scrollId);
        //13.执行删除
        ClearScrollResponse clearScrollResponse = client.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
        //14.输出结果
        System.out.println("删除scroll结果：" + clearScrollResponse.isSucceeded());
        /*
        ========首页========
        5:{descr=123000000liu lao lao。, author=曹雪芹, name=红楼梦, count=123456789, on-sale=2011-12-13}
        2:{descr=刘姥姥进大观园。, author=曹雪芹, name=红楼梦, count=123456789, on-sale=2011-12-13}
        4:{descr=liu lao lao。, author=曹雪芹, name=红楼梦, count=123456789, on-sale=2011-12-13}
        3:{descr=liulaolao进大观园。, author=曹雪芹, name=红楼梦, count=123456789, on-sale=2011-12-13}
        1:{descr=四大名著刘姥姥进大观园, author=施耐庵, name=红楼梦, count=1000001, on-sale=2001-01-10}
        f9qNN3oBwiPJbe8gOw0r:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
        ======第2页=======
        gdqNN3oBwiPJbe8gQw0t:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
        ftqNN3oBwiPJbe8gGw2O:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
        KPwBPXoBpklwLylkcN6G:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
        gtqNN3oBwiPJbe8gRw1M:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
        g9qNN3oBwiPJbe8gSg3s:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
        gNqNN3oBwiPJbe8gPw0j:{descr=九九八十一难, author=吴承恩, name=西游记, count=1000000, on-sale=2000-01-10}
        =======结束========
        删除scroll结果：true
         */
    }


    // ==============================end 复杂查询==================================//
}
