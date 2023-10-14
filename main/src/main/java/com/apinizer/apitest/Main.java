package com.apinizer.apitest;

import com.apinizer.apitest.apinizerrequest.ApinizerJSON;
import com.apinizer.apitest.db.H2DatabaseInitializer;
import com.apinizer.apitest.util.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.h2.tools.Server;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main extends javafx.application.Application{

    public static Server server;
    @Override
    public void start(Stage stage) throws IOException

    {

        // Load the icon image (you need to provide the path to your icon image)
        Image icon = new Image(getClass().getResourceAsStream("images/apinizer logo - A.png"));

        // Set the window icon by adding the icon to the list of icons
        stage.getIcons().add(icon);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main.fxml"));
        Parent root = fxmlLoader.load();
        Controller controller = fxmlLoader.getController();
        Scene scene = new Scene(root, 1240, 720);
        stage.setTitle("Apinizer API Test Console");
        stage.setScene(scene);
        scene.getStylesheets().add(Main.class.getResource("styles.css").toExternalForm());

        stage.setOnCloseRequest(event -> {
            // This code will be executed when the stage is closed
            // Close the database connection here
            try {
                Connection connection = H2DatabaseInitializer.getConnection();
                if (connection != null && !connection.isClosed()) {
                    controller.saveAllTabs();
                    controller.storeJson();
                    server.stop();
                    connection.close();
                    System.out.println("Connection has been closed successfully");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        stage.setMinWidth(850);
        stage.setMinHeight(720);
        stage.show();

    }

    public static void main(String[] args) {
        H2DatabaseInitializer.init();

        try{
            //openWebBrowser("http://localhost:8082");
            server = Server.createWebServer("-web", "-webAllowOthers", "-tcp", "-tcpAllowOthers");

            server.start();

            System.out.println("H2 Console is running at http://localhost:8082");


        }catch (SQLException ex){
            ex.printStackTrace();
        }
        launch();
    }
    private static void openWebBrowser(String url) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

}