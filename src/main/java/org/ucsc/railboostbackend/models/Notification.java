package org.ucsc.railboostbackend.models;

import java.time.LocalDateTime;

import java.time.LocalDateTime;

public class Notification {
    private long id;
    private int userId; // Added userId attribute
    private String title;
    private String message;
    private LocalDateTime timestamp;

    public Notification() {

    }

    // Getters and setters for all attributes

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
