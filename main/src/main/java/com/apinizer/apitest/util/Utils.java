package com.apinizer.apitest.util;


import com.apinizer.apitest.Controller;
import com.apinizer.apitest.CustomTreeItem;
import com.apinizer.apitest.apinizerrequest.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.scene.control.TreeItem;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.*;

public class Utils {

    public static ApinizerJSON apinizerJSON = new ApinizerJSON();
    private static ObjectMapper deserializerMapper = new ObjectMapper();
    private static ObjectMapper serializerMapper = new ObjectMapper();

    public static <T> T fromJSONString(String jsonString, Class<T> type) {
        if (jsonString == null || jsonString.isEmpty()) {
            return null;
        }

        T obj;
        try {
            obj = deserializerMapper.readValue(jsonString, type);

        } catch (Exception e) {
            e.printStackTrace();
            obj = null;
        }
        return obj;
    }
    public static String formatJsonString(String jsonValue) {
        if (StringUtils.isEmpty(jsonValue)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            Object jsonObject = mapper.readValue(jsonValue, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        } catch (com.fasterxml.jackson.core.JsonParseException ex) {
            return jsonValue;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String toJSONString(Object entity) {
        if (entity == null) {
            return "";
        }
        String json;
        // generate json
        try {
            json = serializerMapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            json = "";
            e.printStackTrace();
        }
        return json;
    }

    public static void wrapTreeViewItems(TreeItem<CustomTreeItem> item, ApinizerCollection parent) {
        if (item != null) {
            if(item.getValue().isIsDirectory()){
                ApinizerCollection newCollection;
                if(item.getValue().getValue().equals("root")){
                    newCollection = parent;
                    newCollection.setCollectionId(item.getValue().getId());
                    newCollection.setName(item.getValue().getValue());
                }else{
                    newCollection  = new ApinizerCollection(item.getValue().getValue(), item.getValue().getId(), parent.getCollectionId());
                    parent.getApinizerCollections().put(newCollection.getCollectionId(), newCollection);
                }
                for (TreeItem<CustomTreeItem> childItem : item.getChildren()) {
                    wrapTreeViewItems(childItem, newCollection);
                }
                return;
            }
            ApinizerRequestTab newTab = new ApinizerRequestTab(item.getValue().getValue(), item.getValue().getId(), parent.getCollectionId());
            parent.getApinizerRequestTabs().put(newTab.getId(), newTab);
        }
    }

    public static void unwrapCollectionItems(ApinizerCollection parent, TreeItem<CustomTreeItem> parentItem) {
        if (parent != null && parentItem != null) {
            if (!parent.getApinizerCollections().isEmpty()) {
                for (ApinizerCollection collection : parent.getApinizerCollections().values()) {
                    TreeItem<CustomTreeItem> collectionItem = new TreeItem<>(
                            new CustomTreeItem(collection.getCollectionId(), collection.getName(), true)
                    );
                    parentItem.getChildren().add(collectionItem);
                    unwrapCollectionItems(collection, collectionItem); // Recursively unwrap collections
                }
            }

            if (!parent.getApinizerRequestTabs().isEmpty()) {
                for (ApinizerRequestTab tab : parent.getApinizerRequestTabs().values()) {
                    TreeItem<CustomTreeItem> tabItem = new TreeItem<>(
                            new CustomTreeItem(tab.getId(), tab.getName(), false)
                    );
                    parentItem.getChildren().add(tabItem);
                }
            }
        }
    }

    public static void wrapHistoryItems(TreeItem<CustomTreeItem> item, ApinizerCollection parent) {
        if (item != null) {
            if(item.getValue().isIsDirectory()){
                ApinizerCollection newCollection;
                if(item.getValue().getValue().equals("root")){
                    newCollection = parent;
                    newCollection.setCollectionId(item.getValue().getId());
                    newCollection.setName(item.getValue().getValue());
                }else{
                    newCollection  = new ApinizerCollection(item.getValue().getValue(), item.getValue().getId(), parent.getCollectionId());
                    parent.getApinizerCollections().put(newCollection.getCollectionId(), newCollection);
                }
                for (TreeItem<CustomTreeItem> childItem : item.getChildren()) {
                    wrapHistoryItems(childItem, newCollection);
                }
                return;
            }
            ApinizerRequestTab newTab = new ApinizerRequestTab(item.getValue().getValue(), item.getValue().getId(), parent.getCollectionId());
            parent.getApinizerRequestTabs().put(newTab.getId(), newTab);
        }
    }

    public static void unwrapHistoryItems(TreeItem<CustomTreeItem> parentItem, List<ApinizerHistory> histories) {
        if (parentItem != null && !histories.isEmpty()) {
            for (ApinizerHistory history : histories) {
                TreeItem<CustomTreeItem> historyItem = new TreeItem<>(
                        new CustomTreeItem(history.getId(), history.getDate(), true)
                );
                for(ApinizerRequestTab tab : history.getRequestTabs()){
                    TreeItem<CustomTreeItem> tabItem = new TreeItem<>(
                            new CustomTreeItem(tab.getId(), tab.getApinizerHttpRequest().getUrl(), false)
                    );
                    historyItem.getChildren().add(tabItem);
                }
                parentItem.getChildren().add(historyItem);
            }
        }
    }

    public static void unwrapSaveStageItems(ApinizerCollection parent, TreeItem<CustomTreeItem> parentItem) {
        if (parent != null && parentItem != null) {
            if (!parent.getApinizerCollections().isEmpty()) {
                for (ApinizerCollection collection : parent.getApinizerCollections().values()) {
                    TreeItem<CustomTreeItem> collectionItem = new TreeItem<>(
                            new CustomTreeItem(collection.getCollectionId(), collection.getName(), true)
                    );
                    parentItem.getChildren().add(collectionItem);
                    unwrapSaveStageItems(collection, collectionItem); // Recursively unwrap collections
                }
            }
        }
    }

    public static Stack<String> digIntoItems(TreeItem<CustomTreeItem> item){
        TreeItem<CustomTreeItem> parent = item.getParent();
        Stack<String> idStack = new Stack<>();
        idStack.push(item.getValue().getId());
        while(parent != null && !parent.getValue().getId().equals("0")){
            idStack.push(parent.getValue().getId());
            parent = parent.getParent();
        }
        return idStack;
    }

    public static ApinizerCollection getParent(TreeItem<CustomTreeItem> item){
        Stack<String> stack = digIntoItems(item);
        ApinizerCollection collection = Controller.jsonData.baseCollection;
        while(stack.size() > 1){
            collection = collection.getApinizerCollections().get(stack.pop());
        }
        return collection;
    }
    public static ApinizerCollection getParent(Stack<String> stack){
        Stack<String> copyStack = (Stack<String>) stack.clone();
        ApinizerCollection collection = Controller.jsonData.baseCollection;
        while(copyStack.size() > 1){
            collection = collection.getApinizerCollections().get(copyStack.pop());
        }
        return collection;
    }

    public static TreeItem<CustomTreeItem> getParentItem(Stack<String> stack, TreeItem<CustomTreeItem> root){
        String id;
        Stack<String> copyStack = (Stack<String>) stack.clone();
        while(copyStack.size() > 1){
            id = copyStack.pop();
            for(int i = 0; i < root.getChildren().size(); i++){
                if(id.equals(root.getChildren().get(i).getValue().getId())){
                    root = root.getChildren().get(i);
                    break;
                }
            }
        }
        return root;
    }

    public static void duplicateCollection(TreeItem<CustomTreeItem> item,  TreeItem<CustomTreeItem> parentItem, ApinizerCollection parent) {
        if (item != null) {
            if(item.getValue().isIsDirectory()){
                ApinizerCollection newCollection  = new ApinizerCollection(item.getValue().getValue(), UUID.randomUUID().toString(), parent.getCollectionId());
                TreeItem<CustomTreeItem> copyTreeItem = new TreeItem<>(new CustomTreeItem(newCollection.getCollectionId(), item.getValue().getValue(), true));
                parentItem.getChildren().add(copyTreeItem);
                parent.getApinizerCollections().put(newCollection.getCollectionId(), newCollection);
                for (TreeItem<CustomTreeItem> childItem : item.getChildren()) {
                    duplicateCollection(childItem, copyTreeItem,  newCollection);
                }
                return;
            }
            ApinizerRequestTab originalTab = getParent(item).getApinizerRequestTabs().get(item.getValue().getId());
            ApinizerRequestTab newTab = new ApinizerRequestTab(item.getValue().getValue(), UUID.randomUUID().toString(), parent.getCollectionId());
            newTab.setAuth(originalTab.getAuth());
            newTab.setApinizerHttpRequest(originalTab.getApinizerHttpRequest());
            parent.getApinizerRequestTabs().put(newTab.getId(), newTab);
            TreeItem<CustomTreeItem> copyTreeItem = new TreeItem<>(new CustomTreeItem(newTab.getId(), item.getValue().getValue(), false));
            parentItem.getChildren().add(copyTreeItem);
        }
    }

    public static List<ApinizerBasicHeader> headerParser(String json){
        List<ApinizerBasicHeader> array = new ArrayList<>();
        if(json.isEmpty())return array;
        List<String> inputs = Arrays.stream(json.split(",")).toList();
        for(int i = 0; i < inputs.size(); i+=2){
            System.out.println(inputs.get(i) + ": " + inputs.get(i+1));
            array.add(new ApinizerBasicHeader(inputs.get(i), inputs.get(i+1)));
        }
        return array;
    }

    public static List<ApinizerBasicParameter> parameterParser(String json){
        List<ApinizerBasicParameter> array = new ArrayList<>();
        if(json.isEmpty())return array;
        List<String> inputs = Arrays.stream(json.split(",")).toList();
        for(int i = 0; i < inputs.size(); i+=2){
            array.add(new ApinizerBasicParameter(inputs.get(i), inputs.get(i+1)));
        }
        return array;
    }
    public static List<NameValuePair> encodedUrlParser(String json){
        List<NameValuePair> array = new ArrayList<>();
        if(json.isEmpty()) {
            return array;
        }
        List<String> inputs = Arrays.stream(json.split(",")).toList();
        for(int i = 0; i < inputs.size(); i+=2){
            array.add(new ApinizerBasicUrlEncoded(inputs.get(i), inputs.get(i+1)));
        }
        return array;
    }
}
