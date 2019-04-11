package com.qf.shop_search;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopSearchApplicationTests {

    @Autowired
    private SolrClient solrClient;

    /**
     * 添加索引
     *
     * id相同就是修改 id不同就是增加
     *
     */
    @Test
    public void add(){

        //查询数据库 - Goods

        //创建document对象
        SolrInputDocument solrDocument = new SolrInputDocument();
        solrDocument.addField("id", 3);
        solrDocument.addField("gname", "华为手机");
        solrDocument.addField("gimage", "http://www.baidu.com");
        solrDocument.addField("ginfo", "手机中的战斗机");
        solrDocument.addField("gprice", 99.99);
        solrDocument.addField("gsave", 10000);

        try {
            solrClient.add(solrDocument);
            solrClient.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据id删除
     *
     * 根据查询结果删除
     *
     * @throws IOException
     * @throws SolrServerException
     */
    @Test
    public void delete() throws IOException, SolrServerException {
//        solrClient.deleteById("1");
//        solrClient.deleteByQuery("gname:洗衣机");
        solrClient.commit();
    }


    /**
     * 查询
     */
    @Test
    public void query() throws IOException, SolrServerException {
        SolrQuery solrQuery = new SolrQuery();

        String keyword = "手机";//省电 洗衣机 洗衣 机 电

        solrQuery.setQuery("gname:" + keyword + " || ginfo:" + keyword);

        //获得查询结果
        QueryResponse result = solrClient.query(solrQuery);
        SolrDocumentList documentList = result.getResults();

        //循环获得结果
        for(SolrDocument document : documentList){
            String id = (String) document.get("id");
            String gname = (String) document.get("gname");
            String ginfo = (String) document.get("ginfo");
            float gprice = (float) document.get("gprice");
            int gsave = (int) document.get("gsave");
            String gimage = (String) document.get("gimage");

            System.out.println(id + " " + gname + " " + ginfo + " " + gprice + " " + gsave + " " + gimage);
        }
    }


    @Test
    public void contextLoads() {
    }

}
