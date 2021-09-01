package com.msj;

import com.alibaba.fastjson.JSON;
import com.msj.pojo.User;
import com.msj.utils.ESConst;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDataSourceConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * 测试es 7.12.0高级客户端的API
 */
@SpringBootTest
class MsjEsApiApplicationTests {

    @Autowired
    //也可以指定要注入的bean名
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    @Test
    void contextLoads() {
    }

    //测试创建索引 Request  对应put命令
    @Test
    void testCreateIndex() throws IOException {
        //1、创建索引请求
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("kuang_index");
        //2.客户端执行创建请求 indices:index的复数形式之一,第二参数使用默认的请求参数就可以了
        CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        //会得到一个对象
        System.out.println(createIndexResponse);
    }

    //测试获取索引
    @Test
    void testExistIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("kuang_index");
        boolean exist = client.indices().exists(request,RequestOptions.DEFAULT);
        System.out.println(exist);
    }

    //测试删除索引
    @Test
    void testDeleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("kuang_index");
        //删除后会返回一个AcknowledgedResponse对象，在可视化界面删除也会提示Acknowledge为true
        AcknowledgedResponse delete = client.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(delete.isAcknowledged());
    }

    //测试添加文档
    @Test
    void testAddDocument() throws IOException {
        //1、创建对象
        User user = new User("狂神说", 22);
        //创建请求:IndexRequest
        IndexRequest request = new IndexRequest("kuang_index");

        //规则 put /kuang_index/_doc/id
        //设置id为1
        request.id("1");
        //设置超时时间
        request.timeout(TimeValue.timeValueSeconds(1));  //等价于：request.timeout("1s");
        //将我们的数据放入json
        request.source(JSON.toJSONString(user), XContentType.JSON);
        //客户端发送请求,获取相应结果
        IndexResponse index = client.index(request, RequestOptions.DEFAULT);
        System.out.println(index.toString());
        //状态：与前端的一样，第一次创建为created，修改为update
        System.out.println(index.status());
    }

    //获取文档，先判断是否存在
    @Test
    void testIsExists() throws IOException {
        GetRequest request = new GetRequest("kuang_index", "1");
        //不获取返回的_source的上下文，效率更高
        request.fetchSourceContext(new FetchSourceContext(false));
        //不设置排序字段
        request.storedFields("_none_");

        boolean exists = client.exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    //获取文档的信息
    @Test
    void testGetDocInfo() throws IOException {
        GetRequest request = new GetRequest("kuang_index", "1");
        GetResponse documentFields = client.get(request, RequestOptions.DEFAULT);
        System.out.println(documentFields.getSourceAsString());  //打印文档内容
        System.out.println(documentFields);//这里返回的全部内容和命令是一样的
    }

    //更新文档信息
    @Test
    void testUpdateDoc() throws IOException {
        UpdateRequest request = new UpdateRequest("kuang_index", "1");
        request.timeout("1s");

        User user  = new User("狂神说Java",18);
        //第二个参数为文档的类型：这里为json
        request.doc(JSON.toJSONString(user),XContentType.JSON);

        UpdateResponse update = client.update(request, RequestOptions.DEFAULT);
        System.out.println(update.status());

    }

    //删除文档记录
    @Test
    void testDeleteRequest() throws IOException {
        DeleteRequest request = new DeleteRequest("kuang_index","3");
        request.timeout("1s");

        DeleteResponse delete = client.delete(request, RequestOptions.DEFAULT);
        System.out.println(delete.status());
    }

    //真实项目都会批量插入数据
    @Test
    void testBulKRequest() throws IOException {
        //批处理请求
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("1s");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(new User("狂神1",12));
        userList.add(new User("狂神2",13));
        userList.add(new User("狂神3",14));
        userList.add(new User("狂神4",15));
        userList.add(new User("狂神5",15));
        userList.add(new User("狂神6",16));
        userList.add(new User("狂神7",19));
        for (int i = 0; i < userList.size(); i++) {
            bulkRequest.add(
                  new IndexRequest("kuang_index")
                   .id("" + (i+1))
                    .source(JSON.toJSONString(userList.get(i)),XContentType.JSON));
        }
        BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        //是否失败，返回false表示成功
        System.out.println(bulk.hasFailures());
    }

    //查询
    @Test
    void testSearch() throws IOException {
        SearchRequest request = new SearchRequest(ESConst.ES_INDEX);
        //构建搜索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //查询条件：我们可以使用QueryBuilders工具来快速构建
        //QueryBuilders.termQuery精确查询
        //查询所有：QueryBuilders.matchAllQuery
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "狂");
        //把查询条件放到构建器中
        searchSourceBuilder.query(termQueryBuilder);
        //设置超时
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        //分页
        /*searchSourceBuilder.from();
        searchSourceBuilder.size();*/

        //构建搜索请求
        request.source(searchSourceBuilder);
        //搜索
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        //打印结果对象
        System.out.println(JSON.toJSONString(search.getHits()));
        System.out.println("=========================================");
        for (SearchHit documentFields : search.getHits().getHits()) {
            System.out.println(documentFields.getSourceAsMap());
        }
    }

}
