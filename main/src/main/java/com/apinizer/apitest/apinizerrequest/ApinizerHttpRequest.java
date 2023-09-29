package com.apinizer.apitest.apinizerrequest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.message.BasicHeader;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.ProxySelector;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.*;

public class ApinizerHttpRequest implements Serializable {
    private static final String[] supportedProtocols = new String[]{
            "TLSv1.3",
            "TLSv1.2",
            "TLSv1.1",
            "TLSv1",
            "SSLv3",
            "SSLv2Hello" };

    private static SSLContext disabledSslContext = null;

    static {
        try {
            disabledSslContext = SSLContext.getInstance("TLSv1.3");
            // set up a TrustManager that trusts everything
            disabledSslContext.init(null, new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }}, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public static SSLContext getDisabledSslContext() {
        return disabledSslContext;
    }

    private String url;
    private List<ApinizerBasicHeader> headerList;

    private List<ApinizerBasicParameter> parameterList;
    private String body;
    private List<NameValuePair> nameValuePairList;//x-www-form-urlencoded ise
    private List<ApinizerRequestBodyPart> bodyPartList;//multipart form request ise

    private EnumHttpRequestMethod httpMethod;
    private Double timeoutInSeconds;
    @JsonIgnore
    private SSLContext sslContext;

    public ApinizerHttpRequest() {

    }

    private ApinizerHttpRequest(String url, List<ApinizerBasicHeader> headerList, String body, EnumHttpRequestMethod httpMethod, Double timeoutInSeconds, SSLContext sslContext) {
        this.url = url;
        this.headerList = headerList;
        this.body = body;
        this.httpMethod = httpMethod;
        this.timeoutInSeconds = timeoutInSeconds;
        this.sslContext = sslContext;
    }

    private static void clearContentLengthHeader(List<ApinizerBasicHeader> headerList) {
        if (CollectionUtils.isNotEmpty(headerList)) {
            for (Iterator<ApinizerBasicHeader> it = headerList.iterator(); it.hasNext(); ) {
                ApinizerBasicHeader o = it.next();
                if (Constants.HEADER_KEY_CONTENTLENGTH.equals(o.getName())) {
                    it.remove();
                    return;
                }
            }
        }
    }

    public static ApinizerHttpRequest createSimpleRequest(String url, List<ApinizerBasicHeader> headerList, String body, EnumHttpRequestMethod httpMethod, Integer timeoutInSeconds, SSLContext sslContext) {
        clearContentLengthHeader(headerList);
        ApinizerHttpRequest apinizerRequest = new ApinizerHttpRequest(url, headerList, body, httpMethod, timeoutInSeconds == null ? null : timeoutInSeconds.doubleValue(), sslContext);
        return apinizerRequest;
    }

    public static ApinizerHttpRequest createSimpleRequest(String url, List<ApinizerBasicHeader> headerList, String body, EnumHttpRequestMethod httpMethod, Double timeoutInSeconds, SSLContext sslContext) {
        clearContentLengthHeader(headerList);
        ApinizerHttpRequest apinizerRequest = new ApinizerHttpRequest(url, headerList, body, httpMethod, timeoutInSeconds, sslContext);
        return apinizerRequest;
    }

    public static ApinizerHttpRequest createFormUrlEncodedRequest(String url, List<ApinizerBasicHeader> headerList, List<NameValuePair> nameValuePairList, EnumHttpRequestMethod httpMethod, Integer timeoutInSeconds, SSLContext sslContext) {
        clearContentLengthHeader(headerList);
        ApinizerHttpRequest apinizerRequest = ApinizerHttpRequest.createSimpleRequest(url, headerList, null, httpMethod, timeoutInSeconds, sslContext);
        apinizerRequest.setNameValuePairList(nameValuePairList);
        return apinizerRequest;
    }

    public static ApinizerHttpRequest createFormUrlEncodedRequest(String url, List<ApinizerBasicHeader> headerList, List<NameValuePair> nameValuePairList, EnumHttpRequestMethod httpMethod, Double timeoutInSeconds, SSLContext sslContext) {
        clearContentLengthHeader(headerList);
        ApinizerHttpRequest apinizerRequest = ApinizerHttpRequest.createSimpleRequest(url, headerList, null, httpMethod, timeoutInSeconds, sslContext);
        apinizerRequest.setNameValuePairList(nameValuePairList);
        return apinizerRequest;
    }

    public static ApinizerHttpRequest createBodyPartRequest(String url, List<ApinizerBasicHeader> headerList, List<ApinizerRequestBodyPart> bodyPartList, EnumHttpRequestMethod httpMethod, Integer timeoutInSeconds, SSLContext sslContext) {
        clearContentLengthHeader(headerList);
        ApinizerHttpRequest apinizerRequest = ApinizerHttpRequest.createSimpleRequest(url, headerList, null, httpMethod, timeoutInSeconds, sslContext);
        apinizerRequest.setBodyPartList(bodyPartList);
        return apinizerRequest;
    }

    public static ApinizerHttpRequest createBodyPartRequest(String url, List<ApinizerBasicHeader> headerList, List<ApinizerRequestBodyPart> bodyPartList, EnumHttpRequestMethod httpMethod, Double timeoutInSeconds, SSLContext sslContext) {
        clearContentLengthHeader(headerList);
        ApinizerHttpRequest apinizerRequest = ApinizerHttpRequest.createSimpleRequest(url, headerList, null, httpMethod, timeoutInSeconds, sslContext);
        apinizerRequest.setBodyPartList(bodyPartList);
        return apinizerRequest;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<ApinizerBasicHeader> getHeaderList() {
        return headerList;
    }

    private Header[] getHeaderArr() {
        Header[] headersArray;
        List<BasicHeader> basicHeaderList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(headerList)) {
            for (ApinizerBasicHeader apinizerBasicHeader : headerList) {
                BasicHeader basicHeader = new BasicHeader(apinizerBasicHeader.getName(), apinizerBasicHeader.getValue());
                basicHeaderList.add(basicHeader);
            }
            headersArray = new Header[basicHeaderList.size()];
            headersArray = basicHeaderList.toArray(headersArray);
        } else {
            headersArray = new Header[0];
        }
        return headersArray;
    }

    public void setHeaderList(List<ApinizerBasicHeader> headerList) {
        this.headerList = headerList;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public EnumHttpRequestMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(EnumHttpRequestMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Double getTimeoutInSeconds() {
        return timeoutInSeconds;
    }

    public void setTimeoutInSeconds(Double timeoutInSeconds) {
        this.timeoutInSeconds = timeoutInSeconds;
    }

    public SSLContext getSslContext() {
        return sslContext;
    }

    public void setSslContext(SSLContext sslContext) {
        this.sslContext = sslContext;
    }

    public List<NameValuePair> getNameValuePairList() {
        return nameValuePairList;
    }

    public void setNameValuePairList(List<NameValuePair> nameValuePairList) {
        this.nameValuePairList = nameValuePairList;
    }

    public List<ApinizerRequestBodyPart> getBodyPartList() {
        return bodyPartList;
    }

    public void setBodyPartList(List<ApinizerRequestBodyPart> bodyPartList) {
        this.bodyPartList = bodyPartList;
    }

    private boolean containsXWWWFormUrlEncoded() {
        if (CollectionUtils.isNotEmpty(headerList)) {
            for (ApinizerBasicHeader apinizerBasicHeader : headerList) {
                if (StringUtils.isNotBlank(apinizerBasicHeader.getName()) &&
                        StringUtils.isNotBlank(apinizerBasicHeader.getValue()) &&
                        apinizerBasicHeader.getName().equals(Constants.HEADER_KEY_CONTENTTYPE) &&
                        apinizerBasicHeader.getValue().contains(Constants.HEADER_VALUE_XWWWFORMURLENCODED)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Map<String, String> createEncodedQueryPairsFromQueryString(Map<String, String[]> parameterMap, String queryStr) throws UnsupportedEncodingException {
        String[] pairs = queryStr.split("&");
        Map<String, String> queryPairs = new LinkedHashMap<>();

        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            if (idx > 0) {
                String key = pair.substring(0, idx);

                key = URLDecoder.decode(key, "UTF-8");

                String value;
                if (parameterMap == null) {
                    value = pair.substring(idx + 1);
                } else if (parameterMap.get(key) == null) {
                    value = "";
                } else {
                    //value queryStr'de encoded gelebilir, ham halini alıyoruz
                    value = StringUtils.join(parameterMap.get(key), ","); //
                }

                value = URLEncoder.encode(value, "UTF-8");

                key = URLEncoder.encode(key, "UTF-8");

                queryPairs.put(key, value);
            } else {
                queryPairs.put(pair, null);
            }
        }
        return queryPairs;
    }


    public static String createUrlFromQueryPairs(Map<String, String> queryPairs) {
        String queryStr = "?";
        for (String key : queryPairs.keySet()) {
            if (queryPairs.get(key) == null) {
                queryStr = queryStr + key + "&";
            } else {
                queryStr = queryStr + key + "=" + queryPairs.get(key) + "&";
            }
        }
        queryStr = StringUtils.removeEnd(queryStr, "&");
        return queryStr;
    }

    public HttpRequestBase createHttpRequest() throws UnsupportedEncodingException {


        if (url.contains("?")) {
            String queryStr = url.substring(url.indexOf("?") + 1);
            url = url.substring(0, url.indexOf("?"));

            Map<String, String> queryPairs = createEncodedQueryPairsFromQueryString(null, queryStr);
            queryStr = createUrlFromQueryPairs(queryPairs);

            url = url + queryStr;
        }

        switch (httpMethod) {
            case GET:
                if (StringUtils.isNoneEmpty(body)) {
                    ApinizerRequestHttpGetWithEntity getHttpMethod = new ApinizerRequestHttpGetWithEntity(url);
                    setDefaultMethodHeaders(getHttpMethod, getHeaderArr());
                    getHttpMethod.setEntity(new ByteArrayEntity(body.getBytes(StandardCharsets.UTF_8)));
                    return getHttpMethod;
                } else {
                    HttpGet getHttpMethod = new HttpGet(url);
                    setDefaultMethodHeaders(getHttpMethod, getHeaderArr());
                    return getHttpMethod;
                }
            case POST:
                HttpPost postHttpMethod = new HttpPost(url);
                setDefaultMethodHeaders(postHttpMethod, getHeaderArr());
                if (CollectionUtils.isNotEmpty(bodyPartList)) {
                    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                    for (ApinizerRequestBodyPart bodyPart : bodyPartList) {
                        if (bodyPart.getBodyPartType().isBinary()) {
                            ApinizerRequestBinaryBody binaryBodyPart = (ApinizerRequestBinaryBody) bodyPart;
                            builder.addBinaryBody(binaryBodyPart.getName(), binaryBodyPart.getContent(), binaryBodyPart.getContentType(), binaryBodyPart.getFileName());
                        } else {
                            ApinizerRequestTextBody textBodyPart = (ApinizerRequestTextBody) bodyPart;
                            builder.addTextBody(textBodyPart.getName(), textBodyPart.getText(), textBodyPart.getContentType());
                        }
                    }
                    HttpEntity entity = builder.build();
                    postHttpMethod.setEntity(entity);
                } else if (containsXWWWFormUrlEncoded() && CollectionUtils.isNotEmpty(nameValuePairList)) {
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairList, Consts.UTF_8);
                    postHttpMethod.setEntity(entity);
                } else if (StringUtils.isNoneEmpty(body)) {
                    postHttpMethod.setEntity(new StringEntity(body, Consts.UTF_8));
                }
                return postHttpMethod;
            case PUT:
                HttpPut putHttpMethod = new HttpPut(url);
                setDefaultMethodHeaders(putHttpMethod, getHeaderArr());
                if (StringUtils.isNoneEmpty(body)) {
                    putHttpMethod.setEntity(new ByteArrayEntity(body.getBytes(StandardCharsets.UTF_8)));

                }
                return putHttpMethod;
            case HEAD:
                HttpHead headHttpMethod = new HttpHead(url);
                setDefaultMethodHeaders(headHttpMethod, getHeaderArr());
                return headHttpMethod;
            case OPTIONS:
                HttpOptions optionsHttpMethod = new HttpOptions(url);
                setDefaultMethodHeaders(optionsHttpMethod, getHeaderArr());
                return optionsHttpMethod;
            case DELETE:
                if (StringUtils.isNoneEmpty(body)) {
                    HttpDeleteWithEntity deleteHttpMethod = new HttpDeleteWithEntity(url);
                    setDefaultMethodHeaders(deleteHttpMethod, getHeaderArr());
                    deleteHttpMethod.setEntity(new ByteArrayEntity(body.getBytes(StandardCharsets.UTF_8)));
                    return deleteHttpMethod;
                } else {
                    HttpDelete deleteHttpMethod = new HttpDelete(url);
                    setDefaultMethodHeaders(deleteHttpMethod, getHeaderArr());
                    return deleteHttpMethod;
                }
            case PATCH:
                HttpPatch patchHttpMethod = new HttpPatch(url);
                setDefaultMethodHeaders(patchHttpMethod, getHeaderArr());
                return patchHttpMethod;
            case TRACE:
                HttpTrace traceHttpMethod = new HttpTrace(url);
                setDefaultMethodHeaders(traceHttpMethod, getHeaderArr());
                return traceHttpMethod;
            default:
                break;
        }
        return null;
    }

    private void setDefaultMethodHeaders(HttpRequestBase httpMethod, Header[] headersArray) {
        httpMethod.setHeader("Connection", "close");
        httpMethod.setHeaders(headersArray);
        httpMethod.removeHeaders(Constants.HEADER_KEY_ACCEPTENCODING);
        httpMethod.removeHeaders(Constants.HEADER_KEY_TRANSFERENCODING);
    }


    public HttpClientBuilder createDefaultHttpClientBuilder(){
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        RegistryBuilder registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
        registryBuilder.register("http", new PlainConnectionSocketFactory());

        SSLContext sslContext;
        if (getSslContext() == null) {
            sslContext = disabledSslContext;
        } else {
            sslContext = getSslContext();
        }

        SSLConnectionSocketFactory sslConnectionFactory
                = new SSLConnectionSocketFactory(sslContext.getSocketFactory(), supportedProtocols, null, NoopHostnameVerifier.INSTANCE);


        httpClientBuilder.setSSLSocketFactory(sslConnectionFactory);
        registryBuilder.register("https", sslConnectionFactory);


        Registry registry = registryBuilder.build();
        httpClientBuilder.setConnectionManager(new BasicHttpClientConnectionManager(registry));
        httpClientBuilder.disableContentCompression();
        return httpClientBuilder;
    }

    public CloseableHttpClient initHttpClient() {

        HttpClientBuilder httpClientBuilder = createDefaultHttpClientBuilder();

        if (getTimeoutInSeconds() != null) {
            Double timeout = getTimeoutInSeconds() * 1000; // dont change methoda parametre olarak saniye gelecek
            int timeoutInt = timeout.intValue();
            RequestConfig.Builder requestBuilder = RequestConfig.custom();
            requestBuilder = requestBuilder.setConnectTimeout(timeoutInt);
            requestBuilder = requestBuilder.setConnectionRequestTimeout(timeoutInt);
            requestBuilder = requestBuilder.setSocketTimeout(timeoutInt);
            httpClientBuilder.setDefaultRequestConfig(requestBuilder.build());
        }

        CloseableHttpClient httpClient = httpClientBuilder.
                setRoutePlanner(new SystemDefaultRoutePlanner(ProxySelector.getDefault()))
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();
        return httpClient;

    }

    public List<ApinizerBasicParameter> getParameterList() {
        return parameterList;
    }

    public void setParameterList(List<ApinizerBasicParameter> parameterList) {
        this.parameterList = parameterList;
    }
}
