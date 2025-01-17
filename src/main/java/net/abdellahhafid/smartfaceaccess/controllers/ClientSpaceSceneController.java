package net.abdellahhafid.smartfaceaccess.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.abdellahhafid.smartfaceaccess.constants.FXMLPathConstants;
import net.abdellahhafid.smartfaceaccess.models.Log;
import net.abdellahhafid.smartfaceaccess.models.Utilisateur;
import net.abdellahhafid.smartfaceaccess.services.ImageProcessingServiceImpl;
import net.abdellahhafid.smartfaceaccess.services.LogService;
import net.abdellahhafid.smartfaceaccess.services.LogServiceImpl;
import net.abdellahhafid.smartfaceaccess.services.UtilisateurServiceImpl;
import net.abdellahhafid.smartfaceaccess.utils.SceneManager;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

public class ClientSpaceSceneController {
    @FXML
    private Canvas cameraCanvas;

    @FXML
    private Button goToAuthenticationButton;

    @FXML
    private StackPane dynamicContentPane;

    private VideoCapture videoCapture;
    private Timer timer;
    private CascadeClassifier faceCascade;
    private ImageProcessingServiceImpl imageProcessingService;
    private LogService logService;

    @FXML
    public void initialize() {
        imageProcessingService = new ImageProcessingServiceImpl(new UtilisateurServiceImpl());
        logService = new LogServiceImpl();
        initializeUI();
        loadFaceCascade();
        startCameraInitialization();
        // switchToIdentified("John Doe", "johndoe@email.com", "+123456789",
        //        "Non autorisé", "Client", "2021-07-01 12:00:00", String.valueOf(ClientSpaceSceneController.class.getResource("/assets/images/scanning-women.jpg").toExternalForm()));
        switchToWelcome();
        // switchToUnidentified();

        Platform.runLater(() -> {
            Stage stage = (Stage) cameraCanvas.getScene().getWindow();
            stage.setOnCloseRequest(event -> {
                stopCamera();
                shutdown();
            });
        });
    }

    public void switchToWelcome() {
        loadContent("client-space-welcome-section.fxml");
    }

    public void switchToIdentified(String nomComplet, String email, String telephone, String statutAccess, String role, Date dateIdentification, byte[] avatarUrl) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/components/client-space-identified-section.fxml"));
            Parent identifiedSection = loader.load();

            ClientSpaceIdentifiedController controller = loader.getController();
            controller.setDetails(nomComplet, email, telephone, statutAccess, role, dateIdentification, avatarUrl);

            dynamicContentPane.getChildren().clear();
            dynamicContentPane.getChildren().add(identifiedSection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToUnidentified() {
        loadContent("client-space-unidentified-section.fxml");
    }

    private void loadContent(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/components/" + fxmlFile));
            dynamicContentPane.getChildren().clear();
            dynamicContentPane.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeUI() {
        GraphicsContext gc = cameraCanvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, cameraCanvas.getWidth(), cameraCanvas.getHeight());

        // Set a larger font size
        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font("Arial", 20)); // Adjust font size here
        gc.fillText("Initializing camera...", cameraCanvas.getWidth() / 2 - 90, cameraCanvas.getHeight() / 2);
    }

    private void loadFaceCascade() {
        InputStream cascadeStream = getClass().getResourceAsStream("/haarcascade_frontalface_default.xml");
        if (cascadeStream != null) {
            try {
                File tempFile = File.createTempFile("haarcascade", ".xml");
                try (FileOutputStream out = new FileOutputStream(tempFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = cascadeStream.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
                faceCascade = new CascadeClassifier(tempFile.getAbsolutePath());
                if (!faceCascade.load(tempFile.getAbsolutePath())) {
                    System.err.println("Failed to load Haar cascade file.");
                }
                tempFile.deleteOnExit();
            } catch (IOException e) {
                System.err.println("Error loading Haar cascade: " + e.getMessage());
            }
        } else {
            System.err.println("Haar cascade file not found.");
        }
    }

    private void startCameraInitialization() {
        new Thread(() -> {
            videoCapture = new VideoCapture(0);
            if (videoCapture.isOpened()) {
                Platform.runLater(this::startCameraFeed);
            } else {
                Platform.runLater(() -> showError("Failed to open the camera."));
            }
        }).start();
    }

    private void showError(String message) {
        GraphicsContext gc = cameraCanvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, cameraCanvas.getWidth(), cameraCanvas.getHeight());
        gc.setFill(Color.RED);
        gc.fillText(message, cameraCanvas.getWidth() / 2 - 60, cameraCanvas.getHeight() / 2);
    }

    private void startCameraFeed() {
        GraphicsContext gc = cameraCanvas.getGraphicsContext2D();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (videoCapture.isOpened()) {
                    Mat frame = new Mat();
                    if (videoCapture.read(frame)) {
                        processAndDisplayFrame(frame, gc);
                        frame.release();
                    }
                }
            }
        }, 0, 33); // Approximately 30 FPS
    }

    private void processAndDisplayFrame(Mat frame, GraphicsContext gc) {
        Mat convertedFrame = convertToFXFormat(frame);
        if (convertedFrame != null) {
            detectAndDrawFaces(convertedFrame, gc);
        }
    }

    private Mat convertToFXFormat(Mat frame) {
        Mat converted = new Mat();
        Imgproc.cvtColor(frame, converted, Imgproc.COLOR_BGR2BGRA);
        return converted;
    }

    private Utilisateur previousUser = null;  // Variable pour mémoriser l'utilisateur précédent

    // Declare this variable at the class level, outside of the method
    private Long previousLogTimestamp = null;

    private void detectAndDrawFaces(Mat frame, GraphicsContext gc) {
        Mat grayFrame = new Mat();
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGRA2GRAY);
        Imgproc.equalizeHist(grayFrame, grayFrame);

        MatOfRect faces = new MatOfRect();
        faceCascade.detectMultiScale(grayFrame, faces, 1.1, 7, 0, new Size(60, 60), new Size(500, 500));
        Rect[] facesArray = faces.toArray();

        Platform.runLater(() -> {
            gc.clearRect(0, 0, cameraCanvas.getWidth(), cameraCanvas.getHeight());
            drawFrame(gc, frame);

            for (Rect face : facesArray) {
                Utilisateur recognizedUser = imageProcessingService.recognizeFace(new Mat(frame, face));

                double scaleX = cameraCanvas.getWidth() / (double) frame.width();
                double scaleY = cameraCanvas.getHeight() / (double) frame.height();

                gc.setStroke(recognizedUser != null ? Color.GREEN : Color.RED);
                gc.setLineWidth(3);
                gc.strokeRect(face.x * scaleX, face.y * scaleY, face.width * scaleX, face.height * scaleY);

                // Vérification si previousUser est null avant d'effectuer la comparaison
                if (recognizedUser != null) {
                    if (previousUser == null || recognizedUser.getId() != previousUser.getId()) {
                        previousUser = recognizedUser;

                        // Get the current time
                        long currentTime = System.currentTimeMillis();

                        // Check if a log was added within the last minute (60,000 milliseconds)
                        if (previousLogTimestamp == null || (currentTime - previousLogTimestamp > 60000)) {
                            // Switch to identified
                            switchToIdentified(
                                    recognizedUser.getName(),
                                    recognizedUser.getEmail(),
                                    recognizedUser.getNumero(),
                                    recognizedUser.getAccessStatus(),
                                    recognizedUser.getFonctionne(),
                                    new Date(currentTime),
                                    recognizedUser.getFaceImage()
                            );

                            // Adding user to log
                            Log log = new Log();
                            log.setUtilisateur(recognizedUser);
                            log.setAccessTime(new Timestamp(currentTime));
                            log.setStatus(recognizedUser.getAccessStatus().equals("autorise") ? "succeed" : "failed");
                            logService.save(log);
                            System.out.println("switchToIdentified and added to logs");

                            // Update the previous log timestamp
                            previousLogTimestamp = currentTime;
                        } else {
                            System.out.println("Log skipped: User added less than a minute ago.");
                        }
                    }
                } else if (previousUser != null) {
                    previousLogTimestamp=null;
                    // Aucun utilisateur reconnu mais un utilisateur précédent est stocké
                    switchToUnidentified();
                    previousUser = null;  // Réinitialiser l'utilisateur précédent
                    System.out.println("switchToUnidentified ");
                }
            }
        });
    }


    private void drawFrame(GraphicsContext gc, Mat frame) {
        int width = frame.width();
        int height = frame.height();
        byte[] buffer = new byte[width * height * 4];
        frame.get(0, 0, buffer);

        WritableImage image = new WritableImage(width, height);
        PixelWriter writer = image.getPixelWriter();
        writer.setPixels(0, 0, width, height, javafx.scene.image.PixelFormat.getByteBgraInstance(), buffer, 0, width * 4);

        gc.drawImage(image, 0, 0, cameraCanvas.getWidth(), cameraCanvas.getHeight());
    }

    @FXML
    private void goToAuthenticationHandler() {
        stopCamera();
        SceneManager sceneManager = new SceneManager((Stage) goToAuthenticationButton.getScene().getWindow());
        sceneManager.switchScene(FXMLPathConstants.AUTHENTICATION_SCENE);
    }

    @FXML
    public void stopCamera() {
        if (timer != null) {
            timer.cancel();
        }
        if (videoCapture != null && videoCapture.isOpened()) {
            videoCapture.release();
        }
    }

    public void shutdown() {
        stopCamera();
    }
}
