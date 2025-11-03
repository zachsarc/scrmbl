package com.example.scrmbl;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxml = new FXMLLoader(Application.class.getResource("/com/example/scrmbl/view.fxml"));
        Scene scene = new Scene(fxml.load(), 700, 400);
        stage.setTitle("SCRMBL");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) { launch(); }
}
