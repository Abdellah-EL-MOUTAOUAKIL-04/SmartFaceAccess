package net.abdellahhafid.smartfaceaccess.services;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;
import org.opencv.core.Mat;
import org.opencv.core.CvType;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageProcessingServiceImpl implements ImageProcessingService {

    private LBPHFaceRecognizer faceRecognizer;

    public ImageProcessingServiceImpl() {
        // Load OpenCV library
        Loader.load(opencv_java.class);  // Ensure OpenCV is loaded
        faceRecognizer = LBPHFaceRecognizer.create();
        // Uncomment the following line to load known faces from the data folder recently commented
        //loadKnownFaces();
        new Thread(this::loadKnownFaces).start();
    }

    private void loadKnownFaces() {
        String dataFolderPath = "data"; // Folder containing the images
        File folder = new File(dataFolderPath);
        File[] files = folder.listFiles();

        if (files == null || files.length == 0) {
            System.err.println("No images found in the data folder.");
            return;
        }

        List<Mat> images = new ArrayList<>();
        List<Integer> labels = new ArrayList<>();

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".jpg")) {
                // Extract the filename and attempt to extract the label
                String fileName = file.getName();
                String labelString = fileName.split("\\.")[0]; // Assuming the filename is the label

                try {
                    // Check if the label string can be parsed as an integer
                    int label = Integer.parseInt(labelString); // This works if filenames are numeric like "1.jpg"

                    Mat img = Imgcodecs.imread(file.getAbsolutePath(), Imgcodecs.IMREAD_GRAYSCALE);
                    if (img.empty()) {
                        System.err.println("Failed to load image: " + fileName);
                        continue; // Skip empty images
                    }

                    Imgproc.equalizeHist(img, img); // Enhance image quality
                    images.add(img);
                    labels.add(label);

                    System.out.println("Loaded image: " + fileName + " with label: " + label);
                } catch (NumberFormatException e) {
                    // Handle non-numeric labels (e.g., filenames with other formats)
                    System.err.println("Skipping file with invalid or non-numeric label: " + fileName);
                    continue; // Skip files that don't have valid numeric labels
                }
            }
        }

        if (images.isEmpty()) {
            System.err.println("No valid images were loaded.");
            return;
        }

        // Convert labels list to a Mat object
        Mat labelsMat = new Mat(labels.size(), 1, CvType.CV_32SC1);
        for (int i = 0; i < labels.size(); i++) {
            labelsMat.put(i, 0, labels.get(i));
        }

        // Train the recognizer
        faceRecognizer.train(images, labelsMat);
        System.out.println("Training complete. Total samples: " + images.size());
    }

    // Method to recognize face from the captured image and return the person's ID if recognized
    public boolean recognizeFace(Mat faceRegion) {
        if (faceRegion == null || faceRegion.empty()) {
            System.err.println("Provided face region is empty or null.");
            return false; // Return false if the face region is invalid
        }

        // Convert the image to grayscale
        Imgproc.cvtColor(faceRegion, faceRegion, Imgproc.COLOR_BGR2GRAY);

        // Equalize the histogram for better contrast
        Imgproc.equalizeHist(faceRegion, faceRegion);

        // Predict the label for the face image and get the confidence (distance)
        int[] predictedLabel = new int[1];
        double[] confidence = new double[1];
        faceRecognizer.predict(faceRegion, predictedLabel, confidence);

        // Define a threshold for confidence (this threshold value may need to be adjusted)
        double threshold = 45;  // You can experiment with different threshold values

        // If the confidence is below the threshold, consider the recognition valid
        if (confidence[0] < threshold) {
            System.out.println("Face recognized with label: " + predictedLabel[0] + " with confidence: " + confidence[0]);
            return true; // Return true if the face is recognized
        } else {
            System.out.println("Face not recognized. Confidence: " + confidence[0]);
            return false; // Return false if the confidence is too low (not recognized)
        }
    }
}
