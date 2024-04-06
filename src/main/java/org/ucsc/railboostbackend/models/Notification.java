package org.ucsc.railboostbackend.models;

// Notification.java

import java.time.LocalDateTime;

public class Notification {

    private Long id;
    private String message;
    private LocalDateTime createdAt;

    public Notification(long id, String message, LocalDateTime timestamp) {
        this.id = id;
        this.message = message;
        this.createdAt = timestamp;
    }

    //getters
    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
    public LocalDateTime getTimestamp() {
        return createdAt;
    }
    //setters
    public void setId(Long id) {
        this.id = id;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setTimestamp(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}

