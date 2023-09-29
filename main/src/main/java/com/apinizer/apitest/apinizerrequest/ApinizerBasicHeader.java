package com.apinizer.apitest.apinizerrequest;

import org.apache.http.NameValuePair;

import java.io.Serializable;

public class ApinizerBasicHeader implements Serializable, NameValuePair {
    private static final long serialVersionUID = 1L;

    private String name;
    private String value;

    public ApinizerBasicHeader() {

    }

    public ApinizerBasicHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public ApinizerBasicHeader name(String name) {
        this.name = name;
        return this;
    }

    public ApinizerBasicHeader value(String value) {
        this.value = value;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
