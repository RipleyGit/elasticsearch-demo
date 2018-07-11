package me.leiho.elasticsearch.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.leiho.elasticsearch.exception.BookException;
import me.leiho.elasticsearch.model.Book;
import me.leiho.elasticsearch.exception.BookException;
import me.leiho.elasticsearch.model.Book;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class BookDao {

    private static final String INDEX = "bookdata";
    private static final String TYPE = "books";

    private static final String ES_SERVICE_ERROR = "Elasticsearch服务异常";

    private RestHighLevelClient restHighLevelClient;

    private ObjectMapper objectMapper;

    public BookDao( ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) {
        this.objectMapper = objectMapper;
        this.restHighLevelClient = restHighLevelClient;
    }

    public Book insertBook(Book book){
        if (StringUtils.isBlank(book.getId())){
            book.setId(UUID.randomUUID().toString());
        }
        Map<String, Object> dataMap = objectMapper.convertValue(book, Map.class);
        IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, book.getId()).source(dataMap);
        try {
            restHighLevelClient.index(indexRequest);
        } catch(ElasticsearchException |java.io.IOException e) {
            throw new BookException(ES_SERVICE_ERROR,e);
        }
        return book;
    }

    public Map<String, Object> getBookById(String id){
        GetRequest getRequest = new GetRequest(INDEX, TYPE, id);
        GetResponse getResponse = null;
        try {
            getResponse = restHighLevelClient.get(getRequest);
        } catch (IOException e){
            throw new BookException(ES_SERVICE_ERROR,e);
        }
        Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
        if (sourceAsMap==null){
            throw new BookException("编号不存在,查询失败");
        }
        return sourceAsMap;
    }

    public Map<String, Object> updateBookById(String id, Book book){
        UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, id).fetchSource(true);    // Fetch Object after its update
        Map<String, Object> sourceAsMap = new HashMap<>();
        try {
            String bookJson = objectMapper.writeValueAsString(book);
            updateRequest.doc(bookJson, XContentType.JSON);
            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest);
            sourceAsMap = updateResponse.getGetResult().sourceAsMap();
        }catch (ElasticsearchStatusException e){
            throw new BookException("书籍未找到,更新失败",e);
        }catch (IOException e){
            throw new BookException(ES_SERVICE_ERROR,e);
        }
        return sourceAsMap;
    }

    public Boolean deleteBookById(String id) {
        DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, id);
        try {
            DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest);
            if ("NOT_FOUND".equalsIgnoreCase(deleteResponse.getResult().toString())){
                throw new BookException("书籍已删除,请勿重复操作");
            }
        } catch (IOException e){
            throw new BookException(ES_SERVICE_ERROR,e);
        }
        return true;
    }

}
