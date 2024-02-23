package org.ucsc.railboostbackend.models;

import org.ucsc.railboostbackend.controllers.JourneyController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class JourneyStation {
    private LocalDate date;
    private short scheduleId;
    private String station;
    private short stIndex;
    private LocalTime arrivalTime;
    private LocalTime departureTime;
    private LocalTime scheduledArrivalTime;
    private LocalTime scheduledDepartureTime;

    public JourneyStation() {
        date = LocalDate.now();
    }

    public JourneyStation(short scheduleId, String station, short stIndex, LocalTime arrivalTime, LocalTime departureTime, LocalTime scheduledArrivalTime, LocalTime scheduledDepartureTime) {
        this.scheduleId = scheduleId;
        this.station = station;
        this.stIndex = stIndex;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.scheduledArrivalTime = scheduledArrivalTime;
        this.scheduledDepartureTime = scheduledDepartureTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public short getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(short scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public short getStIndex() {
        return stIndex;
    }

    public void setStIndex(short stIndex) {
        this.stIndex = stIndex;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getScheduledArrivalTime() {
        return scheduledArrivalTime;
    }

    public void setScheduledArrivalTime(LocalTime scheduledArrivalTime) {
        this.scheduledArrivalTime = scheduledArrivalTime;
    }

    public LocalTime getScheduledDepartureTime() {
        return scheduledDepartureTime;
    }

    public void setScheduledDepartureTime(LocalTime scheduledDepartureTime) {
        this.scheduledDepartureTime = scheduledDepartureTime;
    }

    @Override
    public String toString() {
        return "JourneyStation{" +
                "date=" + date +
                ", scheduleId=" + scheduleId +
                ", station='" + station + '\'' +
                ", stIndex=" + stIndex +
                ", arrivalTime=" + arrivalTime +
                ", departureTime=" + departureTime +
                '}';
    }
}
