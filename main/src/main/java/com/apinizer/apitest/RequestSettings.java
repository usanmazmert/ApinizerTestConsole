package com.apinizer.apitest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class RequestSettings {


    public static void display() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("request-settings.fxml"));
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);

        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        window.setScene(scene);

        window.setResizable(false);

        window.showAndWait();
    }

}
