package org.ucsc.railboostbackend.models;

import java.util.ArrayList;
import java.util.List;

public class SendParcels {
    List<String> bookingIdList = new ArrayList<>();

    public List<String> getBookingIdList() {
        return bookingIdList;
    }

    private int scheduleId;

    public void setBookingIdList(List<String> bookingIdList) {
        this.bookingIdList = bookingIdList;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    @Override
    public String toString() {
        return "SendParcels{" +
                "bookingIdList=" + bookingIdList +
                ", scheduleId=" + scheduleId +
                '}';
    }
}
