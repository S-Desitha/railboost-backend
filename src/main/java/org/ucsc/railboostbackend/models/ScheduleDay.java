package org.ucsc.railboostbackend.models;

import org.ucsc.railboostbackend.enums.Day;

public class ScheduleDay {
    private Short scheduleId;
    private Day day;

    public ScheduleDay(Short scheduleId, Day day) {
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

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
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
