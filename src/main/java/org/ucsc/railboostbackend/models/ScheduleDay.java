package org.ucsc.railboostbackend.models;

import org.ucsc.railboostbackend.enums.Days;

public class ScheduleDay {
    private Short scheduleId;
    private Days day;

    public ScheduleDay(Short scheduleId, Days day) {
        this.scheduleId = scheduleId;
        this.day = day;
    }

    public ScheduleDay() {
    }

    public Short getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Short scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Days getDay() {
        return day;
    }

    public void setDay(Days day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "\nScheduleDay{" +
                "scheduleId=" + scheduleId +
                ", day=" + day +
                '}';
    }
}
