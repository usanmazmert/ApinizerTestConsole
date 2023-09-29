package com.apinizer.apitest.apinizerrequest;

import org.apache.http.NameValuePair;

import java.io.Serializable;
import java.util.jar.Attributes;

public class ApinizerBasicParameter implements Serializable, NameValuePair {
    private static final long serialVersionUID = 1L;

    private String name;
    private String value;

    public ApinizerBasicParameter() {

    }

    public ApinizerBasicParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public ApinizerBasicParameter name(String name) {
        this.name = name;
        return this;
    }

    public ApinizerBasicParameter value(String value) {
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
