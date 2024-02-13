package org.ucsc.railboostbackend.models;

import java.time.LocalDate;

public class Booking {
    private int id;
    private int userId;
    private String startStation;
    private String endStation;
    private LocalDate date;
    private String trainClass;
    private int numberOfTickets;
    private double totalPrice;
//    private boolean paid;

    public String getStartStation() {
        return startStation;
    }
    public String getEndStation() {
        return endStation;
    }
    public int getNumberOfTickets() {
        return numberOfTickets;
    }
    public LocalDate getDate() {
        return date;
    }
    public String getTrainClass(){
        return  trainClass;
    }
    public double getTotalPrice() {
        return totalPrice;
    }
    public int getId() {
        return id;
    }
    public int getUserId(){
        return userId;
    }
//    public boolean isPaid() {
//        return paid;
//    }
    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }
    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void setTrainClass(String trainClass){
        this.trainClass = trainClass;
    }
//    public void setPaid(boolean paid) {
//        this.paid = paid;
//    }
    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
