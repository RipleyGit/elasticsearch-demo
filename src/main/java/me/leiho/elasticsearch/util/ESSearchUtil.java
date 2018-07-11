package me.leiho.elasticsearch.util;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

import java.util.Arrays;
import java.util.Iterator;

public class ESSearchUtil {
    private TransportClient client;

    private SearchResponse response;
    private SearchRequestBuilder searchRequestBuilder;

    /**
     * @description : 构造一个搜索实例
     * @param client	es服务器的ip地址
     */
    public ESSearchUtil(TransportClient client){
        this.client=client;
    }

    /**
     * @description : 使用前必须先自定义搜索语句
     */
    private QueryBuilder setQuery(){
        /*
         * 	must		查询子句must出现在文档中，并且会影响文档得分score
         *	filter		查询子句must出现在文档中，但是不会影响文档得分，并且会缓存
         *	should		查询子句should出现在文档中，即并列的should子句必须有一个或者多个出现在文档中，可以通过设置参数 minimum_should_match来指定最少匹配的查询子句
         *	must_not	查询子句一定不能出现在文档中，处于filter上下文，不影响文档得分，会被缓存
         *
         * 1.   加权查询,数据在"title","summary","author","content"四个字段中包含搜索关键词"国经理",并将该次查询分数变成1.0倍。
         * 2.   整句查询,数据在"title","summary"字段中包含搜索关键短语"事件反腐风暴",事件反腐风暴不分词，事件反腐风暴中间可间隔2位
         * 3.   模糊查询,数据在"title","summary"字段中包含搜索关键词"国经理",并将该次查询设置为模糊查询,模糊程度"AUTO"
         * 4.   数据在"title"字段中包含搜索符合关键词"反腐","国家","公司"中任意一个条件，并并将该次查询分数变成-0.9倍
         * 5.   数据不在"title","summary","author","content"任意一个字段内含有关键词"交易"
         * 6.	通配符查询,数据在"author"字段中含有通配符表达式"*w*"，支持？ 匹配任何字符，*匹配零个或多个字符
         * 7.   正则查询,数据在"author"字段中含有正则表达式"[a-z]k[a-z]"
         * 8.	前缀短语查询,数据在"summary"字段中以"据"开头的数据
         * 9-11。数据在3个should中满足至少一个查询子句
         * 12. 	设置至少要符合多少个条件,参数为百分比，或者整型
         * 13.  数据在"publish_time"字段里的数据在from("2018-01-13 14:21:09")和to("2018-01-17 14:21:09")之间
         * 14.	数据存在"title"字段
         * 15.  衰减函数DecayFunction(field, origin, scale, offset, decay),有linear,gauss,exponential三种,数据离origin越近得分越高
         * 16.  域值因子fieldValueFactorFunction(field).通过字段field里的值提高数据的得分
         */
        return QueryBuilders
                .boolQuery()
                /*1*/	//	.must(QueryBuilders.multiMatchQuery("国经理", "title","summary","author","content").boost(1.0f))
                /*2*/	//	.must(QueryBuilders.multiMatchQuery("事件反腐风暴", "title","summary").type(Type.PHRASE).slop(2))
                /*3*/	//	.must(QueryBuilders.multiMatchQuery("报道", "title","summary").fuzziness("AUTO"))
                /*4*/	//	.must(QueryBuilders.termsQuery("title", "反腐","国家","公司").boost(-0.9f))
                /*5*/	//	.mustNot(QueryBuilders.multiMatchQuery("交易", "title","summary","author","content"))
                /*6*/	//	.must(QueryBuilders.wildcardQuery("author", "*w*"))
                /*7*/ 	//	.must(QueryBuilders.regexpQuery("author", "[a-z]k[a-z]"))
                /*8*/ 	//	.must(QueryBuilders.matchPhrasePrefixQuery("summary","据"))
                /*9*/	//	.should(QueryBuilders.multiMatchQuery("亿元","title","summary","author","content"))
                /*10*/	//	.should(QueryBuilders.multiMatchQuery("严格","title","summary","author","content"))
                /*11*/	//	.should(QueryBuilders.multiMatchQuery("发表","title","summary","author","content"))
                /*12*/	//	.minimumShouldMatch("55%")
                /*13*/	//	.must(QueryBuilders.rangeQuery("publish_time").from("2018-01-13 14:21:09").to("2018-01-17 14:21:09"))//50.45 59.12  1741 1749.66
                /*14*/  //	.must(QueryBuilders.existsQuery("title"))
                /*15*/	//	.must(QueryBuilders.functionScoreQuery(ScoreFunctionBuilders.linearDecayFunction("price",67,20,5,0.5)))
                /*16*/	//	.must(QueryBuilders.functionScoreQuery(ScoreFunctionBuilders.fieldValueFactorFunction("comment").modifier(Modifier.LN1P).factor(0.1f)).boostMode(CombineFunction.SUM))
                ;
    }

    /**
     * @description : 使用前必须先自定义过滤语句,过滤不影响得分,
     */
    private QueryBuilder setFilter(){
        /*
         * 	must		查询子句must出现在文档中，并且会影响文档得分score
         *	filter		查询子句must出现在文档中，但是不会影响文档得分，并且会缓存
         *	should		查询子句should出现在文档中，即并列的should子句必须有一个或者多个出现在文档中，可以通过设置参数 minimum_should_match来指定最少匹配的查询子句
         *	must_not	查询子句一定不能出现在文档中，处于filter上下文，不影响文档得分，会被缓存
         *
         * 1.   加权查询,数据在"title","summary","author","content"四个字段中包含搜索关键词"国经理",并将该次查询分数变成1.5倍。
         * 2.   整句查询,数据在"title","summary"字段中包含搜索关键短语"事件反腐风暴",事件反腐风暴不分词，事件反腐风暴中间可间隔2位
         * 3.   模糊查询,数据在"title","summary"字段中包含搜索关键词"国经理",并将该次查询设置为模糊查询,模糊程度"AUTO"
         * 4.   数据在"title"字段中包含搜索符合关键词"反腐","国家","公司"中任意一个条件，并并将该次查询分数变成-0.9倍
         * 5.   数据不在"title","summary","author","content"任意一个字段内含有关键词"交易"
         * 6.	通配符查询,数据在"author"字段中含有通配符表达式"*w*"，支持？ 匹配任何字符，*匹配零个或多个字符
         * 7.   正则查询,数据在"author"字段中含有正则表达式"[a-z]k[a-z]"
         * 8.	前缀短语查询,数据在"summary"字段中以"据"开头的数据
         * 9-11。	数据在3个should中满足至少一个查询子句
         * 12. 	设置至少要符合多少个条件,参数为百分比，或者整型
         * 13.  数据在"publish_time"字段里的数据在from("2018-01-13 14:21:09")和to("2018-01-17 14:21:09")之间
         */
        return QueryBuilders
                .boolQuery()
                /*1*/	//	.must(QueryBuilders.multiMatchQuery("国经理", "title","summary","author","content").boost(1.5f))
                /*2*/	//	.must(QueryBuilders.multiMatchQuery("事件反腐风暴", "title","summary").type(Type.PHRASE).slop(2))
                /*3*/	//	.must(QueryBuilders.multiMatchQuery("报道", "title","summary").fuzziness("AUTO"))
                /*4*/	//	.must(QueryBuilders.termsQuery("title", "反腐","国家","公司").boost(-0.9f))
                /*5*/	//	.mustNot(QueryBuilders.multiMatchQuery("交易", "title","summary","author","content"))
                /*6*/	//	.must(QueryBuilders.wildcardQuery("author", "*w*"))
                /*7*/ 	//	.must(QueryBuilders.regexpQuery("author", "[a-z]k[a-z]"))
                /*8*/ 	//	.must(QueryBuilders.matchPhrasePrefixQuery("summary","据"))
                /*9*/	//	.should(QueryBuilders.multiMatchQuery("亿元","title","summary","author","content"))
                /*10*/	//	.should(QueryBuilders.multiMatchQuery("严格","title","summary","author","content"))
                /*11*/	//	.should(QueryBuilders.multiMatchQuery("发表","title","summary","author","content"))
                /*12*/	//	.minimumShouldMatch("55%")
                /*13*/	//	.must(QueryBuilders.rangeQuery("publish_time").from("2018-01-13 14:21:09").to("2018-01-17 14:21:09"))
                ;
    }

    /**
     * @description : 使用前必须先自定义高亮设置语句
     */
    private HighlightBuilder setHighlight(){
        HighlightBuilder highlightBuild = new HighlightBuilder()
                // 设置被高亮的字段
                .field("summary").field("title")
                // 设置高亮的标签
                .preTags("<strong>").postTags("</strong>");
        return highlightBuild;
    }

    /**
     * @description : 打印搜索结果
     */
    public void printResponse(){
        response=this.getResponse();
        // timed_out
        boolean isTimedOut = response.isTimedOut();
        System.out.println("查询成功，timed_out:"+isTimedOut);

        // _shards，查询总分片数,查询成功片数,查询失败片数
        int totalShards = response.getTotalShards();
        int successfulShards = response.getSuccessfulShards();
        int failedShards = response.getFailedShards();
        System.out.println("查询分片数:{ total="+totalShards+" successful="+successfulShards+" failed="+failedShards+"},查询到"+response.getHits().totalHits+"条记录");

        // 文档在hit数组中，更多方法使用请看API中SearchHits
        SearchHits searchHits = response.getHits();
        Iterator<SearchHit> iterator = searchHits.iterator();
        while(iterator.hasNext()) {
            SearchHit hit = iterator.next();
            String hitIndex = hit.getIndex();
            String hitType = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();
            System.out.println("index="+hitIndex+" type="+hitType+" id="+id+" score="+score+" source-->"+hit.getSourceAsString());
            System.out.println(hit.getHighlightFields());
        }
        System.out.println("查询结束...");
    }


    /**
     * @description 获取搜索结果,需先修改BasaSearch里的方法setQuery(),setFilter(),setHighlight()
     * @param indexs 索引名称
     * @param types	类型名称
     */
    public void getSearchByIndexAndType(String[] indexs,String[] types) {

        System.out.print("集群中查询索引为"+Arrays.deepToString(indexs)
                +"和类型为"+Arrays.deepToString(types)
                +"的所有数据");
        //查询

        QueryBuilder queryBulid=this.setQuery();
        QueryBuilder filterBulid=this.setFilter();
        HighlightBuilder highlightBuild1=this.setHighlight();

        searchRequestBuilder =
                //设置查询的index，例{"twitter","indexName"}
                client.prepareSearch(indexs)
                        //设置查询的type，例{"tweet","typeName"}
                        .setTypes(types)
                        /* 设置查询类型
                         * 1.SearchType.DFS_QUERY_THEN_FETCH = 最精确
                         * 2.SearchType.QUERY_THEN_FETCH = 比较快
                         */
                        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                        //设置查询
                        .setQuery(queryBulid)
                        //设置过滤器
                        .setPostFilter(filterBulid)
                        //设置获取查询数据的位置
                        .setFrom(0)
                        //设置获取查询数据的数量
                        .setSize(50)
                        //设置是否按查询匹配度排序,很耗资源,只能用作调试工具,千万不要用于生产环境
//              .setExplain(true)
                        //设置显示高亮，
                        .highlighter(highlightBuild1)
        //设置根据"publish_time"排序
//              .addSort("publish_time", SortOrder.DESC)
        ;

        System.out.println("开始查询...");
        //建立查询，并返回结果
        SearchResponse response = searchRequestBuilder.get();
        this.setResponse(response);
    }

    /**
     * @description : 获取发送给服务器的请求的JSON格式
     * @return String
     */
    public String getRequestJSON(){
        return searchRequestBuilder.toString();
    }

    public TransportClient getClient() {
        return client;
    }
    public void setClient(TransportClient client) {
        this.client = client;
    }

    public SearchRequestBuilder getSearchRequestBuilder() {
        return searchRequestBuilder;
    }
    public void setSearchRequestBuilder(SearchRequestBuilder searchRequestBuilder) {
        this.searchRequestBuilder = searchRequestBuilder;
    }

    public SearchResponse getResponse() {
        return response;
    }
    public void setResponse(SearchResponse response) {
        this.response = response;
    }
}