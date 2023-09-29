package com.apinizer.apitest.apinizerrequest;

import com.apinizer.apitest.CustomTreeItem;
import com.apinizer.apitest.util.Utils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.control.TreeItem;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class ApinizerRequestTab implements Serializable {

    private String id;
    private String name;
    private String parentFolderId;
    private ApinizerHttpRequest apinizerHttpRequest;
    @JsonIgnore
    private ApinizerHttpResponse apinizerHttpResponse;

    private Stack<String> idStack;

    private Authorization auth;

    public ApinizerRequestTab(){
        this.id = UUID.randomUUID().toString();
        this.parentFolderId = parentFolderId;
        this.name = "New Request";
        apinizerHttpRequest = ApinizerHttpRequest.createSimpleRequest("", new ArrayList<>(), "",
                EnumHttpRequestMethod.GET, 10, null);
        idStack = new Stack<>();
        auth = new Authorization();
    }
    public ApinizerRequestTab(String name, String id, String parentId){
        this.id = id;
        this.name = name;
        apinizerHttpRequest = ApinizerHttpRequest.createSimpleRequest("", new ArrayList<>(), "",
                EnumHttpRequestMethod.GET, 10, null);
        this.parentFolderId = parentId;
        idStack = new Stack<>();
        auth = new Authorization();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ApinizerHttpRequest getApinizerHttpRequest() {
        return apinizerHttpRequest;
    }

    public void setApinizerHttpRequest(ApinizerHttpRequest apinizerHttpRequest) {
        this.apinizerHttpRequest = apinizerHttpRequest;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentFolderId() {
        return parentFolderId;
    }

    public void setParentFolderId(String parentFolderId) {
        this.parentFolderId = parentFolderId;
    }

    public ApinizerHttpResponse getApinizerHttpResponse() {
        return apinizerHttpResponse;
    }

    public void setApinizerHttpResponse(ApinizerHttpResponse apinizerHttpResponse) {
        this.apinizerHttpResponse = apinizerHttpResponse;
    }

    public Stack<String> getIdStack() {
        return idStack;
    }

    public void setIdStack(Stack<String> idStack) {
        this.idStack = idStack;
    }

    public Authorization getAuth() {
        return auth;
    }

    public void setAuth(Authorization auth) {
        this.auth = auth;
    }
}
