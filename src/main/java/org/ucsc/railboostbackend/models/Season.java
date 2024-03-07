package org.ucsc.railboostbackend.models;

import java.time.LocalDate;

public class Season {
    private int id;
    private int userId;
    private String startStation;
    private String endStation;
    private String passengerType;
    private LocalDate startDate;
    private String duration;

    private LocalDate endDate;

    private String trainClass;
    private double totalPrice;
    private String fileName;
    private String status;

    public String getStartStation() {
        return startStation;
    }
    public String getEndStation() {
        return endStation;
    }
    public String getPassengerType(){ return passengerType;}
    public LocalDate getStartDate() {
        return startDate;
    }
    public String getDuration(){ return duration;}
    public LocalDate getEndDate(){ return endDate;}
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
    public String getFileName(){ return  fileName;}
    public String getStatus(){return status;}

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }
    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }
    public void setPassengerType(String passengerType){this.passengerType = passengerType;}
    public void setDuration(String duration){ this.duration = duration;}
    public  void setEndDate(LocalDate endDate){ this.endDate = endDate;}
    public void setId(int id) {
        this.id = id;
    }
    public void setStartDate(LocalDate date) {
        this.startDate = date;
    }
    public void setTrainClass(String trainClass){
        this.trainClass = trainClass;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void setFileName(String fileName){ this.fileName = fileName;}
    public void setStatus(String status){ this.status = status;}
}
