package edu.au.cpsc.launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LauncherApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                LauncherApplication.class.getResource("/edu/au/cpsc/launcher/launcher-app.fxml")
        );

        Parent root = loader.load();
        Scene scene = new Scene(root);

        scene.getStylesheets().add(
                LauncherApplication.class.getResource("/edu/au/cpsc/launcher/style/main.css").toExternalForm()
        );

        stage.setTitle("Welcome");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
