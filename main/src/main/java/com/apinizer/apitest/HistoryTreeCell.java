package com.apinizer.apitest;


import com.apinizer.apitest.apinizerrequest.ApinizerCollection;
import com.apinizer.apitest.apinizerrequest.ApinizerHistory;
import com.apinizer.apitest.apinizerrequest.ApinizerRequestTab;
import com.apinizer.apitest.util.Utils;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;


import java.awt.*;
import java.util.Objects;
import java.util.Stack;
import java.util.UUID;

public class HistoryTreeCell extends TreeCell<CustomTreeItem> {
    public HistoryTreeCell() {
        setOnMouseClicked(event -> {
            TreeItem<CustomTreeItem> selectedItem = getTreeItem();
            if(selectedItem != null && !selectedItem.getValue().isIsDirectory() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2){
                clickEvent(selectedItem);
            }
        });

    }
    protected void updateItem(CustomTreeItem item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) {
            // Set the label of the TreeItem based on your CustomTreeItem's label property or method
            setText(item.getValue()); // You should replace 'getLabel()' with the actual method or property name
        } else {
            setText(null);
        }

    }

    public void clickEvent(TreeItem<CustomTreeItem> selectedItem){
        ApinizerHistory parent = new ApinizerHistory();
        for(ApinizerHistory apinizerHistory : Controller.jsonData.getHistories()){
            if(apinizerHistory.getId().equals(selectedItem.getParent().getValue().getId())){
                parent = apinizerHistory;
                break;
            }
        }
        ApinizerRequestTab originalObject = new ApinizerRequestTab();
        for(ApinizerRequestTab tab : parent.getRequestTabs()){
            if(tab.getId().equals(selectedItem.getValue().getId())){
                originalObject = tab;
                break;
            }
        }
        ApinizerRequestTab tab = new ApinizerRequestTab(selectedItem.getValue().getValue(), UUID.randomUUID().toString(), null);
        tab.setApinizerHttpRequest(originalObject.getApinizerHttpRequest());
        tab.setApinizerHttpResponse(originalObject.getApinizerHttpResponse());
        Controller.openTab(tab);
        Controller.jsonData.tabIndexId = tab.getId();
    }
}
