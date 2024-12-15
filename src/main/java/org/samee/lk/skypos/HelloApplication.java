package org.samee.lk.skypos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("product-add-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1900, 1000);
        stage.setOpacity(1);
        stage.setTitle("Petcare System");

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}