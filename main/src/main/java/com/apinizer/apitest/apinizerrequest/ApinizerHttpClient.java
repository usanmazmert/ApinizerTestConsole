package com.apinizer.apitest.apinizerrequest;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.util.Date;

public class ApinizerHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(ApinizerHttpClient.class);

    private ApinizerHttpRequest apinizerRequest;

    public ApinizerHttpClient(ApinizerHttpRequest apinizerRequest) {
        this.apinizerRequest = apinizerRequest;
    }

    public ApinizerHttpResponse callRestService() {
        long startTime = new Date().getTime();
        ApinizerHttpResponse apinizerResponse = new ApinizerHttpResponse();
        HttpRequestBase request = null;
        HttpResponse response = null;

        try {
            request = apinizerRequest.createHttpRequest();
            CloseableHttpClient httpClient = apinizerRequest.initHttpClient();
            response = httpClient.execute(request);
            int statusCode;
            if (response != null) {
                statusCode = response.getStatusLine().getStatusCode();
                apinizerResponse.setStatusCode(statusCode);
                apinizerResponse.setStatusMessage(response.getStatusLine().getReasonPhrase());
                apinizerResponse.setHeaders(response.getAllHeaders());
                if (response.getEntity() != null) {
                    String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                    apinizerResponse.setResponseString(result);
                }
            }
            return apinizerResponse;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            apinizerResponse.setStatusCode(503);
            // apinizerResponse.setStatusMessage(UtilCommon.getStackTrace(e));
            apinizerResponse.setResponseString(e.getMessage());
            return apinizerResponse;
        } finally {
            if (request != null) {
                try {
                    request.releaseConnection();
                } catch (Exception e) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(e.getMessage(), e);
                    }
                }
            }

            try {
                if (response != null && response.getEntity() != null) {
                    EntityUtils.consume(response.getEntity());
                }
            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.debug(e.getMessage(), e);
                }
            }

            long endTime = new Date().getTime();
            apinizerResponse.setResponseTime(endTime - startTime);


        }
    }


    public static void main(String[] args) throws IOException {
       /* testHttpsUrl("https://expired.badssl.com");
        testHttpsUrl("https://wrong.host.badssl.com");
        testHttpsUrl("https://self-signed.badssl.com");
        testHttpsUrl("https://untrusted-root.badssl.com");
        testHttpsUrl("https://revoked.badssl.com");
        testHttpsUrl("https://pinning-test.badssl.com");

        testHttpsUrl("https://no-common-name.badssl.com/");
        testHttpsUrl("https://no-subject.badssl.com/");
        testHttpsUrl("https://incomplete-chain.badssl.com//");

        testHttpsUrl("https://1000-sans.badssl.com/");
        testHttpsUrl("https://10000-sans.badssl.com/");

        testHttpsUrl("https://ecc256.badssl.com/");
        testHttpsUrl("https://ecc384.badssl.com/");


        testHttpsUrl("https://rsa2048.badssl.com/");
        testHttpsUrl("https://rsa4096.badssl.com/");
        testHttpsUrl("https://rsa8192.badssl.com/");

        testHttpsUrl("https://extended-validation.badssl.com/");

        testHttpsUrl("https://sha1-intermediate.badssl.com");
        */

        //testHttpsUrl("https://mailserv.baehal.com/");//sslv3 hatali
        testHttpsUrl("https://tls-v1-0.badssl.com:1010");
        testHttpsUrl("https://tls-v1-1.badssl.com:1011/");
        testHttpsUrl("https://tls-v1-2.badssl.com:1012/");
/*
       testHttpsUrl("https://client.badssl.com/");
        testHttpsUrl("https://client-cert-missing.badssl.com/");

        testHttpsUrl("https://mixed-script.badssl.com/");
        testHttpsUrl("https://very.badssl.com/");

        testHttpsUrl("http://http.badssl.com/");
        testHttpsUrl("https://null.badssl.com/");
        testHttpsUrl("https://3des.badssl.com/");
        testHttpsUrl("https://rc4.badssl.com/");
        testHttpsUrl("https://rc4-md5.badssl.com/");
        testHttpsUrl("https://cbc.badssl.com/");

        testHttpsUrl("https://dh480.badssl.com/");
        testHttpsUrl("https://dh512.badssl.com/");
        testHttpsUrl("https://dh1024.badssl.com/");
        testHttpsUrl("https://no-sct.badssl.com/"); */

    }

    private static void testHttpsUrl(String url) throws IOException {
        ApinizerHttpResponse apinizerResponse = new ApinizerHttpClient(
                ApinizerHttpRequest.createSimpleRequest(url, null, null, EnumHttpRequestMethod.GET, 10, null)).callRestService();

        int statusCode = apinizerResponse.getStatusCode();

        System.out.println(url + ":" + statusCode);
    }


}
