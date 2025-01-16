package net.abdellahhafid.smartfaceaccess.models;

import java.sql.Timestamp;

public class Log {
    private int id;
    private Utilisateur utilisateur;
    private Timestamp accessTime; // Heure de la tentative d'accès
    private String status; // "succeed" ou "failed"

    // Constructeur par défaut
    public Log() {}

    // Constructeur avec paramètres
    public Log(int id, Utilisateur utilisateur, Timestamp accessTime, String status) {
        this.id = id;
        this.utilisateur = utilisateur;
        this.accessTime = accessTime;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                '}';
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Timestamp getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Timestamp accessTime) {
        this.accessTime = accessTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}