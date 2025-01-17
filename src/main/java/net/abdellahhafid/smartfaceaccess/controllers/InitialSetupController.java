package net.abdellahhafid.smartfaceaccess.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.abdellahhafid.smartfaceaccess.constants.FXMLPathConstants;
import net.abdellahhafid.smartfaceaccess.models.Utilisateur;
import net.abdellahhafid.smartfaceaccess.services.UtilisateurService;
import net.abdellahhafid.smartfaceaccess.services.UtilisateurServiceImpl;
import net.abdellahhafid.smartfaceaccess.utils.SceneManager;

import java.time.LocalDate;
import java.util.List;

public class InitialSetupController {

    @FXML
    private TextField nomFieldInitialSetup;

    @FXML
    private TextField prenomFieldInitialSetup;

    @FXML
    private TextField telephoneFieldInitialSetup;

    @FXML
    private TextField emailFieldInitialSetup;

    @FXML
    private DatePicker dateBirthDatePickerInitialSetup;

    @FXML
    private PasswordField passwordFieldInitialSetup;

    @FXML
    private PasswordField confirmPasswordFieldInitialSetup;

    @FXML
    private Label errorMessageLabelInitialSetup;

    @FXML
    private Button annulerButtonInitialSetup;

    @FXML
    private Button sauvegarderButtonInitialSetup;

    private UtilisateurService utilisateurService;

    @FXML
    public void initialize() {
        utilisateurService = new UtilisateurServiceImpl();
        errorMessageLabelInitialSetup.setText("");
    }

    /**
     * Handle the "Annuler" button action.
     * Exits the application after confirmation.
     */
    @FXML
    void handleCancelInitialSetup(ActionEvent event) {
        Alert confirmExit = new Alert(Alert.AlertType.CONFIRMATION);
        confirmExit.setTitle("Confirmer la fermeture");
        confirmExit.setHeaderText(null);
        confirmExit.setContentText("Êtes-vous sûr de vouloir quitter la configuration initiale?");
        confirmExit.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.exit(0);
            }
        });
    }

    /**
     * Handle the "Sauvegarder" button action.
     * Validates input and saves the admin account to the database.
     */
    @FXML
    void handleSauvegarderInitialSetup(ActionEvent event) {
        // Clear previous error messages
        errorMessageLabelInitialSetup.setText("");

        // Retrieve input values
        String nom = nomFieldInitialSetup.getText().trim();
        String prenom = prenomFieldInitialSetup.getText().trim();
        String telephone = telephoneFieldInitialSetup.getText().trim();
        String email = emailFieldInitialSetup.getText().trim();
        LocalDate dateNaissance = dateBirthDatePickerInitialSetup.getValue();
        String password = passwordFieldInitialSetup.getText();
        String confirmPassword = confirmPasswordFieldInitialSetup.getText();

        // Validate inputs
        if (nom.isEmpty() || prenom.isEmpty() || telephone.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorMessageLabelInitialSetup.setText("Tous les champs sont obligatoires.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorMessageLabelInitialSetup.setText("Les mots de passe ne correspondent pas.");
            return;
        }

        if (!isValidEmail(email)) {
            errorMessageLabelInitialSetup.setText("Format d'email invalide.");
            return;
        }

        if (!isValidPhoneNumber(telephone)) {
            errorMessageLabelInitialSetup.setText("Format de téléphone invalide.");
            return;
        }

        // Check if an admin already exists using streams on findAll()
        List<Utilisateur> allUsers = utilisateurService.findAll();
        boolean adminExists = allUsers.stream()
                .anyMatch(user -> "admin".equalsIgnoreCase(user.getFonctionne()));

        if (adminExists) {
            errorMessageLabelInitialSetup.setText("Un administrateur existe déjà.");
            return;
        }

        // Create a new Utilisateur instance
        Utilisateur admin = new Utilisateur();
        admin.setName(prenom + " " + nom); // Assuming "Prénom Nom" format
        admin.setEmail(email);
        admin.setNumero(telephone);
        admin.setFonctionne("admin"); // Set role to 'admin' as admin
        admin.setAccessStatus("autorise"); // Grant access by default
        admin.setPassword(hashPassword(password)); // Hash the password for security
        admin.setFaceImage(null); // No image for admin

        // Save the admin to the database
        utilisateurService.save(admin);

        // Optionally, initialize stats or logs for the admin
        // e.g., statistiqueService.initializeStatsForAdmin(admin.getId());

        // Navigate to the main admin interface
        SceneManager sceneManager = new SceneManager((Stage) sauvegarderButtonInitialSetup.getScene().getWindow());
        sceneManager.switchScene(FXMLPathConstants.ADMINISTRATOR_SCENE);
    }

    /**
     * Validates the email format.
     *
     * @param email The email string to validate.
     * @return True if valid, else false.
     */
    private boolean isValidEmail(String email) {
        // Simple regex for email validation
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    /**
     * Validates the phone number format.
     *
     * @param phone The phone number string to validate.
     * @return True if valid, else false.
     */
    private boolean isValidPhoneNumber(String phone) {
        // Regex to accept either:
        // 1. +<country_code> <3_digits>-<6-8_digits> (e.g., +212 674-389348)
        // 2. 0 followed by exactly 9 digits (e.g., 0651990327)
        String phoneRegex = "^(?:\\+\\d{1,3}\\s\\d{3}-\\d{6,8}|0\\d{9})$";
        return phone.matches(phoneRegex);
    }

    /**
     * Hashes the password for secure storage.
     *
     * @param password The plain-text password.
     * @return The hashed password.
     */
    private String hashPassword(String password) {
        // Implement password hashing (e.g., using BCrypt)
        // For simplicity, we'll return the plain password here, but **you must hash passwords in production**
        return password;
    }
}
