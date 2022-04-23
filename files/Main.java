package com.Minin.cloud;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
//        Parent parent = FXMLLoader.load(getClass().getResource("main.fxml"));
//        primaryStage.setScene(new Scene(parent));
//        primaryStage.show();
        URL url = new File("D:\\Program files\\Cloud\\cloud-application\\src\\main\\resources\\com.Minin.cloud\\main.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
