package net.abdellahhafid.smartfaceaccess.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.stage.Window;
import net.abdellahhafid.smartfaceaccess.controllers.ToastController;
import net.abdellahhafid.smartfaceaccess.models.enums.ToastType;

import java.io.File;
import java.io.IOException;

public class ToastUtility {

    public static void showToast(Window owner, String message, ToastType type) {
        try {
            File fxmlFile = new File("src/main/resources/net/abdellahhafid/smartfaceaccess/views/toast.fxml");
            if (!fxmlFile.exists()) {
                throw new IllegalStateException("FXML file not found at: " + fxmlFile.getAbsolutePath());
            }

            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
            StackPane toastRoot = loader.load();
            ToastController controller = loader.getController();
            controller.showToast(message, type);

            Popup popup = new Popup();
            popup.setAutoHide(true);
            popup.getContent().add(toastRoot);

            popup.setOnShown(e -> {
                double padding = 40;

                popup.setX(owner.getX() + owner.getWidth() - toastRoot.getWidth() - padding);
                popup.setY(owner.getY() + padding);
            });


            popup.show(owner);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load the toast FXML file.");
        }
    }

}
