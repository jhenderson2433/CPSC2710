/*
 * Project: Module 5
 * Author: Tre Henderson
 * Email: jdh0204@auburn.edu
 * Date: 2/08/2026
 * Description: style basics.
 */
package edu.au.cpsc.miscstyle;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Part1Application extends Application {

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader loader = new FXMLLoader(
            Part1Application.class.getResource("/edu/au/cpsc/miscstyle/part1.fxml")
    );

    Parent root = loader.load();
    Scene scene = new Scene(root);

    scene.getStylesheets().add(
            Part1Application.class.getResource("/edu/au/cpsc/launcher/style/main.css").toExternalForm()
    );

    stage.setScene(scene);
    stage.show();


  }

  public static void main(String[] args) {
    launch();
  }
}