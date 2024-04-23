package org.ucsc.railboostbackend.models;

import java.util.List;

public class TransitSchedule {
    private int scheduleIndex;
    private List<Schedule> scheduleList;

    public int getScheduleIndex() {
        return scheduleIndex;
    }

    public void setScheduleIndex(int scheduleIndex) {
        this.scheduleIndex = scheduleIndex;
    }

    public List<Schedule> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
    }

    public TransitSchedule(int scheduleIndex, List<Schedule> scheduleList) {
        this.scheduleIndex = scheduleIndex;
        this.scheduleList = scheduleList;
    }
}
