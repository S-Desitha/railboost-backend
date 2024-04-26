package org.ucsc.railboostbackend.models;

import java.util.List;

public class CancelledSchedulesList {
    private List<CancelledSchedule> cancelledScheduleList;

    public List<CancelledSchedule> getCancelledScheduleList() {
        return cancelledScheduleList;
    }

    public void setCancelledScheduleList(List<CancelledSchedule> cancelledScheduleList) {
        this.cancelledScheduleList = cancelledScheduleList;
    }

    @Override
    public String toString() {
        return "CancelledSchedulesList{" +
                "cancelledScheduleList=" + cancelledScheduleList +
                '}';
    }
}
