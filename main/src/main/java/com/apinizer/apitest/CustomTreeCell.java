package com.apinizer.apitest;


import com.apinizer.apitest.apinizerrequest.ApinizerCollection;
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

public class CustomTreeCell extends TreeCell<CustomTreeItem> {
    private final ContextMenu directoryContextMenu = createDirectoryContextMenu();
    private final ContextMenu fileContextMenu = createFileContextMenu();
    private final TextField textField = new TextField();
    private final Label label = new Label();
    private final ImageView icon = new ImageView();

    public CustomTreeCell() {
        icon.setFitHeight(16);
        icon.setFitWidth(16);
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                ApinizerCollection apinizerCollection = Utils.getParent(getTreeItem());
                if(getItem().isDirectory()){
                    apinizerCollection.getApinizerCollections().get(getItem().getId()).setName(textField.getText());
                    commitEdit(new CustomTreeItem(getItem().getId(), textField.getText(), true));
                }
                else{
                    apinizerCollection.getApinizerRequestTabs().get(getItem().getId()).setName(textField.getText());
                    commitEdit(new CustomTreeItem(getItem().getId(), textField.getText(), false));
                }

                getParent().requestFocus();
                event.consume();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
                event.consume();
            }
        });
        setOnMouseClicked(event -> {
            TreeItem<CustomTreeItem> selectedItem = getTreeItem();
            if(selectedItem != null && !selectedItem.getValue().isIsDirectory() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2){
                if(Controller.jsonData.openedTabs.containsKey(selectedItem.getValue().getId())){
                    Controller.bodyEngine.executeScript("changeTab(" + "'" + selectedItem.getValue().getId() +  "'" + ")");
                    return;
                }
                clickEvent(selectedItem);
            }
        });

    }
    protected void updateItem(CustomTreeItem item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setContextMenu(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                textField.setText(item.getValue());
                setText(null);
                setGraphic(textField);
            } else {
                label.setText(item.getValue());

                // Clear the icon by default
                icon.setImage(null);

                // Set the appropriate context menu based on the item's type
                if (item.isDirectory()) {
                    setContextMenu(directoryContextMenu);
                    icon.setImage(new Image(Main.class.getResourceAsStream("images/5994710.png")));
                } else {
                    setContextMenu(fileContextMenu);
                }
                // Create an HBox to hold the icon and label
                HBox hbox = new HBox();
                hbox.setSpacing(5); // Adjust the spacing as needed
                hbox.setAlignment(Pos.CENTER_LEFT);

                // Add the icon and label to the HBox
                hbox.getChildren().addAll(icon, label);

                // Set the HBox as the graphic for the TreeCell
                setGraphic(hbox);
            }
        }
    }

    @Override
    public void startEdit() {
        super.startEdit();
        textField.setText(getItem().getValue());
        setText(null);
        setGraphic(textField);
        textField.requestFocus();
        textField.selectAll();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem().getValue());
        setGraphic(null);
    }

    @Override
    public void commitEdit(CustomTreeItem newValue) {
        super.commitEdit(newValue);
        getItem().setValue(textField.getText()); // Update the item's value
        setGraphic(null);
        updateItem(getItem(), false); // Update the cell's display with the new value
        if(!getItem().isDirectory() && Controller.jsonData.getOpenedTabs().containsKey(getItem().getId())){
            Controller.bodyEngine.executeScript("renameTab(" + "'" + getItem().getId() + "'" + "," + "'" + getItem().getValue() + "'" + ")");
        }

    }


    private ContextMenu createDirectoryContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem addRequestItem = new MenuItem("Add Request");
        MenuItem addFolderItem = new MenuItem("Add Folder");
        MenuItem renameItem = new MenuItem("Rename");
        MenuItem duplicateItem = new MenuItem("Duplicate");
        MenuItem deleteItem = new MenuItem("Delete");
        contextMenu.getItems().addAll(addRequestItem, addFolderItem, renameItem, duplicateItem, deleteItem);


        addRequestItem.setOnAction(event -> {
            TreeItem<CustomTreeItem> selectedItem = getTreeItem();
            if (selectedItem != null) {
                ApinizerRequestTab apinizerRequestTab = new ApinizerRequestTab();
                // Handle the "Add Request" button click here
                // Create a new request item and add it as a child
                CustomTreeItem newCell = new CustomTreeItem(apinizerRequestTab.getId(), "New Request", false); // Assuming it's a file
                TreeItem<CustomTreeItem> newItem = new TreeItem<>(newCell);
                newItem.setExpanded(false);

                selectedItem.getChildren().add(newItem);

                ApinizerCollection parentCollection = Utils.getParent(newItem);
                apinizerRequestTab.setParentFolderId(parentCollection.getCollectionId());
                parentCollection.getApinizerRequestTabs().put(apinizerRequestTab.getId(), apinizerRequestTab);

                apinizerRequestTab.setName(newItem.getValue().getValue());

                getTreeView().getSelectionModel().select(newItem);

                clickEvent(newItem);

//                            openTab(tab);
//                            jsonData.tabIndexId = selectedItem.getValue().getId();
            }
        });
        addFolderItem.setOnAction(event -> {
                TreeItem<CustomTreeItem> selectedItem = getTreeItem();
                if(selectedItem != null){
                    ApinizerCollection apinizerCollection = new ApinizerCollection();

                    CustomTreeItem item = new CustomTreeItem(apinizerCollection.getCollectionId(), apinizerCollection.getName(), true);
                    TreeItem<CustomTreeItem> treeItem = new TreeItem<>(item); // Wrap the custom item
                    treeItem.setExpanded(true);

                    getTreeItem().getChildren().add(treeItem);

                    ApinizerCollection parentCollection = Utils.getParent(treeItem);
                    apinizerCollection.setParentId(parentCollection.getCollectionId());
                    parentCollection.getApinizerCollections().put(apinizerCollection.getCollectionId(), apinizerCollection);

                }
            }
        );

        deleteItem.setOnAction(event -> {
            TreeItem<CustomTreeItem> selectedItem = getTreeItem();
            if (selectedItem != null) {
                TreeItem<CustomTreeItem> parent = selectedItem.getParent();
                if (parent != null) {
                    ApinizerCollection parentCollection = Utils.getParent(selectedItem);
                    ApinizerCollection selectedCollection = parentCollection.getApinizerCollections().get(getItem().getId());
                    for(String key : selectedCollection.getApinizerRequestTabs().keySet()){
                        if(Controller.jsonData.openedTabs.containsKey(key)){
                            Controller.bodyEngine.executeScript("deleteTabFromList(" + "'" + key +"'" + ")");
                        }
                    }
                    parentCollection.getApinizerCollections().remove(selectedItem.getValue().getId());
                    parent.getChildren().remove(selectedItem);
                }
            }
        });

        duplicateItem.setOnAction(event -> {
            TreeItem<CustomTreeItem> selectedItem = getTreeItem();
            if (selectedItem != null) {
                TreeItem<CustomTreeItem> parent = selectedItem.getParent();
                if (parent != null) {
                    ApinizerCollection parentCollection = Utils.getParent(selectedItem);
                    Utils.duplicateCollection(selectedItem, parent, parentCollection);
                }
            }
        });

        renameItem.setOnAction(event -> {
            startEdit();
        });



        return contextMenu;
    }

    private ContextMenu createFileContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem openInTabItem = new MenuItem("Open In Tab");
        MenuItem renameRequestItem = new MenuItem("Rename");
        MenuItem duplicateItem = new MenuItem("Duplicate");
        MenuItem deleteRequestItem = new MenuItem("Delete");
        contextMenu.getItems().addAll(openInTabItem, renameRequestItem, duplicateItem, deleteRequestItem);

        deleteRequestItem.setOnAction(event -> {
            TreeItem<CustomTreeItem> selectedItem = getTreeItem();
            if (selectedItem != null) {
                TreeItem<CustomTreeItem> parent = selectedItem.getParent();
                if (parent != null) {
                    ApinizerCollection parentCollection = Utils.getParent(selectedItem);
                    parentCollection.getApinizerRequestTabs().remove(selectedItem.getValue().getId());
                    parent.getChildren().remove(selectedItem);
                    if(Controller.jsonData.openedTabs.containsKey(selectedItem.getValue().getId())){
                        Controller.jsonData.openedTabs.remove(selectedItem.getValue().getId());
                        Controller.bodyEngine.executeScript("deleteTabFromList(" + "'" + selectedItem.getValue().getId() +"'" + ")");
                        Controller.jsonData.tabIndexId = Controller.bodyEngine.executeScript("window.activeElementId").toString();
                    }
                }
            }
        });

        duplicateItem.setOnAction(event -> {
            TreeItem<CustomTreeItem> selectedItem = getTreeItem();
            if (selectedItem != null) {
                TreeItem<CustomTreeItem> parent = selectedItem.getParent();
                if (parent != null) {
                    ApinizerCollection parentCollection = Utils.getParent(selectedItem);
                    ApinizerRequestTab apinizerRequestTab = parentCollection.getApinizerRequestTabs().get(selectedItem.getValue().getId());

                    ApinizerRequestTab copiedTab = new ApinizerRequestTab(apinizerRequestTab.getName(), UUID.randomUUID().toString(), apinizerRequestTab.getParentFolderId());
                    parentCollection.getApinizerRequestTabs().put(copiedTab.getId(), copiedTab);

                    CustomTreeItem selectedCustomItem = selectedItem.getValue();
                    CustomTreeItem newCell = new CustomTreeItem(copiedTab.getId(), selectedCustomItem.getValue(), selectedCustomItem.isDirectory());
                    TreeItem<CustomTreeItem> duplicateCustomItem = new TreeItem<>(newCell);
                    parent.getChildren().add(duplicateCustomItem);
                }
            }
        });

        renameRequestItem.setOnAction(event -> {
            startEdit();
        });



        return contextMenu;
    }

    public void clickEvent(TreeItem<CustomTreeItem> selectedItem){
        ApinizerCollection parent = Utils.getParent(selectedItem);
        ApinizerRequestTab tab = new ApinizerRequestTab(selectedItem.getValue().getValue(), selectedItem.getValue().getId(), selectedItem.getParent().getValue().getId());
        Stack<String> idStack = Utils.digIntoItems(selectedItem);
        tab.setIdStack(idStack);
        ApinizerRequestTab originalObject = parent.getApinizerRequestTabs().get(selectedItem.getValue().getId());
        tab.setApinizerHttpRequest(originalObject.getApinizerHttpRequest());
        tab.setApinizerHttpResponse(originalObject.getApinizerHttpResponse());
        tab.setAuth(originalObject.getAuth());
        Controller.openTab(tab);
        Controller.jsonData.tabIndexId = selectedItem.getValue().getId();
    }
}
