package net.abdellahhafid.smartfaceaccess.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AuthenticationSceneController {
    @FXML
    private TextField emailInput;

    @FXML
    private Label errorMssg;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordInput;

    @FXML
    void loginButtonAction(ActionEvent event) {
        String email = emailInput.getText();
        String password = passwordInput.getText();

        if (email.equals("admin") && password.equals("admin")) {
            errorMssg.setText("Login successful");
        } else {
            errorMssg.setText("Login failed");
        }
    }
}
