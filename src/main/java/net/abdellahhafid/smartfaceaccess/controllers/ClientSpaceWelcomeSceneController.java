package net.abdellahhafid.smartfaceaccess.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import net.abdellahhafid.smartfaceaccess.constants.FXMLPathConstants;
import net.abdellahhafid.smartfaceaccess.utils.SceneManager;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayInputStream;
public class ClientSpaceWelcomeSceneController {
    @FXML
    private Canvas cameraCanvas;

    private VideoCapture videoCapture;
    private boolean cameraActive;
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    @FXML
    private Button goToAuthenticationButton;

    public void initialize() {
        startCamera();
    }
    private void startCamera() {
        videoCapture = new VideoCapture();
        videoCapture.open(0); // Open the default camera (index 0)

        if (videoCapture.isOpened()) {
            cameraActive = true;

            GraphicsContext graphicsContext = cameraCanvas.getGraphicsContext2D();

            // Animation loop for rendering frames
            AnimationTimer timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if (cameraActive) {
                        Mat frame = grabFrame();
                        if (frame != null) {
                            Image imageToShow = matToImage(frame);
                            graphicsContext.drawImage(imageToShow, 0, 0, cameraCanvas.getWidth(), cameraCanvas.getHeight());
                        }
                    }
                }
            };

            timer.start();
        } else {
            System.err.println("Failed to open the camera connection...");
        }
    }

    private Mat grabFrame() {
        Mat frame = new Mat();
        if (videoCapture.isOpened()) {
            videoCapture.read(frame);
            if (!frame.empty()) {
                // Optionally process the frame (e.g., convert to grayscale)
            }
        }
        return frame;
    }

    private Image matToImage(Mat frame) {
        try {
            MatOfByte buffer = new MatOfByte();
            Imgcodecs.imencode(".png", frame, buffer);
            return new Image(new ByteArrayInputStream(buffer.toArray()));
        } catch (Exception e) {
            System.err.println("Failed to convert Mat to Image: " + e.getMessage());
            return null;
        }
    }

    public void stopCamera() {
        cameraActive = false;
        if (videoCapture != null && videoCapture.isOpened()) {
            videoCapture.release();
        }
    }
    @FXML
    void goToAuthenticationHandler(ActionEvent event) {
        SceneManager sceneManager = new SceneManager((Stage) goToAuthenticationButton.getScene().getWindow());
        sceneManager.switchScene(FXMLPathConstants.AUTHENTICATION_SCENE);
    }
}
