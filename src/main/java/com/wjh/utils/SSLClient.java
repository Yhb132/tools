package com.wjh.utils;

import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

public class SSLClient {

    public static CloseableHttpClient createSSLClientDefault() {
        try {
            //调用SSL之前取消重写验证方法，取消检测SSL
            X509TrustManager trustManager = new X509TrustManager() {
                //检查客户端证书
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
                }

                //检查服务器证书
                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
                }

                //返回受信任的X509证书数组
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            //设置TLS-->数据通信之间保证数据的保密性和完整性
            SSLContext ctx = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
            //初始化
            ctx.init(null, new TrustManager[]{trustManager}, null);
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(ctx, NoopHostnameVerifier.INSTANCE);
            RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).setExpectContinueEnabled(Boolean.TRUE).setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST)).setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
            //创建Registry
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()

                    .register("http", PlainConnectionSocketFactory.INSTANCE).register("https", socketFactory).build();
            //创建ConnectionManager,添加Connection配置信息
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig).build();
            return httpClient;
        } catch (NoSuchAlgorithmException e) {
            new RuntimeException(e);
        } catch (KeyManagementException e) {
            new RuntimeException(e);
        }
        return HttpClients.createDefault();
    }
}
