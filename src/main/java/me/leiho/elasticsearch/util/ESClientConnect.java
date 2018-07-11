package me.leiho.elasticsearch.util;


import me.leiho.elasticsearch.exception.ESServiceException;
import me.leiho.elasticsearch.exception.ESServiceException;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ESClientConnect {

    private TransportClient client;

    public TransportClient getClient() {
        return client;
    }

    /**
     * @param clusterName    集群名称
     * @param clusterAddress 集群ip地址
     * @param port           端口号
     */
    @SuppressWarnings({"unchecked", "resource"})
    public ESClientConnect(String clusterName, String clusterAddress, int port) {
        Settings settings = Settings.builder().put("cluster.name", clusterName).put("client.transport.sniff", true)
                .build();
        try {
            client = new PreBuiltTransportClient(settings).addTransportAddress(new TransportAddress(InetAddress.getByName(clusterAddress), port));
        } catch (UnknownHostException e) {
            throw new ESServiceException("客户端初始化错误");
        }
    }
}


