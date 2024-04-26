package org.ucsc.railboostbackend.models;

import java.time.LocalDate;

public class CancelledSchedule {
    private int scheduleId;
    private LocalDate fromDate;
    private LocalDate toDate;

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    @Override
    public String toString() {
        return "ScheduleCancels{" +
                "scheduleId=" + scheduleId +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                '}';
    }
}
