package com.cliproco.model;

import java.time.LocalDateTime;

public class Notification {
    private Integer id;
    private String message;
    private String type;
    private LocalDateTime dateCreation;
    private boolean lu;

    public Notification() {
        this.dateCreation = LocalDateTime.now();
        this.lu = false;
    }

    public Notification(String message, String type) {
        this();
        this.message = message;
        this.type = type;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public boolean isLu() {
        return lu;
    }

    public void setLu(boolean lu) {
        this.lu = lu;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", type='" + type + '\'' +
                ", dateCreation=" + dateCreation +
                ", lu=" + lu +
                '}';
    }
} 