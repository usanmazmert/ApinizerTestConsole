package com.apinizer.apitest.apinizerrequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class ApinizerBasicUrlEncoded implements NameValuePair {

    private String name;
    private String value;

    public ApinizerBasicUrlEncoded(){
        name = "";
        value = "";
    }

    public ApinizerBasicUrlEncoded(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
