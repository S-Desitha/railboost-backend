package org.ucsc.railboostbackend.models;

import java.time.LocalDate;

public class Announcement {
    private int id;
    private String title;
    private String category;
    private LocalDate date;
    private String body;

    public String getTitle() {
        return title;
    }
    public String getCategory() {
        return category;
    }
    public LocalDate getDate() {
        return date;
    }
    public int getId() {
        return id;
    }
    public String getBody() {
        return body;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void setBody(String body) { this.body = body; }

}
