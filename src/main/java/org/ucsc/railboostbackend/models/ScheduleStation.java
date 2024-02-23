package org.ucsc.railboostbackend.models;

import java.time.LocalTime;

public class ScheduleStation extends BaseModel {
    private Short scheduleId;
    private String station;
    private Short stIndex;
    private LocalTime scheduledArrivalTime;
    private LocalTime scheduledDepartureTime;

    public ScheduleStation(Short scheduleId, String station, Short stIndex, LocalTime scheduledArrivalTime, LocalTime scheduledDepartureTime) {
        this.scheduleId = scheduleId;
        this.station = station;
        this.stIndex = stIndex;
        this.scheduledArrivalTime = scheduledArrivalTime;
        this.scheduledDepartureTime = scheduledDepartureTime;
    }


    public ScheduleStation() {}

    public Short getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Short scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public Short getStIndex() {
        return stIndex;
    }

    public void setStIndex(Short stIndex) {
        this.stIndex = stIndex;
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
        return "\n\tScheduleStation{" +
                "scheduleId=" + scheduleId +
                ", station='" + station + '\'' +
                ", stIndex=" + stIndex +
                ", scheduledArrivalTime='" + scheduledArrivalTime + '\'' +
                ", scheduledDepartureTime='" + scheduledDepartureTime + '\'' +
                '}';
    }
}
