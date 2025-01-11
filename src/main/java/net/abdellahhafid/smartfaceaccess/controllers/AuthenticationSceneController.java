package net.abdellahhafid.smartfaceaccess.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.abdellahhafid.smartfaceaccess.constants.FXMLPathConstants;
import net.abdellahhafid.smartfaceaccess.models.enums.ToastType;
import net.abdellahhafid.smartfaceaccess.utils.SceneManager;
import net.abdellahhafid.smartfaceaccess.utils.ToastUtility;

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

    @FXML
    void goToClientSpaceHandler(ActionEvent event) {
        SceneManager sceneManager = new SceneManager((Stage) loginButton.getScene().getWindow());
        sceneManager.switchScene(FXMLPathConstants.CLIENT_SPACE_WELCOME_SCENE);
    }

    @FXML
    void loginButtonAction(ActionEvent event) {
        String email = emailInput.getText();
        String password = passwordInput.getText();

        if (email.equals("admin") && password.equals("admin")) {
            errorMssg.setText("Login successful");
            ToastUtility.showToast((Stage) loginButton.getScene().getWindow(), "Login successful", ToastType.SUCCESS);
        } else {
            errorMssg.setText("Login failed");
            ToastUtility.showToast((Stage) loginButton.getScene().getWindow(), "Login unsuccessful", ToastType.ALERT);
        }
    }


}
