package net.abdellahhafid.smartfaceaccess.models;

public class Utilisateur {
    private int id;
    private String name;
    private String email;
    private String numero;
    private byte[] faceImage;
    private int etage;
    private String fonctionne;
    private String accessStatus;

    public Utilisateur() {}

    public Utilisateur(int id, String name, String email, String numero, byte[] faceImage, int etage, String fonctionne, String accessStatus) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.numero = numero;
        this.faceImage = faceImage;
        this.etage = etage;
        this.fonctionne = fonctionne;
        this.accessStatus = accessStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public byte[] getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(byte[] faceImage) {
        this.faceImage = faceImage;
    }

    public int getEtage() {
        return etage;
    }

    public void setEtage(int etage) {
        this.etage = etage;
    }

    public String getFonctionne() {
        return fonctionne;
    }

    public void setFonctionne(String fonctionne) {
        this.fonctionne = fonctionne;
    }

    public String getAccessStatus() {
        return accessStatus;
    }

    public void setAccessStatus(String accessStatus) {
        this.accessStatus = accessStatus;
    }
}