package net.abdellahhafid.smartfaceaccess.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import net.abdellahhafid.smartfaceaccess.services.ImageProcessingServiceImpl;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class ClientSpaceWelcomeSceneController {
    @FXML
    private Canvas cameraCanvas;
    @FXML
    private Button goToAuthenticationButton;

    private VideoCapture videoCapture;
    private Timer timer;
    private CascadeClassifier faceCascade;
    private ImageProcessingServiceImpl imageProcessingService;

    @FXML
    public void initialize() {
        imageProcessingService = new ImageProcessingServiceImpl();
        initializeUI();
        //recently commented
        loadFaceCascade();
        startCameraInitialization();
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
        // Display a message while initializing
        Platform.runLater(() -> showInitializingMessage("Starting camera..."));

        // Open the camera and start feed in a background thread
        new Thread(() -> {
            videoCapture = new VideoCapture(0); // Open the default camera
            if (videoCapture.isOpened()) {
                Platform.runLater(() -> {
                    showInitializingMessage("Camera started.");
                    startCameraFeed();
                });
            } else {
                Platform.runLater(() -> showError("Failed to open the camera."));
            }
        }).start();
    }

    private void showInitializingMessage(String message) {
        GraphicsContext gc = cameraCanvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, cameraCanvas.getWidth(), cameraCanvas.getHeight());
        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font("Arial", 16)); // Adjust font size here
        gc.fillText(message, cameraCanvas.getWidth() / 2 - 90, cameraCanvas.getHeight() / 2);
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
                boolean recognized = imageProcessingService.recognizeFace(new Mat(frame, face));

                double scaleX = cameraCanvas.getWidth() / (double) frame.width();
                double scaleY = cameraCanvas.getHeight() / (double) frame.height();

                gc.setStroke(recognized ? Color.GREEN : Color.RED);
                gc.setLineWidth(3);
                gc.strokeRect(face.x * scaleX, face.y * scaleY, face.width * scaleX, face.height * scaleY);
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
        // Navigate to authentication scene (implement navigation logic)
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
