package com.apinizer.apitest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class SaveStage {
    public static void display() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("save-tab.fxml"));
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);

        Scene scene = new Scene(fxmlLoader.load(), 600, 600);

        scene.getStylesheets().add(Main.class.getResource("save-tab-styles.css").toExternalForm());

        window.setScene(scene);

        window.setResizable(false);

        window.showAndWait();

    }
}
