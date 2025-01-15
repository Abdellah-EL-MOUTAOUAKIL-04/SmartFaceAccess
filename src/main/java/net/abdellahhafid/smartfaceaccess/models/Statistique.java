package net.abdellahhafid.smartfaceaccess.Models;

public class Statistique {
    private int id;
    private int totalAttempts;
    private int successfulAttempts;
    private int failedAttempts;

    public Statistique() {}

    public Statistique(int id, int totalAttempts, int successfulAttempts, int failedAttempts) {
        this.id = id;
        this.totalAttempts = totalAttempts;
        this.successfulAttempts = successfulAttempts;
        this.failedAttempts = failedAttempts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalAttempts() {
        return totalAttempts;
    }

    public void setTotalAttempts(int totalAttempts) {
        this.totalAttempts = totalAttempts;
    }

    public int getSuccessfulAttempts() {
        return successfulAttempts;
    }

    public void setSuccessfulAttempts(int successfulAttempts) {
        this.successfulAttempts = successfulAttempts;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }
}