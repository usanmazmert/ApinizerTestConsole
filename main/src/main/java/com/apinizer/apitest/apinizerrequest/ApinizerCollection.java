package com.apinizer.apitest.apinizerrequest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ApinizerCollection implements Serializable {

    private String parentId;
    @JsonProperty
    private String collectionId;
    @JsonProperty
    private String name;
    @JsonProperty
    private HashMap<String, ApinizerCollection> apinizerCollections;
    @JsonProperty
    private HashMap<String, ApinizerRequestTab> apinizerRequestTabs;

    public ApinizerCollection(){
        this.collectionId = UUID.randomUUID().toString();
        this.name = "New Collection";
        this.apinizerRequestTabs = new HashMap<>();
        apinizerCollections = new HashMap<>();
    }

    public ApinizerCollection(String name, String id, String parentId){
        this.parentId = parentId;
        this.collectionId = id;
        this.name = name;
        this.apinizerRequestTabs = new HashMap<>();
        apinizerCollections = new HashMap<>();
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addNewTab(){
        ApinizerRequestTab newTab = new ApinizerRequestTab();
        apinizerRequestTabs.put(newTab.getId(), newTab);
    }

    public HashMap<String, ApinizerCollection> getApinizerCollections() {
        return apinizerCollections;
    }

    public void setApinizerCollections(HashMap<String, ApinizerCollection> apinizerCollections) {
        this.apinizerCollections = apinizerCollections;
    }

    public HashMap<String, ApinizerRequestTab> getApinizerRequestTabs() {
        return apinizerRequestTabs;
    }

    public void setApinizerRequestTabs(HashMap<String, ApinizerRequestTab> apinizerRequestTabs) {
        this.apinizerRequestTabs = apinizerRequestTabs;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}

