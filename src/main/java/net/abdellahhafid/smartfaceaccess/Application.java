package net.abdellahhafid.smartfaceaccess;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.abdellahhafid.smartfaceaccess.constants.FXMLPathConstants;

import java.io.IOException;
import java.net.URL;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        URL resource = Application.class.getResource(FXMLPathConstants.ADMINISTRATOR_SCENE);
        if (resource == null) {
            throw new RuntimeException("FXML file not found: " + FXMLPathConstants.AUTHENTICATION_SCENE);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("SmartFaceAccess");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}