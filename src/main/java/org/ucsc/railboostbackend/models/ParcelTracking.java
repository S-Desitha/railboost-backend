package org.ucsc.railboostbackend.models;

public class ParcelTracking {
    private Integer bookingId;
    private String sendingStation;
    private String recoveringStation;
    private String trackingId;
    private String status;
    private String item;

    private Integer scheduleId;

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public String getSendingStation() {
        return sendingStation;
    }

    public void setSendingStation(String sendingStation) {
        this.sendingStation = sendingStation;
    }

    public String getRecoveringStation() {
        return recoveringStation;
    }

    public void setRecoveringStation(String recoveringStation) {
        this.recoveringStation = recoveringStation;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    @Override
    public String toString() {
        return "ParcelTracking{" +
                "bookingId=" + bookingId +
                ", sendingStation='" + sendingStation + '\'' +
                ", recoveringStation='" + recoveringStation + '\'' +
                ", trackingId='" + trackingId + '\'' +
                ", status='" + status + '\'' +
                ", item='" + item + '\'' +
                ", scheduleId=" + scheduleId +
                '}';
    }
}
