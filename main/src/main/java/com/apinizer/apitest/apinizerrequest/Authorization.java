package com.apinizer.apitest.apinizerrequest;

import java.io.Serializable;
import java.util.Base64;

public class Authorization implements Serializable {
    private String method;
    private String key;
    private String value;
    private String username;
    private String password;
    private String token;

    public Authorization() {
        method = "No Auth";
        key = "";
        value = "";
        username = "";
        password = "";
        token = "";
    }


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

