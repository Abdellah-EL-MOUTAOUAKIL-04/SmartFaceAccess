package net.abdellahhafid.smartfaceaccess.controllers;


import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.sql.Date;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.ByteArrayInputStream;

public class ClientSpaceIdentifiedController {

    @FXML
    private Text nomComplet, email, telephone, statutAccess, role, dateIdentification;

    @FXML
    private ImageView avatarImage;


    public void setDetails(String nomComplet, String email, String telephone, String statutAccess, String role, Date dateIdentification, byte[] avatarUrl) {
        if (this.nomComplet == null || this.email == null || this.telephone == null || this.statutAccess == null || this.role == null || this.dateIdentification == null || this.avatarImage == null) {
            System.err.println("FXML elements are not initialized. Check fx:id in FXML file.");
            return;
        }

        this.nomComplet.setText(nomComplet != null ? nomComplet : "N/A");
        this.email.setText(email != null ? email : "N/A");
        this.telephone.setText(telephone != null ? telephone : "N/A");
        this.statutAccess.setText(statutAccess != null ? statutAccess : "N/A");

        if ("Autorisé".equals(statutAccess)) {
            this.statutAccess.setFill(Paint.valueOf("#039855"));
        } else {
            this.statutAccess.setFill(Paint.valueOf("#D92D20"));
        }

        this.role.setText(role != null ? role : "N/A");
        this.dateIdentification.setText(dateIdentification != null ? dateIdentification.toString() : "N/A");

        if (avatarUrl != null && avatarUrl.length > 0) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(avatarUrl);
            Image image = new Image(byteArrayInputStream);
            this.avatarImage.setImage(image);
        } else {
            this.avatarImage.setImage(new Image(String.valueOf(ClientSpaceSceneController.class.getResource("assets/images/scanning-women.jpg").toExternalForm())));
        }
    }

}
