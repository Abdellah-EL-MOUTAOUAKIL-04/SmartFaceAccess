package net.abdellahhafid.smartfaceaccess.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.abdellahhafid.smartfaceaccess.constants.FXMLPathConstants;
import net.abdellahhafid.smartfaceaccess.utils.SceneManager;

public class AdministratorSceneController {

    @FXML
    private Button accueilButton;

    @FXML
    private TableColumn<?, ?> accueilDateHeureColumn;

    @FXML
    private TableColumn<?, ?> accueilEmailColumn;

    @FXML
    private ComboBox<?> accueilFilterComboBox;

    @FXML
    private Pane accueilPane;

    @FXML
    private ImageView accueilRefreshButton;

    @FXML
    private TableColumn<?, ?> accueilStatutAccessColumn;

    @FXML
    private Label accueilTentativeAujourdhuiEchouees;

    @FXML
    private Label accueilTentativeAujourdhuiReussi;

    @FXML
    private Text accueilTentativeToday;

    @FXML
    private Text accueilTentativeTotalNumber;

    @FXML
    private Text accueilTotalUsersNumbers;

    @FXML
    private TableColumn<?, ?> accueilTypeUtilisateurColumn;

    @FXML
    private TableColumn<?, ?> accueilUtilisateurColumn;

    @FXML
    private Pane affectationPane;

    @FXML
    private Button ajouterButton;

    @FXML
    private Button ajouterUtilisateurButton;

    @FXML
    private Button annulerButtonParametres;

    @FXML
    private TableView<?> coursEncoursTableView;

    @FXML
    private DatePicker dateBirthDatePickerParametres;

    @FXML
    private Button deconnecterButton;

    @FXML
    private TextField emailFieldParametres;

    @FXML
    private Label errorPasswordUpdate;

    @FXML
    private Text localDateTimeLabelAccueilPane;

    @FXML
    private Button logsButton;

    @FXML
    private Text nameAdminText;

    @FXML
    private TextField newPasswordParametres;

    @FXML
    private TextField nomFieldParametres;

    @FXML
    private Label nombreClasse3EnCours;

    @FXML
    private Label nombreClasse3EnCours1;

    @FXML
    private Label nombreClasse4EnCours;

    @FXML
    private Label nombreClasse4EnCours1;

    @FXML
    private Label nombreLaboratoiresDisponibles;

    @FXML
    private Label nombreSalleCoursDisponibles;

    @FXML
    private Label nombreSalleDeSportDisponibles;

    @FXML
    private TextField oldPasswordParametres;

    @FXML
    private Button parametresButton;

    @FXML
    private Pane parametresPane;

    @FXML
    private ImageView photoAdmin;

    @FXML
    private TextField prenomFieldParametres;

    @FXML
    private TableColumn<?, ?> professeursDeleteActionColumn;

    @FXML
    private TableColumn<?, ?> professeursEditActionColumn;

    @FXML
    private Pane professeursPane;

    @FXML
    private TableColumn<?, ?> professeursVoirActionColumn;

    @FXML
    private Button refreshButton;

    @FXML
    private BorderPane rootPane;

    @FXML
    private Button sauvegarderButtonInfosParametres;

    @FXML
    private Button sauvegarderButtonSecurityParametres;

    @FXML
    private Button saveUserInfosButton;

    @FXML
    private TextField searchTextFieldAccueilPane;

    @FXML
    private TextField telephoneFieldParametres;

    @FXML
    private Label tentativeTotaleReconnaissanceEchouée;

    @FXML
    private Label tentativeTotaleReconnaissanceReussi;

    @FXML
    private Button userInfosAnnulerButton;

    @FXML
    private ImageView utilisateurAvatar;

    @FXML
    private DatePicker utilisateurDateNaissance;

    @FXML
    private TextField utilisateurEmail;

    @FXML
    private TableColumn<?, ?> utilisateurEmailColumn;

    @FXML
    private TableColumn<?, ?> utilisateurEtageColumn;

    @FXML
    private ComboBox<?> utilisateurFonction;

    @FXML
    private TableColumn<?, ?> utilisateurNomColumn;

    @FXML
    private TextField utilisateurNomComplet;

    @FXML
    private TableColumn<?, ?> utilisateurPrenomColumn;

    @FXML
    private TextField utilisateurTelephone;

    @FXML
    private TableColumn<?, ?> utilisateurTelephoneColumn;

    @FXML
    private Button utilisateursButton;

    @FXML
    private TableView<?> utilisateursTableView;

    @FXML
    public void initialize() {
        accueilPane.setVisible(true);
        professeursPane.setVisible(false);
        parametresPane.setVisible(false);
        affectationPane.setVisible(false);

        accueilButton.getStyleClass().add("active");
    }

    @FXML
    void handleSideBarButtonAction(ActionEvent event) {
        // Identify the clicked button
        Button clickedButton = (Button) event.getSource();

        // Explicitly clear the 'active' class from all buttons
        resetButtonStyles();

        // Add the 'active' class to the clicked button
        if (!clickedButton.getStyleClass().contains("active")) {
            clickedButton.getStyleClass().add("active");
        }

        // Hide all panes
        accueilPane.setVisible(false);
        professeursPane.setVisible(false);
        affectationPane.setVisible(false);
        parametresPane.setVisible(false);

        // Show the pane corresponding to the clicked button
        if (clickedButton.equals(accueilButton)) {
            accueilPane.setVisible(true);
        } else if (clickedButton.equals(utilisateursButton)) {
            professeursPane.setVisible(true);
        } else if (clickedButton.equals(logsButton)) {
            affectationPane.setVisible(true); // Replace with logsPane if available
        } else if (clickedButton.equals(parametresButton)) {
            parametresPane.setVisible(true);
        } else if (clickedButton.equals(deconnecterButton)) {
            handleLogout();
        }
    }

    // Reset all button styles to ensure only one is active
    private void resetButtonStyles() {
        // List all sidebar buttons and clear their 'active' class
        clearActiveStyle(accueilButton);
        clearActiveStyle(utilisateursButton);
        clearActiveStyle(logsButton);
        clearActiveStyle(parametresButton);
    }

    // Utility method to clear 'active' style from a button
    private void clearActiveStyle(Button button) {
        button.getStyleClass().removeIf(style -> style.equals("active"));
    }


    private void handleLogout() {
        SceneManager sceneManager = new SceneManager((Stage) deconnecterButton.getScene().getWindow());
        sceneManager.switchScene(FXMLPathConstants.AUTHENTICATION_SCENE);
    }

    @FXML
    void cancelUserInfosAnnulerHandler(ActionEvent event) {

    }

    @FXML
    void handleAjouterUtilisateur(ActionEvent event) {

    }

    @FXML
    void handleCancelParametresChanges(ActionEvent event) {

    }

    @FXML
    void handleRefreshButtonAction(ActionEvent event) {

    }

    @FXML
    void handleSauvegarderButtonInfosParameters(ActionEvent event) {

    }

    @FXML
    void handleSauvegarderButtonSecurityParameters(ActionEvent event) {

    }

    @FXML
    void handleSaveUserInfos(ActionEvent event) {

    }
}
