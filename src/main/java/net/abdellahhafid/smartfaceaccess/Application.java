package net.abdellahhafid.smartfaceaccess;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.abdellahhafid.smartfaceaccess.constants.FXMLPathConstants;
import net.abdellahhafid.smartfaceaccess.models.Utilisateur;
import net.abdellahhafid.smartfaceaccess.services.UtilisateurService;
import net.abdellahhafid.smartfaceaccess.services.UtilisateurServiceImpl;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        UtilisateurService utilisateurService = new UtilisateurServiceImpl();

        List<Utilisateur> allUsers = utilisateurService.findAll();

        boolean adminExists = allUsers.stream()
                .anyMatch(user -> "admin".equalsIgnoreCase(user.getFonctionne()));

        String fxmlPath;
        if (adminExists) {
            fxmlPath = FXMLPathConstants.AUTHENTICATION_SCENE;
        } else {
            fxmlPath = FXMLPathConstants.INITIAL_SETUP_SCENE;
        }

        // Load the appropriate FXML resource
        URL resource = Application.class.getResource(fxmlPath);
        if (resource == null) {
            throw new RuntimeException("FXML file not found: " + fxmlPath);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);

        // Set up and display the stage
        stage.setTitle("SmartFaceAccess");
        stage.setScene(scene);
        stage.setResizable(false); // Optional: Prevent window resizing
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}