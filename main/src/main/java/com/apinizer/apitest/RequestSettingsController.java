package com.apinizer.apitest;

import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.EventTarget;

public class RequestSettingsController {

    @FXML
    WebView requestSettingsView;

    @FXML
    VBox vbox;

    @FXML
    protected void initialize(){
        final WebEngine engine = requestSettingsView.getEngine();
        engine.load(getClass().getResource("html-source/request-settings.html").toExternalForm());

        engine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            try{
                if (newValue == Worker.State.SUCCEEDED) {
                    // Bind the Java method to JavaScript context

                    Stage stage = (Stage) vbox.getScene().getWindow();

                    Document document = engine.getDocument();

                    Element closeButton = document.getElementById("close-btn");

                    ((EventTarget) closeButton).addEventListener("click", (event) -> {try{stage.close();}catch (Exception ex){ex.printStackTrace();}}, false);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });
    }

}
