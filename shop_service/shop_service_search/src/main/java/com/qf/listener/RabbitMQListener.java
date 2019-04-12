package com.qf.listener;

import com.qf.entity.Goods;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RabbitMQListener {

    @Autowired
    private SolrClient solrClient;

    @RabbitListener(queues = "goods_queue1")
    public void handleMsg(Goods goods){
        //接收到MQ的消息
        System.out.println("接收到MQ的消息：" + goods);

        //同步索引库
        SolrInputDocument document = new SolrInputDocument();
        document.setField("id", goods.getId());
        document.setField("gname", goods.getGname());
        document.setField("ginfo", goods.getGinfo());
        document.setField("gprice", goods.getGprice().doubleValue());
        document.setField("gsave", goods.getGsave());
        document.setField("gimage", goods.getGimage());

        try {
            solrClient.add(document);
            solrClient.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
