package me.leiho.elasticsearch.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ElasticSearchConfiguration extends AbstractFactoryBean {

    @Value("${spring.data.elasticsearch.cluster-nodes}")
    private String clusterNodes;
    @Value("${spring.data.elasticsearch.cluster-name}")
    private String clusterName;
    @Value("${elasticsearch.url}")
    private String url;
    @Value("${elasticsearch.port}")
    private Integer port;
    @Value("${elasticsearch.scheme}")
    private String scheme;

    private RestHighLevelClient restHighLevelClient;

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public void destroy() throws Exception {
        try {
            if (restHighLevelClient != null) {
                restHighLevelClient.close();
            }
        } catch (final Exception e) {
            log.error("关闭ElasticSearch客户端出错: ", e);
        }
    }

    @Override
    public Class<RestHighLevelClient> getObjectType() {
        return RestHighLevelClient.class;
    }

    @Override
    protected Object createInstance() throws Exception {
        return buildClient();
    }

    private RestHighLevelClient buildClient() {
        try {
            restHighLevelClient = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost(url, port, scheme)
//                            ,new HttpHost("localhost", 9201, "http")
                    )
            );
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return restHighLevelClient;
    }
}
