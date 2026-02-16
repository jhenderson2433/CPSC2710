/*
 * Project: Module 4 – Flight Designator App
 * Author: Javontea Henderson
 * Email: Jdh0204@auburn.edu
 * Date: 2026-02-01
 * Description: JavaFX airline flight app.
 */

package com.example.module4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FlightDesignatorApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FlightDesignatorApplication.class.getResource("flight-designator-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
