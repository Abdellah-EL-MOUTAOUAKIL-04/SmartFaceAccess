package net.abdellahhafid.smartfaceaccess.services;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.face.LBPHFaceRecognizer;
import net.abdellahhafid.smartfaceaccess.models.Utilisateur;

import java.util.ArrayList;
import java.util.List;

public class ImageProcessingServiceImpl implements ImageProcessingService {

    private final LBPHFaceRecognizer faceRecognizer;
    private final UtilisateurService utilisateurService;

    public ImageProcessingServiceImpl(UtilisateurService utilisateurService) {
        // Charger OpenCV
        Loader.load(opencv_java.class);
        this.faceRecognizer = LBPHFaceRecognizer.create();
        this.utilisateurService = utilisateurService;
        loadKnownFaces();
    }

    private void loadKnownFaces() {
        List<Utilisateur> utilisateurs = utilisateurService.findAll();

        if (utilisateurs.isEmpty()) {
            System.err.println("Aucun utilisateur trouvé dans la base de données.");
            return;
        }

        List<Mat> imagesList = new ArrayList<>();
        List<Utilisateur> utilisateursWithoutAdmin = utilisateurs.stream()
                .filter(user -> !"admin".equalsIgnoreCase(user.getFonctionne()))
                .toList();
        Mat labelsMat = new Mat(utilisateursWithoutAdmin.size(), 1, CvType.CV_32SC1);
        int labelIndex = 0;

        for (Utilisateur utilisateur : utilisateursWithoutAdmin) {
            byte[] faceBlob = utilisateur.getFaceImage();
            if (faceBlob == null || faceBlob.length == 0) {
                System.err.println("L'utilisateur avec ID " + utilisateur.getId() + " n'a pas d'image.");
                continue;
            }

            Mat faceImage = convertBlobToMat(faceBlob);
            if (faceImage.empty()) {
                System.err.println("Impossible de charger l'image de l'utilisateur avec ID " + utilisateur.getId());
                continue;
            }

            // Prétraitement de l'image
            Imgproc.equalizeHist(faceImage, faceImage);

            // Ajouter l'image et son label
            imagesList.add(faceImage);
            labelsMat.put(labelIndex, 0, utilisateur.getId());
            labelIndex++;

            System.out.println("Visage de l'utilisateur avec ID " + utilisateur.getId() + " chargé.");
        }

        // Mise à jour du modèle de reconnaissance
        if (imagesList.isEmpty()) {
            System.err.println("Aucune image valide trouvée pour l'entraînement.");
            return;
        }

        faceRecognizer.train(imagesList, labelsMat);
    }

    public Utilisateur recognizeFace(Mat faceRegion) {
        if (faceRegion.empty()) {
            System.err.println("La région du visage est vide ou invalide.");
            return null;
        }

        // Prétraiter l'image du visage
        Mat preprocessedFace = preprocessFace(faceRegion);

        // Effectuer la prédiction
        int[] predictedLabel = new int[1];
        double[] confidence = new double[1];
        faceRecognizer.predict(preprocessedFace, predictedLabel, confidence);

        double threshold = 55.0; // Ajuster le seuil de confiance selon vos besoins

        if (confidence[0] < threshold) {
            System.out.println("Visage reconnu avec ID : " + predictedLabel[0] + " et confiance : " + confidence[0]);
            return utilisateurService.findById(predictedLabel[0]);
        } else {
            System.out.println("Visage non reconnu. Confiance : " + confidence[0]);
            return null;
        }
    }

    private Mat preprocessFace(Mat faceRegion) {
        // Convertir l'image en niveaux de gris et égaliser l'histogramme
        Mat grayFace = new Mat();
        Imgproc.cvtColor(faceRegion, grayFace, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(grayFace, grayFace);
        return grayFace;
    }

    private Mat convertBlobToMat(byte[] blob) {
        // Conversion d'un tableau d'octets en Mat
        MatOfByte matOfByte = new MatOfByte(blob);
        Mat decodedImage = Imgcodecs.imdecode(matOfByte, Imgcodecs.IMREAD_GRAYSCALE);
        if (decodedImage.empty()) {
            System.err.println("Erreur lors du décodage de l'image.");
        }
        return decodedImage;
    }
}
