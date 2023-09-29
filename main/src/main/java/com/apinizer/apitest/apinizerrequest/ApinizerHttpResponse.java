package com.apinizer.apitest.apinizerrequest;

import com.apinizer.apitest.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.beans.Transient;
import java.io.Serializable;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.List;

public class ApinizerHttpResponse implements Serializable {

    private int statusCode;
    private String statusMessage;
    private long responseTime;
    private String responseString;
    private Header[] headers;

    public ApinizerHttpResponse() {
    }


    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public String getResponseString() {
        return responseString;
    }

    public String getFormattedResponseString() {
        return Utils.formatJsonString(responseString);
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }


    public String toString() {
        return Utils.toJSONString(this);
    }

    @Transient
    public String getHumanReadableStringContentSize() {
        if (StringUtils.isBlank(this.responseString)) {
            return "0 B";
        } else {
            long bytes = this.responseString.length();
            long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
            if (absB < 1024) {
                return bytes + " B";
            }
            long value = absB;
            CharacterIterator ci = new StringCharacterIterator("KMGTPE");
            for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
                value >>= 10;
                ci.next();
            }
            value *= Long.signum(bytes);
            return String.format("%.1f %ciB", value / 1024.0, ci.current());
        }
    }

    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 400;
    }
}
