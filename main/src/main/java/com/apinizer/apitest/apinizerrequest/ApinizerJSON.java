package com.apinizer.apitest.apinizerrequest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApinizerJSON implements Serializable {

    @JsonProperty
    public ApinizerCollection baseCollection;

    @JsonProperty
    public HashMap<String, ApinizerRequestTab> openedTabs;

    @JsonProperty
    public List<ApinizerHistory> histories;

    @JsonProperty
    public String tabIndexId;

    public ApinizerJSON(){
        baseCollection = new ApinizerCollection();
        histories = new ArrayList<>();
        openedTabs = new HashMap<>();
    }

    public ApinizerJSON(ApinizerCollection collection, List<ApinizerHistory> history){
        this.baseCollection = collection;
        this.histories = history;
    }

    public ApinizerJSON(ApinizerCollection baseCollection, HashMap<String, ApinizerRequestTab> openedTabs, List<ApinizerHistory> histories) {
        this.baseCollection = baseCollection;
        this.openedTabs = openedTabs;
        this.histories = histories;
    }

    public ApinizerJSON(ApinizerCollection baseCollection, HashMap<String, ApinizerRequestTab> openedTabs, List<ApinizerHistory> histories, String tabIndexId) {
        this.baseCollection = baseCollection;
        this.openedTabs = openedTabs;
        this.histories = histories;
        this.tabIndexId = tabIndexId;
    }

    public ApinizerCollection getBaseCollection() {
        return baseCollection;
    }

    public void setBaseCollection(ApinizerCollection baseCollection) {
        this.baseCollection = baseCollection;
    }

    public HashMap<String, ApinizerRequestTab> getOpenedTabs() {
        return openedTabs;
    }

    public void setOpenedTabs(HashMap<String, ApinizerRequestTab> openedTabs) {
        this.openedTabs = openedTabs;
    }

    public List<ApinizerHistory> getHistories() {
        return histories;
    }

    public void setHistories(List<ApinizerHistory> histories) {
        this.histories = histories;
    }

    public String getTabIndexId() {
        return tabIndexId;
    }

    public void setTabIndexId(String tabIndexId) {
        this.tabIndexId = tabIndexId;
    }

}
