package com.apinizer.apitest;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class CustomTreeItem extends TreeItem<String> {
    private final BooleanProperty isDirectory;

    private final String id;

    public CustomTreeItem(String id, String value, boolean isDirectory) {
        super(value);
        this.id = id;
        this.isDirectory = new SimpleBooleanProperty(isDirectory);
    }

    public BooleanProperty isDirectoryProperty() {
        return isDirectory;
    }

    public boolean isDirectory() {
        return isDirectory.get();
    }

    public void setDirectory(boolean isDirectory) {
        this.isDirectory.set(isDirectory);
    }

    public boolean isIsDirectory() {
        return isDirectory.get();
    }

    public void setIsDirectory(boolean isDirectory) {
        this.isDirectory.set(isDirectory);
    }

    public String getId() {
        return id;
    }
}
