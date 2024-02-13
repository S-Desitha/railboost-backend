package org.ucsc.railboostbackend.models;

public class TicketPrice {
    private int id;
    private String startStation;
    private String endStation;
    private double firstClass;
    private double secondClass;
    private double thirdClass;

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }
    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }
    public void setFirstClass(double firstClass){
        this.firstClass = firstClass;
    }
    public void setSecondClass(double secondClass){
        this.secondClass = secondClass;
    }
    public void setThirdClass(double thirdClass){
        this.thirdClass = thirdClass;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getEndStation() {
        return endStation;
    }
    public String getStartStation() {
        return startStation;
    }

    public double getFirstClass() {
        return firstClass;
    }
    public double getSecondClass() {
        return secondClass;
    }
    public double getThirdClass() {
        return thirdClass;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "TicketPrice{" +
                "id='" + id + '\'' +
                ", startStation='" + startStation + '\'' +
                ", endStation='" + endStation + '\'' +
                ", 1st Class='" + firstClass + '\'' +
                ", 2st Class='" + secondClass + '\'' +
                ", 3st Class='" + thirdClass + '\'' +
                '}';
    }
}
