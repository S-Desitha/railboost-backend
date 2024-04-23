package org.ucsc.railboostbackend.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Journey {
    private LocalDate date;
    private short scheduleId;
    private List<JourneyStation> stations = new ArrayList<>();
    private Schedule schedule;

    public Journey(){}
    public Journey(short scheduleId, LocalDate date) {
        this.scheduleId = scheduleId;
        this.date = date;
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

    public List<JourneyStation> getStations() {
        return stations;
    }

    public void setStations(List<JourneyStation> stations) {
        this.stations = stations;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return "Journey{" +
                "date=" + date +
                ", scheduleId=" + scheduleId +
                ", stations=" + stations +
                '}';
    }
}
