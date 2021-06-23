package org.cmcc.es.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.common.xcontent.json.JsonXContent;
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
}
