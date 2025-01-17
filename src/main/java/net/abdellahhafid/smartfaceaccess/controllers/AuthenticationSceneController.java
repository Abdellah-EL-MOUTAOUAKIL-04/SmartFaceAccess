package net.abdellahhafid.smartfaceaccess.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.abdellahhafid.smartfaceaccess.constants.FXMLPathConstants;
import net.abdellahhafid.smartfaceaccess.models.Utilisateur;
import net.abdellahhafid.smartfaceaccess.services.UtilisateurService;
import net.abdellahhafid.smartfaceaccess.services.UtilisateurServiceImpl;
import net.abdellahhafid.smartfaceaccess.utils.SceneManager;
import net.abdellahhafid.smartfaceaccess.utils.ToastUtility;
import net.abdellahhafid.smartfaceaccess.models.enums.ToastType;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AuthenticationSceneController {
    @FXML
    private TextField emailInput;

    @FXML
    private Label errorMssg;

    @FXML
    private Button loginButton;

    @FXML
    private Button goToClientSpaceButton;

    @FXML
    private PasswordField passwordInput;

    private UtilisateurService utilisateurService;

    @FXML
    public void initialize() {
        // Initialize the service
        utilisateurService = new UtilisateurServiceImpl();
    }

    @FXML
    void goToClientSpaceHandler(ActionEvent event) {
        SceneManager sceneManager = new SceneManager((Stage) loginButton.getScene().getWindow());
        sceneManager.switchScene(FXMLPathConstants.CLIENT_SPACE_SCENE);
    }

    @FXML
    void loginButtonAction(ActionEvent event) {
        String email = emailInput.getText().trim();
        String password = passwordInput.getText();

        // Clear previous error message
        errorMssg.setText("");

        if (email.isEmpty() || password.isEmpty()) {
            errorMssg.setText("Veuillez remplir tous les champs.");
            return;
        }

        try {
            List<Utilisateur> users = utilisateurService.findAll();

            Optional<Utilisateur> matchedUser = users.stream()
                    .filter(user -> user.getEmail().equalsIgnoreCase(email))
                    .findFirst();

            if (matchedUser.isPresent()) {
                Utilisateur user = matchedUser.get();

                if (password.equals(user.getPassword())) { // Plain text comparison
                    if ("admin".equalsIgnoreCase(user.getFonctionne())) {
                        errorMssg.setText("Connexion réussie.");
                        ToastUtility.showToast((Stage) loginButton.getScene().getWindow(),
                                "Connexion réussie.", ToastType.SUCCESS);
                        SceneManager sceneManager = new SceneManager((Stage) loginButton.getScene().getWindow());
                        sceneManager.switchScene(FXMLPathConstants.ADMINISTRATOR_SCENE);
                    } else {
                        errorMssg.setText("Accès refusé. Utilisateur non administrateur.");
                        ToastUtility.showToast((Stage) loginButton.getScene().getWindow(),
                                "Accès refusé. Utilisateur non administrateur.", ToastType.ALERT);
                    }
                } else {
                    errorMssg.setText("Mot de passe incorrect.");
                    ToastUtility.showToast((Stage) loginButton.getScene().getWindow(),
                            "Mot de passe incorrect.", ToastType.ALERT);
                }
            } else {
                errorMssg.setText("Aucun utilisateur trouvé avec cet email.");
                ToastUtility.showToast((Stage) loginButton.getScene().getWindow(),
                        "Aucun utilisateur trouvé avec cet email.", ToastType.ALERT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            errorMssg.setText("Erreur de connexion. Veuillez réessayer.");
            ToastUtility.showToast((Stage) loginButton.getScene().getWindow(),
                    "Erreur de connexion. Veuillez réessayer.", ToastType.ALERT);
        }
    }
}
