package com.wjh.utils;

import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import java.util.HashMap;
import java.util.Map;

/**
 * @Classname DingDingMessage
 * @Description 钉钉发送服务
 * @Date 2020/2/24 上午10:52
 * @Created by wjh
 */
@Slf4j
public class DingDingMessage {
    /**
     * 发送markdown信息
     * @param title 首屏会话透出的展示内容
     * @param text 信息内容
     * @param phoneArray 需要@人员的手机号集合
     * @param webHook 群机器人的标识
     * @param isAtAll 是否@所有人
     */
    public static void sendMarkdown(String title, StringBuffer text, String[] phoneArray , String webHook, boolean isAtAll){
        Map<String, Object> at = new HashMap<>();
        at.put("isAtAll", isAtAll);
        if (ArrayUtils.isNotEmpty(phoneArray)) {
            at.put("atMobiles", phoneArray);
            text.append("\n");
            for (String phone: phoneArray) {
                text = text.append("@").append(phone);
            }
        }
        Map<String, String> body = new HashMap<>();
        body.put("title", title);
        body.put("text", text.toString());
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("msgtype", "markdown");
        paramMap.put("markdown", body);
        paramMap.put("at", at);
        String json = JSON.toJSONString(paramMap);
        String url = "https://oapi.dingtalk.com/robot/send";
        HttpPost post = new HttpPost(url + "?access_token=" + webHook);
        post.setEntity(new StringEntity(json, "UTF-8"));
        post.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        try (CloseableHttpClient client = HttpClients.createDefault();) {
            CloseableHttpResponse response = client.execute(post);
            log.info("statusCode={}", response.getStatusLine().getStatusCode());
        } catch (Exception e) {
            log.error("发送钉钉消息异常，titile={}", title, e);
        }
    }
}
