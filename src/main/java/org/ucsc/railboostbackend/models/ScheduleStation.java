package org.ucsc.railboostbackend.models;

import java.util.Date;

public class ScheduleStation {
    private Short scheduleId;
    private String station;
    private Short stIndex;
    private Date scheduledArrivalTime;
    private Date scheduledDepartureTime;

    public ScheduleStation(Short scheduleId, String station, Short stIndex, Date scheduledArrivalTime, Date scheduledDepartureTime) {
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

    public Date getScheduledArrivalTime() {
        return scheduledArrivalTime;
    }

    public void setScheduledArrivalTime(Date scheduledArrivalTime) {
        this.scheduledArrivalTime = scheduledArrivalTime;
    }

    public Date getScheduledDepartureTime() {
        return scheduledDepartureTime;
    }

    public void setScheduledDepartureTime(Date scheduledDepartureTime) {
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
