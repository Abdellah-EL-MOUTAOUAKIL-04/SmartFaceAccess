package net.abdellahhafid.smartfaceaccess.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.abdellahhafid.smartfaceaccess.constants.FXMLPathConstants;
import net.abdellahhafid.smartfaceaccess.dao.StatistiqueDao;
import net.abdellahhafid.smartfaceaccess.dao.StatistiqueDaoImpl;
import net.abdellahhafid.smartfaceaccess.dao.UtilisateurDao;
import net.abdellahhafid.smartfaceaccess.dao.UtilisateurDaoImpl;
import net.abdellahhafid.smartfaceaccess.models.Log;
import net.abdellahhafid.smartfaceaccess.models.Statistique;
import net.abdellahhafid.smartfaceaccess.models.Utilisateur;
import net.abdellahhafid.smartfaceaccess.services.*;
import net.abdellahhafid.smartfaceaccess.utils.SceneManager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

public class AdministratorSceneController {

    // Existing Panes
    @FXML
    private Pane utilisateurInfosModificationPane;

    @FXML
    private Pane utilisateursPane;

    @FXML
    private Pane ajouterUtilisateurPane;

    @FXML
    private Pane parametresPane;

    @FXML
    private Pane accueilPane;

    @FXML
    private BorderPane rootPane;

    // Sidebar Buttons
    @FXML
    private Button accueilButton;

    @FXML
    private Button utilisateursButton;

    @FXML
    private Button ajouterButton; // Button to show Ajouter Pane

    @FXML
    private Button logsButton;

    @FXML
    private Button parametresButton;

    @FXML
    private Button deconnecterButton;

    // TableView and Columns user
    @FXML
    private TableView<Utilisateur> utilisateursTableView;

    @FXML
    private TableColumn<Utilisateur, String> utilisateurNomColumn;

    @FXML
    private TableColumn<Utilisateur, String> utilisateurEmailColumn;

    @FXML
    private TableColumn<Utilisateur, String> utilisateurTelephoneColumn;

    @FXML
    private TableColumn<Utilisateur, Integer> utilisateurEtageColumn;

    @FXML
    private TableColumn<Utilisateur, String> utilisateurFonctionneColumn;

    @FXML
    private TableColumn<Utilisateur, String> utilisateursEtatAccesColumn;

    @FXML
    private TableColumn<Utilisateur, String> utilisateurModifyColumn;

    @FXML
    private TableColumn<Utilisateur, String> utilisateurDeleteColumn;


    // TableView and Columns Log
    @FXML
    private TableView<Log> logsTableView;

    @FXML
    private TableColumn<Log, Byte[]> accueilUtilisateurColumn;

    @FXML
    private TableColumn<Log, String> accueilEmailColumn;

    @FXML
    private TableColumn<Log, String> accueilTypeUtilisateurColumn;

    @FXML
    private TableColumn<Log, String> accueilDateHeureColumn;

    @FXML
    private TableColumn<Log, String> accueilStatutAccessColumn;

    // Stats
    @FXML
    private Text accueilTotalUsersNumbers;

    @FXML
    private Text accueilTentativeTotalNumber;

    @FXML
    private Label tentativeTotaleReconnaissanceReussi;

    @FXML
    private Label tentativeTotaleReconnaissanceEchouée;

    @FXML
    private Text accueilTentativeToday;

    @FXML
    private Label accueilTentativeAujourdhuiReussi;

    @FXML
    private Label accueilTentativeAujourdhuiEchouees;





    //other controls



    @FXML
    private ImageView utilisateurAvatar;

    @FXML
    private TextField utilisateurNomComplet;

    @FXML
    private TextField utilisateurEmail;

    @FXML
    private TextField utilisateurTelephone;

    @FXML
    private DatePicker utilisateurDateNaissance;

    @FXML
    private ComboBox<String> utilisateurFonction;

    @FXML
    private ComboBox<String> utilisateurStatusAcces;

    @FXML
    private Button saveUserInfosButton;

    @FXML
    private Button userInfosAnnulerButton;

    @FXML
    private TextField ajouterPaneNomComplet;

    @FXML
    private TextField ajouterPaneEmail;

    @FXML
    private TextField ajouterPaneTelephone;

    @FXML
    private DatePicker ajouterPaneDateNaissance;

    @FXML
    private ComboBox<String> ajouterPaneFonction;

    @FXML
    private ComboBox<String> ajouterPaneStatutAccess;

    @FXML
    private ImageView ajouterPaneAvatar;

    @FXML
    private Button ajouterPaneSauvegarderButton;

    @FXML
    private Button ajouterPaneAnnulerButton;

    private Utilisateur currentUserBeingModified;

    // Other Fields
    private final ObservableList<Utilisateur> usersList = FXCollections.observableArrayList();
    private final ObservableList<Log> logList=FXCollections.observableArrayList();


    private UtilisateurService utilisateurService;
    private LogService logService;
    private StatistiqueService statistiqueService;


    private byte[] selectedImageBytes;

    @FXML
    public void initialize() {
        // Initialize the service
        utilisateurService = new UtilisateurServiceImpl(); // Replace with your actual implementation
        logService=new LogServiceImpl();

        // Set initial pane visibility
        showPane(accueilPane);

        // Set active style to accueilButton
        accueilButton.getStyleClass().add("active");

        // Initialize ComboBoxes
        utilisateurFonction.setItems(FXCollections.observableArrayList("gardien", "habitant", "femme de menage"));
        utilisateurStatusAcces.setItems(FXCollections.observableArrayList("autorise", "refuse"));
        ajouterPaneFonction.setItems(FXCollections.observableArrayList("gardien", "habitant", "femme de menage"));
        ajouterPaneStatutAccess.setItems(FXCollections.observableArrayList("autorise", "refuse"));

        // Configure UserTableView columns using PropertyValueFactory
        utilisateurNomColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        utilisateurEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        utilisateurTelephoneColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
        utilisateurEtageColumn.setCellValueFactory(new PropertyValueFactory<>("etage"));
        utilisateurFonctionneColumn.setCellValueFactory(new PropertyValueFactory<>("fonctionne"));
        utilisateursEtatAccesColumn.setCellValueFactory(new PropertyValueFactory<>("accessStatus"));
        utilisateursEtatAccesColumn.setCellFactory(column -> new TableCell<Utilisateur, String>() {
            @Override
            protected void updateItem(String accessStatus, boolean empty) {
                super.updateItem(accessStatus, empty);

                if (empty || accessStatus == null) {
                    setText(null);
                    getStyleClass().removeAll("access-authorise", "access-refuse", "access-default");
                } else {
                    // Remove existing classes
                    getStyleClass().removeAll("access-authorise", "access-refuse", "access-default");

                    switch (accessStatus) {
                        case "autorise":
                            setText("Autorisée");
                            getStyleClass().add("access-authorise");
                            break;
                        case "refuse":
                            setText("Non Autorisée");
                            getStyleClass().add("access-refuse");
                            break;
                        default:
                            setText(accessStatus);
                            getStyleClass().add("access-default");
                            break;
                    }
                }
            }
        });

        // Bind data to UserTableView
        utilisateursTableView.setItems(usersList);

        //Configure LogTableView columns using PropertyValueFactory
        accueilEmailColumn.setCellValueFactory(cellData -> {
            Log log = cellData.getValue();
            if (log.getUtilisateur() != null) {
                return new SimpleObjectProperty<>(log.getUtilisateur().getName());
            } else {
                return new SimpleObjectProperty<>(null);
            }
        });
        accueilDateHeureColumn.setCellValueFactory(new PropertyValueFactory<>("accessTime"));
        accueilStatutAccessColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        accueilStatutAccessColumn.setCellFactory(column -> new TableCell<Log, String>() {
            @Override
            protected void updateItem(String accessStatus, boolean empty) {
                super.updateItem(accessStatus, empty);
                if (empty || accessStatus == null) {
                    setText("");
                    setStyle("");
                } else {
                    switch (accessStatus) {
                        case "succeed":
                            setText("Autorisée");
                            setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                            break;
                        case "failed":
                            setText("Refusée");
                            setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                            break;
                        default:
                            setText(accessStatus);
                            setStyle("-fx-text-fill: black;");
                            break;
                    }
                }
            }
        });

        accueilUtilisateurColumn.setCellFactory(column -> new TableCell<Log, Byte[]>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Byte[] imageBytes, boolean empty) {
                super.updateItem(imageBytes, empty);

                if (empty || imageBytes == null || imageBytes.length == 0) {
                    setGraphic(null); // No image if the cell is empty
                } else {
                    try {
                        // Convert Byte[] to byte[] for ByteArrayInputStream
                        byte[] primitiveBytes = convertToPrimitive(imageBytes);

                        // Convert the byte array to an Image
                        Image image = new Image(new ByteArrayInputStream(primitiveBytes));
                        imageView.setImage(image);

                        // Adjust the dimensions of the image
                        imageView.setFitWidth(50); // Width
                        imageView.setFitHeight(50); // Height
                        imageView.setPreserveRatio(true); // Maintain aspect ratio
                        setGraphic(imageView);
                    } catch (Exception e) {
                        setGraphic(null); // In case of error
                        e.printStackTrace();
                    }
                }
            }

            // Helper method to convert Byte[] to byte[]
            private byte[] convertToPrimitive(Byte[] boxedArray) {
                byte[] primitiveArray = new byte[boxedArray.length];
                for (int i = 0; i < boxedArray.length; i++) {
                    primitiveArray[i] = boxedArray[i];
                }
                return primitiveArray;
            }
        });

        accueilUtilisateurColumn.setCellValueFactory(cellData -> {
            Log log = cellData.getValue();
            if (log.getUtilisateur() != null) {
                // Convert the primitive byte[] to a boxed Byte[] and wrap it in a SimpleObjectProperty
                Byte[] boxedBytes = convertToBoxed(log.getUtilisateur().getFaceImage());
                return new SimpleObjectProperty<>(boxedBytes);
            } else {
                return new SimpleObjectProperty<>(null);
            }
        });


        accueilTypeUtilisateurColumn.setCellValueFactory(cellData -> {
            Log log = cellData.getValue();
            if (log.getUtilisateur() != null) {
                return new SimpleStringProperty(log.getUtilisateur().getFonctionne());
            } else {
                return new SimpleStringProperty("");
            }
        });



        //Bind data to LogTableView
        logsTableView.setItems(logList);

        // Initialize Modify Button Column
        addModifyButtonToTable();

        // Initialize Delete Button Column
        addDeleteButtonToTable();

        // Load data from the service
        loadUsers();

        // Set total users count
        updateUserCount();

        //Load Stats
        loadStats();

        //Load Stats
        loadLogs();

        // Set up ImageView click to upload image for modification Pane
        utilisateurAvatar.setOnMouseClicked(this::handleImageUpload);

        // Set up ImageView click to upload image for add Pane
        ajouterPaneAvatar.setOnMouseClicked(this::handleAjouterPaneImageUpload);

        // Set up selection listener to populate the modification pane
        utilisateursTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                currentUserBeingModified = newSelection;
                populateUserForm(newSelection);
                showModificationPane();
            }
        });
    }

    private Byte[] convertToBoxed(byte[] primitiveArray) {
        if (primitiveArray == null) {
            return null;
        }
        Byte[] boxedArray = new Byte[primitiveArray.length];
        for (int i = 0; i < primitiveArray.length; i++) {
            boxedArray[i] = primitiveArray[i];
        }
        return boxedArray;
    }

    public void loadStats(){
        utilisateurService=new UtilisateurServiceImpl();
        statistiqueService=new StatistiqueServiceImpl();

        List<Utilisateur> users=utilisateurService.findAll();

        Statistique statistique= statistiqueService.findById(0);

        accueilTotalUsersNumbers.setText(String.valueOf(users.size()));

        accueilTentativeTotalNumber.setText("");

        tentativeTotaleReconnaissanceReussi.setText("");

        tentativeTotaleReconnaissanceEchouée.setText("");

        accueilTentativeToday.setText("");

        accueilTentativeAujourdhuiReussi.setText("");

        accueilTentativeAujourdhuiEchouees.setText("");
    }

    void loadLogs() {
        List<Log> logs=logService.findAll();
        System.out.println(logs.toString());
        logList.setAll(logs);
    }

    private void addModifyButtonToTable() {
        Callback<TableColumn<Utilisateur, String>, TableCell<Utilisateur, String>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Utilisateur, String> call(final TableColumn<Utilisateur, String> param) {
                final TableCell<Utilisateur, String> cell = new TableCell() {

                    private final ImageView modifyIcon = new ImageView(new Image(getClass().getResourceAsStream("/assets/images/icons/pencil-02.png")));
                    private final Button btn = new Button();

                    {
                        modifyIcon.setFitHeight(16);
                        modifyIcon.setFitWidth(16);
                        btn.setGraphic(modifyIcon);
                        btn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                        btn.setOnAction((event) -> {
                            Utilisateur user = (Utilisateur) getTableView().getItems().get(getIndex());
                            currentUserBeingModified = user;
                            handleModifyUser(user);
                        });
                    }

                    @Override
                    public void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        utilisateurModifyColumn.setCellFactory(cellFactory);
    }

    private void addDeleteButtonToTable() {
        Callback<TableColumn<Utilisateur, String>, TableCell<Utilisateur, String>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Utilisateur, String> call(final TableColumn<Utilisateur, String> param) {
                final TableCell<Utilisateur, String> cell = new TableCell() {

                    private final ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/assets/images/icons/trash-03.png")));
                    private final Button btn = new Button();

                    {
                        deleteIcon.setFitHeight(16);
                        deleteIcon.setFitWidth(16);
                        btn.setGraphic(deleteIcon);
                        btn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                        btn.setOnAction((event) -> {
                            Utilisateur user = (Utilisateur) getTableView().getItems().get(getIndex());
                            handleDeleteUser(user);
                        });
                    }

                    @Override
                    public void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        utilisateurDeleteColumn.setCellFactory(cellFactory);
    }

    private void handleModifyUser(Utilisateur user) {
        populateUserForm(user);
        showModificationPane();
    }

    private void handleDeleteUser(Utilisateur user) {
        // Confirm deletion
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmer la Suppression");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer l'utilisateur: " + user.getName() + "?");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                utilisateurService.delete(user);
                utilisateursTableView.getItems().remove(user);

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Succès");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Utilisateur supprimé avec succès.");
                successAlert.showAndWait();
            }
        });
    }

    // Method to load users from the service
    private void loadUsers() {
        List<Utilisateur> users = utilisateurService.findAll();
        usersList.setAll(users);
    }

    // Method to update user count display
    private void updateUserCount() {
        accueilTotalUsersNumbers.setText(String.valueOf(usersList.size()));
    }

    // Handle sidebar button actions
    @FXML
    void handleSideBarButtonAction(ActionEvent event) {
        // Identify the clicked button
        Button clickedButton = (Button) event.getSource();

        // Clear active styles
        resetButtonStyles();

        // Add active style to clicked button
        if (!clickedButton.getStyleClass().contains("active")) {
            clickedButton.getStyleClass().add("active");
        }


        // Show the pane corresponding to the clicked button
        if (clickedButton.equals(accueilButton)) {
            showPane(accueilPane);
        } else if (clickedButton.equals(utilisateursButton)) {
            showPane(utilisateursPane);
        } else if (clickedButton.equals(ajouterButton)) {
            showPane(ajouterUtilisateurPane);
        } else if (clickedButton.equals(logsButton)) {
            showAlert("Info", "Logs Pane is not implemented yet.", Alert.AlertType.INFORMATION);
        } else if (clickedButton.equals(parametresButton)) {
            parametresPane.setVisible(true);
        } else if (clickedButton.equals(deconnecterButton)) {
            handleLogout();
        }
    }

    // Reset all button styles to ensure only one is active
    private void resetButtonStyles() {
        clearActiveStyle(accueilButton);
        clearActiveStyle(utilisateursButton);
        clearActiveStyle(ajouterButton);
        clearActiveStyle(logsButton);
        clearActiveStyle(parametresButton);
    }

    private void clearActiveStyle(Button button) {
        button.getStyleClass().removeIf(style -> style.equals("active"));
    }

    private void handleLogout() {
        SceneManager sceneManager = new SceneManager((Stage) deconnecterButton.getScene().getWindow());
        sceneManager.switchScene(FXMLPathConstants.AUTHENTICATION_SCENE);
    }

    private void showPane(Pane paneToShow) {
        accueilPane.setVisible(false);
        utilisateursPane.setVisible(false);
        parametresPane.setVisible(false);
        ajouterUtilisateurPane.setVisible(false);
        utilisateurInfosModificationPane.setVisible(false);

        // Show the desired pane
        paneToShow.setVisible(true);
    }

    // Handle adding a new user from modification Pane (if applicable)
    @FXML
    void handleAjouterUtilisateur(ActionEvent event) {
        showPane(ajouterUtilisateurPane);
    }

    // Handle saving/updating user information
    @FXML
    void handleSaveUserInfos(ActionEvent event) {
        if (currentUserBeingModified == null) {
            showAlert("No Selection", "Please select a user to update.", Alert.AlertType.WARNING);
            return;
        }

        String nomComplet = utilisateurNomComplet.getText();
        String email = utilisateurEmail.getText();
        String telephone = utilisateurTelephone.getText();
        LocalDate dateNaissance = utilisateurDateNaissance.getValue();
        String fonction = utilisateurFonction.getValue();
        String accessStatus = utilisateurStatusAcces.getValue();

        if (nomComplet.isEmpty() || email.isEmpty() || telephone.isEmpty() || fonction == null) {
            showAlert("Validation Error", "All fields are required!", Alert.AlertType.ERROR);
            return;
        }

        String[] nameParts = nomComplet.trim().split(" ");
        if (nameParts.length < 2) {
            showAlert("Validation Error", "Please enter both first name and last name.", Alert.AlertType.ERROR);
            return;
        }

        String prenom = nameParts[0];
        String nom = nameParts[1];

        currentUserBeingModified.setName(prenom + " " + nom);
        currentUserBeingModified.setEmail(email);
        currentUserBeingModified.setNumero(telephone);
        currentUserBeingModified.setFonctionne(fonction);
        currentUserBeingModified.setAccessStatus(accessStatus);

        if (selectedImageBytes != null) {
            currentUserBeingModified.setFaceImage(selectedImageBytes);
        }

        utilisateurService.update(currentUserBeingModified);
        utilisateursTableView.refresh();
        clearUserForm();
        currentUserBeingModified = null; // Reset after saving

        showPane(utilisateursPane); // Return to the utilisateursPane after saving

        showAlert("Success", "User updated successfully.", Alert.AlertType.INFORMATION);
    }

    // Handle deleting a user
    @FXML
    void handleDeleteUser(ActionEvent event) {
        Utilisateur selectedUser = utilisateursTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            utilisateurService.delete(selectedUser);
            usersList.remove(selectedUser);
            updateUserCount();
            showAlert("Success", "User deleted successfully.", Alert.AlertType.INFORMATION);
        } else {
            showAlert("No Selection", "Please select a user to delete.", Alert.AlertType.WARNING);
        }
    }

    // Handle image upload for modification Pane
    @FXML
    void handleImageUpload(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose User Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        Stage stage = (Stage) utilisateurAvatar.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                // Convert selected image to byte array
                selectedImageBytes = Files.readAllBytes(selectedFile.toPath());

                // Display the selected image
                Image image = new Image(new FileInputStream(selectedFile));
                utilisateurAvatar.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load image.", Alert.AlertType.ERROR);
            }
        }
    }

    // Handle image upload for ajouterUtilisateurPane
    @FXML
    void handleAjouterPaneImageUpload(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose User Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        Stage stage = (Stage) ajouterUtilisateurPane.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                // Convert selected image to byte array
                selectedImageBytes = Files.readAllBytes(selectedFile.toPath());

                // Display the selected image
                Image image = new Image(new FileInputStream(selectedFile));
                ajouterPaneAvatar.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load image.", Alert.AlertType.ERROR);
            }
        }
    }

    // Method to populate the user form when a user is selected
    private void populateUserForm(Utilisateur user) {
        utilisateurNomComplet.setText(user.getName());
        utilisateurEmail.setText(user.getEmail());
        utilisateurTelephone.setText(user.getNumero());
        // Assuming date of birth is stored or handled separately; adjust as needed
        utilisateurDateNaissance.setValue(null); // Replace with actual value if available
        utilisateurFonction.setValue(user.getFonctionne());
        utilisateurStatusAcces.setValue(user.getAccessStatus());

        // Display user's image if available
        if (user.getFaceImage() != null && user.getFaceImage().length > 0) {
            Image image = new Image(new ByteArrayInputStream(user.getFaceImage()));
            utilisateurAvatar.setImage(image);
        } else {
            utilisateurAvatar.setImage(new Image(getClass().getResourceAsStream("/assets/images/default.png")));
        }
    }

    // Method to show the modification Pane (optional)
    private void showModificationPane() {
        utilisateurInfosModificationPane.setVisible(true);
        accueilPane.setVisible(false);
        utilisateursPane.setVisible(false);
        parametresPane.setVisible(false);
        ajouterUtilisateurPane.setVisible(false);
    }

    // Method to show alerts
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Optional: Remove header
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Clear the user form after add/update
    private void clearUserForm() {
        utilisateurNomComplet.clear();
        utilisateurEmail.clear();
        utilisateurTelephone.clear();
        utilisateurDateNaissance.setValue(null);
        utilisateurFonction.setValue(null);
        utilisateurAvatar.setImage(new Image(getClass().getResourceAsStream("/assets/images/default.png"))); // Ensure default image exists
        selectedImageBytes = null;
    }

    // Clear the ajouter user form after add
    private void clearAjouterUserForm() {
        ajouterPaneNomComplet.clear();
        ajouterPaneEmail.clear();
        ajouterPaneTelephone.clear();
        ajouterPaneDateNaissance.setValue(null);
        ajouterPaneFonction.setValue(null);
        ajouterPaneAvatar.setImage(new Image(getClass().getResourceAsStream("/assets/images/default.png"))); // Ensure default image exists
        selectedImageBytes = null;
    }

    // Handle canceling user info modification
    @FXML
    void cancelUserInfosAnnulerHandler(ActionEvent event) {
        clearUserForm();
        showPane(utilisateursPane);
    }

    // Handle canceling add user action
    @FXML
    void cancelAjouterUtilisateurHandler(ActionEvent event) {
        clearAjouterUserForm();
        showPane(utilisateursPane);
    }

    // Handle saving a new user via ajouterUtilisateurPane
    @FXML
    void handleAjouterPaneSauvegarder(ActionEvent event) {
        String nomComplet = ajouterPaneNomComplet.getText();
        String email = ajouterPaneEmail.getText();
        String telephone = ajouterPaneTelephone.getText();
        LocalDate dateNaissance = ajouterPaneDateNaissance.getValue(); // If used
        String fonction = ajouterPaneFonction.getValue();

        if (nomComplet.isEmpty() || email.isEmpty() || telephone.isEmpty() || fonction == null) {
            showAlert("Validation Error", "All fields are required!", Alert.AlertType.ERROR);
            return;
        }

        String[] nameParts = nomComplet.trim().split(" ");
        if (nameParts.length < 2) {
            showAlert("Validation Error", "Please enter both first name and last name.", Alert.AlertType.ERROR);
            return;
        }

        Utilisateur newUser = new Utilisateur();
        newUser.setName(nameParts[0] + " " + nameParts[1]);
        newUser.setEmail(email);
        newUser.setNumero(telephone);
        newUser.setEtage(1); // Example default value, adjust as needed
        // fonctionne IN ('gardien', 'habitant', 'femme de menage')
        newUser.setFonctionne(fonction);
        //access_status IN ('autorise', 'refuse')
        newUser.setAccessStatus("autorise"); // Example default value, adjust as needed
        if(selectedImageBytes == null) {
         Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select an image for the user.");
            alert.showAndWait();
        }
        newUser.setFaceImage(selectedImageBytes); // Image bytes

        utilisateurService.save(newUser);
        usersList.add(newUser);
        updateUserCount();
        clearAjouterUserForm();
        ajouterUtilisateurPane.setVisible(false);

        showAlert("Success", "User added successfully.", Alert.AlertType.INFORMATION);
    }

    // Handle refreshing the user list
    @FXML
    void handleRefreshButtonAction(ActionEvent event) {
        loadUsers();
        utilisateursTableView.refresh();
        showAlert("Refreshed", "User list has been refreshed.", Alert.AlertType.INFORMATION);
    }

    // Placeholder methods for other actions
    @FXML
    void handleCancelParametresChanges(ActionEvent event) {
        // Implement parameter cancel logic
    }

    @FXML
    void handleSauvegarderButtonInfosParameters(ActionEvent event) {
        // Implement parameter save logic
    }

    @FXML
    void handleSauvegarderButtonSecurityParameters(ActionEvent event) {
        // Implement security parameter save logic
    }

}
