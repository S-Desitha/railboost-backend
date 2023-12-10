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

    public JourneyStation() {
        date = LocalDate.of(2023, 12, 9);
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
