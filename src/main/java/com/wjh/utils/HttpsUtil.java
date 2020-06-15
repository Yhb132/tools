package com.wjh.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * @Classname HttpsUtil
 * @Description
 * @Date 2020/6/15 下午5:20
 * @Created by wjh
 */
@Slf4j
public class HttpsUtil {

    static final String CHAR_SET = "UTF-8";

    public static String sendPost(String url, String jsondata) {
        log.info("[HttpsUtil-post] post url : {} , jsondata : {}", url, jsondata);
        CloseableHttpClient client = SSLClient.createSSLClientDefault();
        // 创建httppost实例
        HttpPost httpPost = new HttpPost(url);
        //添加请求头
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
        CloseableHttpResponse resp = null;
        try {
            httpPost.setEntity(new StringEntity(jsondata, CHAR_SET));
            resp = client.execute(httpPost);
            //  获取响应entity
            HttpEntity respEntity = resp.getEntity();
            String result = EntityUtils.toString(respEntity, CHAR_SET);
            log.info("post method result: {}", result);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                JSONObject obj = JSONObject.parseObject(result);
                Integer errcode = obj.getInteger("code");
                return obj.getString("message");
            } else {
                log.error("post method failed: {}", resp.getStatusLine());
                return"HttpUtil获取数据失败";
            }
        } catch (Exception e) {
            log.error("[HttpsUtil-post] error: url=" + url + "\r\n post content=" + jsondata + "\r\n , exception=" + e);
        } finally {
            if (httpPost != null) {
                httpPost.releaseConnection();
            }
        }
        return "Success";
    }

    public static String sendGet(String url) {
        CloseableHttpClient client = SSLClient.createSSLClientDefault();
        // 创建httppost实例
        HttpGet httpGet = new HttpGet(url);
        //添加请求头
        httpGet.addHeader("Content-Type", "application/json;charset=utf-8");
        CloseableHttpResponse resp = null;
        try {
            resp = client.execute(httpGet);
            //  获取响应entity
            HttpEntity respEntity = resp.getEntity();
            String result = EntityUtils.toString(respEntity, CHAR_SET);

            int statusCode = resp.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                JSONObject obj = JSONObject.parseObject(result);
                Integer errcode = obj.getInteger("errcode");
                return obj.getString("errmsg");
            } else {
                log.error("post method failed: {}", resp.getStatusLine());
                return "HttpUtil获取数据失败";
            }
        } catch (Exception e) {
            log.error("[HttpsUtil-post] error: url=" + url + "\r\n , exception=" + e);
        } finally {
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
        return "Success";
    }
}
