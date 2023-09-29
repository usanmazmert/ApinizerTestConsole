package com.apinizer.apitest;

import com.apinizer.apitest.apinizerrequest.*;
import com.apinizer.apitest.db.H2DatabaseInitializer;
import com.apinizer.apitest.util.Utils;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSException;
import org.apache.http.NameValuePair;
import netscape.javascript.JSObject;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;


public class Controller{

    @FXML
    WebView bodyView;

    @FXML
    WebView navigationView;

    static WebEngine bodyEngine;

    static WebEngine navEngine;

    @FXML
    TreeView<CustomTreeItem> collectionsTreeView;

    @FXML
    TreeView<CustomTreeItem> historyTreeView;

    @FXML
    HBox body;

    @FXML
    VBox mainContainer;

    @FXML
    Button addCollection;

    public static JSObject responseObject;

    public static JSObject tabsListObject;

    public static ApinizerJSON jsonData;




    @FXML
    protected void openRequestSettings(){
        try{
            RequestSettings.display();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    @FXML
    protected void initialize(){

        try{
            TreeItem<CustomTreeItem> historiesRootItem = new TreeItem<>(new CustomTreeItem("1", "historyRoot", true));
            historyTreeView.setRoot(historiesRootItem);
            historyTreeView.setShowRoot(false);

            historyTreeView.setCellFactory(c -> new HistoryTreeCell());

            /* Set Navigation Congigurations*/
            body.setPadding(new Insets(0, 0, 10, 0));
            navEngine = navigationView.getEngine();
            navEngine.load(getClass().getResource("html-source/navigation.html").toExternalForm());

            navEngine.setJavaScriptEnabled(true);
            navEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
                try{
                    if (newValue == Worker.State.SUCCEEDED) {

                        JSObject window = (JSObject) navEngine.executeScript("window");
                        window.setMember("javaFXController", this);

                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            });

            navigationView.setContextMenuEnabled(false);

            /*Set TreeViewConfigurations*/

            TreeItem<CustomTreeItem> collectionsRootItem = new TreeItem<>(new CustomTreeItem("0", "root", true));

            collectionsTreeView.setRoot(collectionsRootItem);
            collectionsTreeView.setShowRoot(false);

            collectionsTreeView.setCellFactory(c -> new CustomTreeCell());

            String data = H2DatabaseInitializer.retrieveJsonData();
            if(data == null){
                jsonData = new ApinizerJSON();
                jsonData.setBaseCollection(new ApinizerCollection(collectionsRootItem.getValue().getValue(), collectionsRootItem.getValue().getId(), null));
            }else{
                System.out.println(Utils.formatJsonString(data.substring(1, data.length()-1).replace("\\", "")));
                jsonData = Utils.fromJSONString(data.substring(1, data.length()-1).replace("\\", ""), ApinizerJSON.class);
                Utils.unwrapCollectionItems(jsonData.baseCollection, collectionsRootItem);
                Utils.unwrapHistoryItems(historiesRootItem, jsonData.histories);
            }


            addCollection.setOnAction((e) -> {
                ApinizerCollection newCollection = new ApinizerCollection();
                newCollection.setParentId("0");
                CustomTreeItem item = new CustomTreeItem(newCollection.getCollectionId(), "New Collection", true);
                TreeItem<CustomTreeItem> treeItem = new TreeItem<>(item); // Wrap the custom item
                jsonData.baseCollection.getApinizerCollections().put(newCollection.getCollectionId(), newCollection);
                treeItem.setExpanded(true);
                collectionsRootItem.getChildren().add(treeItem);
            });

            /* Set Main Body Configurations */

            bodyEngine = bodyView.getEngine();
            bodyEngine.load(getClass().getResource("html-source/main.html").toExternalForm());

            bodyEngine.setJavaScriptEnabled(true);
            bodyEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
                try{
                    if (newValue == Worker.State.SUCCEEDED) {
                        // Bind the Java method to JavaScript context
                        JSObject window = (JSObject) bodyEngine.executeScript("window");
                        window.setMember("javaFXController", this); // Expose JavaFX controller to JavaScript

                        this.responseObject = (JSObject) bodyEngine.executeScript("new Object()");
                        window.setMember("responseJavaObject", responseObject);

                        this.tabsListObject = (JSObject) bodyEngine.executeScript("new Object()");
                        window.setMember("tabList", tabsListObject);

                        getTabs();

                        if(jsonData.tabIndexId != null){
                            bodyEngine.executeScript("activeElementId = " + "'" + jsonData.tabIndexId + "'");
                        }

                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            });
            bodyView.setContextMenuEnabled(false);

        }catch (JSException ex){

            Platform.runLater(() -> bodyEngine.reload());

        }
    }


    public void displayRequestSettings() {
        try {
            RequestSettings.display();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void displaySaveTab() {
        try {
            SaveStage.display();
            if(SaveStageController.itemStack != null){
                TreeItem<CustomTreeItem> parent = Utils.getParentItem(SaveStageController.itemStack, collectionsTreeView.getRoot());
                parent.getChildren().add(new TreeItem<CustomTreeItem>(new CustomTreeItem(SaveStageController.tab.getId(), SaveStageController.tab.getName(), false)));
            }
            SaveStageController.itemStack = null;
            SaveStageController.tab = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void setCollectionsVisibility(){
        try
        {
            historyTreeView.setVisible(false);
            collectionsTreeView.setVisible(true);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void setHistoryVisibility(){
        try
        {
            historyTreeView.setVisible(true);
            collectionsTreeView.setVisible(false);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void createBasicHeader(String name, String value){
        try{
            System.out.println(name);
            if(name.isEmpty() || value.isEmpty())return;
            ApinizerRequestTab apinizerRequestTab = jsonData.openedTabs.get(jsonData.tabIndexId);
            for(ApinizerBasicHeader apinizerBasicHeader : apinizerRequestTab.getApinizerHttpRequest().getHeaderList()){
                if(apinizerBasicHeader.getName().equals(name)){
                    apinizerBasicHeader.setValue(value);
                    return;
                }
            }
            apinizerRequestTab.getApinizerHttpRequest().getHeaderList().add(new ApinizerBasicHeader(name, value));
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void clearHeaders(){
        jsonData.openedTabs.get(jsonData.tabIndexId).getApinizerHttpRequest().getHeaderList().clear();
    }


    public ApinizerHttpResponse sendRequest(ApinizerHttpRequest apinizerHttpRequest){
        ApinizerHttpResponse apinizerResponse = new ApinizerHttpClient(
                ApinizerHttpRequest.createSimpleRequest(apinizerHttpRequest.getUrl(), apinizerHttpRequest.getHeaderList(), apinizerHttpRequest.getBody(),
                        apinizerHttpRequest.getHttpMethod(), 10, null)).callRestService();
        return apinizerResponse;
    }

    public void createHistory(ApinizerHttpRequest apinizerHttpRequest){
        LocalDate currentDate = LocalDate.now();

        // Define a custom date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd", Locale.ENGLISH);

        // Format the current date using the custom format
        String formattedDate = currentDate.format(formatter);

        ApinizerRequestTab apinizerRequestTab = new ApinizerRequestTab();
        apinizerRequestTab.setApinizerHttpRequest(apinizerHttpRequest);
        if(!jsonData.histories.isEmpty() && jsonData.histories.get(0).getDate().equals(formattedDate)){
            jsonData.histories.get(0).getRequestTabs().add(0, apinizerRequestTab);
            historyTreeView.getRoot().getChildren().get(0).getChildren().add(0, new TreeItem<CustomTreeItem>(new CustomTreeItem(apinizerRequestTab.getId(), apinizerHttpRequest.getUrl(), false)));
        }else{
            ApinizerHistory apinizerHistory = new ApinizerHistory();
            apinizerHistory.getRequestTabs().add(0, apinizerRequestTab);
            jsonData.getHistories().add(0, apinizerHistory);
            TreeItem<CustomTreeItem> historyItem = new TreeItem<>(new CustomTreeItem(apinizerHistory.getId(), formattedDate, true));
            historyTreeView.getRoot().getChildren().add(0, historyItem);
            historyTreeView.getRoot().getChildren().get(0).getChildren().add(0, new TreeItem<CustomTreeItem>(new CustomTreeItem(apinizerRequestTab.getId(), apinizerRequestTab.getName(), false)));
        }
    }

    public void getTabs(){
        if(!jsonData.openedTabs.isEmpty()){
            JSObject jsTab;
            for(ApinizerRequestTab tab : jsonData.openedTabs.values()){
                openTab(tab);

            }
        }
    }

    public void saveTab(String id, JSObject jsObject ){
        ApinizerRequestTab temporalTab = jsonData.openedTabs.get(id);
        System.out.println(temporalTab.getName());
        ApinizerHttpRequest apinizerHttpRequest = ApinizerHttpRequest.createSimpleRequest(jsObject.getMember("url").toString(), Utils.headerParser(jsObject.getMember("headers").toString()), jsObject.getMember("body").toString(),
                EnumHttpRequestMethod.valueOf(jsObject.getMember("method").toString()), 10, null);
        //apinizerHttpRequest.setNameValuePairList((List<NameValuePair>) Utils.encodedUrlParser(jsObject.getMember("urlencoded").toString()));
        apinizerHttpRequest.setParameterList(Utils.parameterParser(jsObject.getMember("parameters").toString()));
        temporalTab.setApinizerHttpRequest(apinizerHttpRequest);
        temporalTab.getAuth().setMethod(jsObject.getMember("authMethod").toString());
        temporalTab.getAuth().setToken(jsObject.getMember("authToken").toString());
        temporalTab.getAuth().setKey(jsObject.getMember("authKey").toString());
        temporalTab.getAuth().setValue(jsObject.getMember("authValue").toString());
        temporalTab.getAuth().setUsername(jsObject.getMember("authUsername").toString());
        temporalTab.getAuth().setPassword(jsObject.getMember("authPassword").toString());
        if(temporalTab.getParentFolderId() == null){
            displaySaveTab();
            return;
        }
       ApinizerRequestTab savedTab = Utils.getParent(temporalTab.getIdStack()).getApinizerRequestTabs().get(id);
       savedTab.setApinizerHttpRequest(apinizerHttpRequest);
       savedTab.getAuth().setMethod(jsObject.getMember("authMethod").toString());
       savedTab.getAuth().setToken(jsObject.getMember("authToken").toString());
       savedTab.getAuth().setKey(jsObject.getMember("authKey").toString());
       savedTab.getAuth().setValue(jsObject.getMember("authValue").toString());
       savedTab.getAuth().setUsername(jsObject.getMember("authUsername").toString());
       savedTab.getAuth().setPassword(jsObject.getMember("authPassword").toString());
    }

    public void renameTab(String id, String name){
        try{

            ApinizerRequestTab tab = jsonData.openedTabs.get(id);
            tab.setName(name);
            if (tab.getParentFolderId() != null) {
                ApinizerRequestTab originalTab = Utils.getParent(tab.getIdStack()).getApinizerRequestTabs().get(id);
                System.out.println(originalTab.getName());
                originalTab.setName(name);
                TreeItem<CustomTreeItem> parentItem = Utils.getParentItem(tab.getIdStack(), collectionsTreeView.getRoot());
                for (TreeItem<CustomTreeItem> item : parentItem.getChildren()) {
                    System.out.println(parentItem.getValue().getValue());
                    if (item.getValue().getId().equals(id)) {
                        item.getValue().setValue(name);
                        collectionsTreeView.refresh(); // Refresh the TreeView to update the cell text
                        break;
                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void saveTemporal(String id, JSObject jsObject ){
        ApinizerRequestTab temporalTab = jsonData.openedTabs.get(id);
        ApinizerHttpRequest apinizerHttpRequest = ApinizerHttpRequest.createSimpleRequest(jsObject.getMember("url").toString(), Utils.headerParser(jsObject.getMember("headers").toString()), jsObject.getMember("body").toString(),
                EnumHttpRequestMethod.valueOf(jsObject.getMember("method").toString()), 10, null);
        apinizerHttpRequest.setParameterList(Utils.parameterParser(jsObject.getMember("parameters").toString()));
        temporalTab.setApinizerHttpRequest(apinizerHttpRequest);
        temporalTab.getAuth().setMethod(jsObject.getMember("authMethod").toString());
        temporalTab.getAuth().setToken(jsObject.getMember("authToken").toString());
        temporalTab.getAuth().setKey(jsObject.getMember("authKey").toString());
        temporalTab.getAuth().setValue(jsObject.getMember("authValue").toString());
        temporalTab.getAuth().setUsername(jsObject.getMember("authUsername").toString());
        temporalTab.getAuth().setPassword(jsObject.getMember("authPassword").toString());
    }

    public void saveAllTabs(){
        bodyEngine.executeScript("saveAndExit()");
    }


    public static void openTab(ApinizerRequestTab tab){
        try{
            JSObject jsTab = (JSObject) bodyEngine.executeScript("new Object()");
            System.out.println("Buradayım2");

            tabsListObject.setMember(tab.getId(), jsTab);

            jsTab.setMember("id", tab.getId());
            jsTab.setMember("name", tab.getName());
            jsTab.setMember("body", tab.getApinizerHttpRequest().getBody());
            jsTab.setMember("method", tab.getApinizerHttpRequest().getHttpMethod().name());
            jsTab.setMember("url", tab.getApinizerHttpRequest().getUrl());
            jsTab.setMember("authMethod", tab.getAuth().getMethod());
            jsTab.setMember("authKey", tab.getAuth().getKey());
            jsTab.setMember("authValue", tab.getAuth().getValue());
            jsTab.setMember("authUsername", tab.getAuth().getUsername());
            jsTab.setMember("authPassword", tab.getAuth().getPassword());
            jsTab.setMember("authToken", tab.getAuth().getToken());

            addKeyValuePair(tab.getApinizerHttpRequest().getParameterList());
            bodyEngine.executeScript("flushNameValueList (" + "'" + tab.getId() + "'" + "," + "\"parameters\"" + ")");

            addKeyValuePair(tab.getApinizerHttpRequest().getHeaderList());
            bodyEngine.executeScript("flushNameValueList (" + "'" + tab.getId() + "'" + "," + "\"headers\"" + ")");

            addKeyValuePair(tab.getApinizerHttpRequest().getNameValuePairList());
            bodyEngine.executeScript("flushNameValueList (" + "'" + tab.getId() + "'" + "," + "\"urlencoded\"" + ")");

            if(tab.getApinizerHttpResponse() != null){
                jsTab.setMember("responseBody", tab.getApinizerHttpResponse().getFormattedResponseString());
                jsTab.setMember("responseHeaders", addKeyValuePair(Arrays.stream(tab.getApinizerHttpResponse().getHeaders()).toList()));
                jsTab.setMember("responseStatus", tab.getApinizerHttpResponse().getStatusCode());
                jsTab.setMember("responseStatusMessage", tab.getApinizerHttpResponse().getStatusMessage());
                jsTab.setMember("responseTime", tab.getApinizerHttpResponse().getResponseTime());
                jsTab.setMember("responseSize", tab.getApinizerHttpResponse().getHumanReadableStringContentSize());
            }

            //bodyEngine.executeScript("methodBtn.firstElementChild.style.color = methodColors[" + "'" + tab.getApinizerHttpRequest().getHttpMethod().name() + "'" +  "]");


            jsonData.openedTabs.put(tab.getId(), tab);
            jsonData.tabIndexId = tab.getId();

            bodyEngine.executeScript("addNewTab(" + "'" + tab.getName() + "'" + "," + "'" + tab.getId() + "'" + ")");

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static <T extends NameValuePair> JSObject addKeyValuePair(List<T> array){
        JSObject jsArray = (JSObject) bodyEngine.executeScript("new Object()");

        if(array != null){

            for (T parameter : array) {

                // Create a JavaScript object for each key-value pair
                bodyEngine.executeScript("createNameValueList(" + "'" + parameter.getName() + "'" + "," + "'" + parameter.getValue() + "'" + ")");

            }
        }

        return jsArray;
    }

    public void deleteTab(String newTabId, String deletedTabId){
        jsonData.openedTabs.remove(deletedTabId);
        jsonData.tabIndexId = newTabId;
    }

    public void changeTab(String id){
        jsonData.tabIndexId = id;
    }

    public String addNewTab(){
        try{
            ApinizerRequestTab newTab = new ApinizerRequestTab();
            jsonData.openedTabs.put(newTab.getId(), newTab);
            jsonData.tabIndexId = newTab.getId();

            JSObject jsTab = (JSObject) bodyEngine.executeScript("new Object()");

            jsTab.setMember("id", newTab.getId());
            jsTab.setMember("name", "New Request");
            jsTab.setMember("body", "");
            jsTab.setMember("method", "GET");
            jsTab.setMember("methodColor", "#6B95FFFF");
            jsTab.setMember("url", "");
            jsTab.setMember("authMethod", "No Auth");
            jsTab.setMember("responseBody", "");
            jsTab.setMember("isOpened", "true");

            tabsListObject.setMember(newTab.getId(), jsTab);

            return newTab.getId();


        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public void debug(String message){
        try{
            throw new Exception(message);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void info(String message){
        System.out.println("********************************************************************************");
        System.out.println("System Info:");
        System.out.println(message);
        System.out.println("********************************************************************************");
    }


    public void sendJsRequestEvent(String url, String body, String method){
        EnumHttpRequestMethod requestMethod = EnumHttpRequestMethod.valueOf(method);
        ApinizerHttpRequest apinizerRequest = new ApinizerHttpRequest();
        apinizerRequest.setUrl(url);
        apinizerRequest.setBody(body);
        apinizerRequest.setHeaderList(jsonData.openedTabs.get(jsonData.tabIndexId).getApinizerHttpRequest().getHeaderList());
        apinizerRequest.setHttpMethod(requestMethod);


        ApinizerHttpResponse response = sendRequest(apinizerRequest);
        String data = Utils.formatJsonString(response.getResponseString());

        JSObject headerObject = (JSObject) bodyEngine.executeScript("new Object()");
        Arrays.stream(response.getHeaders()).forEach(item -> headerObject.setMember(item.getName(), item.getValue()));

        System.out.println();

        responseObject.setMember("headers", headerObject);
        responseObject.setMember("data", data);
        responseObject.setMember("statusCode", Integer.toString(response.getStatusCode()));
        responseObject.setMember("time", response.getResponseTime());
        responseObject.setMember("size", response.getHumanReadableStringContentSize());

        jsonData.openedTabs.get(jsonData.tabIndexId).setApinizerHttpResponse(response);

        createHistory(apinizerRequest);
    }

    public void printJSON(String data){
        data = data.substring(1, data.length() - 1);
        System.out.println(Utils.formatJsonString(data));
    }

    public void storeJson() {
        try {
            System.out.println(Utils.toJSONString(jsonData));
            H2DatabaseInitializer.insertJsonData(Utils.toJSONString(jsonData));
            //printJSON(Utils.toJSONString(jsonData));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}