package net.abdellahhafid.smartfaceaccess.controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import net.abdellahhafid.smartfaceaccess.models.enums.ToastType;

public class ToastController {

    @FXML
    private StackPane toastPane;

    @FXML
    private Label toastMessage;

    @FXML
    private ProgressBar progressBar;
    public void showToast(String message, ToastType type) {
        toastMessage.setText(message);
        applyStyle(type);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), toastPane);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setOnFinished(event -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(500), toastPane);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setDelay(Duration.seconds(2));
            fadeOut.play();
        });

        fadeIn.play();
    }

    private void applyStyle(ToastType type) {
        switch (type) {
            case ToastType.WARNING:
                toastPane.setStyle("-fx-background-color: #e74c3c; -fx-background-radius: 10; -fx-padding: 10;");
                break;
            case ToastType.ALERT:
                toastPane.setStyle("-fx-background-color: #f39c12; -fx-background-radius: 10; -fx-padding: 10;");
                break;
            case ToastType.INFO:
                toastPane.setStyle("-fx-background-color: #3498db; -fx-background-radius: 10; -fx-padding: 10;");
                break;
            case ToastType.SUCCESS:
                toastPane.setStyle("-fx-background-color: #2ecc71; -fx-background-radius: 10; -fx-padding: 10;");
                break;
            default:
                toastPane.setStyle("-fx-background-color: #333; -fx-background-radius: 10; -fx-padding: 10;");
                break;
        }
    }
}
