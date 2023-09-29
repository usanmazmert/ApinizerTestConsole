package com.apinizer.apitest;

import com.apinizer.apitest.apinizerrequest.ApinizerCollection;
import com.apinizer.apitest.apinizerrequest.ApinizerRequestTab;
import com.apinizer.apitest.util.Utils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Stack;

public class SaveStageController {

    @FXML
    TreeView<CustomTreeItem> treeView;

    @FXML
    VBox container;

    @FXML
    Button applyBtn;

    @FXML
    Button cancelBtn;

    @FXML
    HBox buttonContainer;

    static public ApinizerRequestTab tab;

    static public Stack<String> itemStack;

    @FXML
    protected void initialize(){

        buttonContainer.setPadding(new Insets(5, 5, 5 ,5));

        TreeItem<CustomTreeItem> rootItem = new TreeItem<>(new CustomTreeItem("0", "root", true));

        treeView.setRoot(rootItem);
        treeView.setShowRoot(false);

        treeView.setCellFactory(c -> new CustomTreeCell());
        Utils.unwrapSaveStageItems(Controller.jsonData.baseCollection, rootItem);

        tab = Controller.jsonData.getOpenedTabs().get(Controller.jsonData.tabIndexId);


        applyBtn.setOnAction(event -> {
            TreeItem<CustomTreeItem> selectedItem = treeView.getSelectionModel().getSelectedItem();
            if(selectedItem != null){
                ApinizerCollection parent = Utils.getParent(selectedItem);
                ApinizerRequestTab newTab = tab;
                newTab.setName(tab.getName());
                newTab.getAuth().setMethod(tab.getAuth().getMethod());
                newTab.getAuth().setToken(tab.getAuth().getToken());
                newTab.getAuth().setKey(tab.getAuth().getKey());
                newTab.getAuth().setValue(tab.getAuth().getValue());
                newTab.getAuth().setUsername(tab.getAuth().getUsername());
                newTab.getAuth().setPassword(tab.getAuth().getPassword());
                newTab.setParentFolderId(parent.getCollectionId());
                parent.getApinizerCollections().get(selectedItem.getValue().getId()).getApinizerRequestTabs().put(newTab.getId(), newTab);
                TreeItem<CustomTreeItem> item = new TreeItem<CustomTreeItem>(new CustomTreeItem(newTab.getId(), newTab.getName(), false));
                selectedItem.getChildren().add(item);
                itemStack = Utils.digIntoItems(item);
                tab.setIdStack(itemStack);
                Platform.runLater(() -> {
                    Stage stage = (Stage) container.getScene().getWindow();
                    stage.close();
                });
            }
        });

        cancelBtn.setOnAction(event -> {
            Platform.runLater(() -> {
                Stage stage = (Stage) container.getScene().getWindow();
                stage.close();
            });
        });
    }
}
