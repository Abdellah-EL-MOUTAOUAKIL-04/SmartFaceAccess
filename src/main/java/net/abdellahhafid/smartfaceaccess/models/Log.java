package net.abdellahhafid.smartfaceaccess.Models;

import java.sql.Timestamp;

public class Log {
    private int id;
    private int userId; // ID de l'utilisateur associé
    private Timestamp accessTime; // Heure de la tentative d'accès
    private String status; // "succeed" ou "failed"

    // Constructeur par défaut
    public Log() {}

    // Constructeur avec paramètres
    public Log(int id, int userId, Timestamp accessTime, String status) {
        this.id = id;
        this.userId = userId;
        this.accessTime = accessTime;
        this.status = status;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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