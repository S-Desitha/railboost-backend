package org.ucsc.railboostbackend.models;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Schedule {
    private short scheduleId;
    private String startStation;
    private String startStationName;
    private String endStation;
    private String endStationName;
    private String trainType;
    private String trainId;
    private String speed;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate date;
    private List<ScheduleStation> stations = new ArrayList<ScheduleStation>();
    private List<ScheduleDay> days = new ArrayList<ScheduleDay>();

    public Schedule(short scheduleId) {
        this.scheduleId = scheduleId;
    }

//    public Schedule() {
//    }

    public short getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(short scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public List<ScheduleStation> getStations() {
        return stations;
    }

    public void setStations(List<ScheduleStation> stations) {
        this.stations = stations;
    }

    public List<ScheduleDay> getDays() {
        return days;
    }

    public void setDays(List<ScheduleDay> days) {
        this.days = days;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStartStationName() {
        return startStationName;
    }

    public void setStartStationName(String startStationName) {
        this.startStationName = startStationName;
    }

    public String getEndStationName() {
        return endStationName;
    }

    public void setEndStationName(String endStationName) {
        this.endStationName = endStationName;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "scheduleId=" + scheduleId +
                ", startStation='" + startStation + '\'' +
                ", endStation='" + endStation + '\'' +
                ", trainId='" + trainId + '\'' +
                ", speed='" + speed + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", stations=" + stations +
//                ", days=" + days +
                '}';
    }
}



