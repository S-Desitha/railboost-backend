package org.ucsc.railboostbackend.models;

public class AddParcelsTOTrain {
    private Integer bookingId;
    private String sendingStation;
    private String recoveringStation;
    private String trackingId;
    private String status;
    private String item;
    private  String deliver_status;
    private  Integer scheduleId;

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

    public String getDeliver_status() {
        return deliver_status;
    }

    public void setDeliver_status(String deliver_status) {
        this.deliver_status = deliver_status;
    }

    public Integer getScheduleId() {
        return this.scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    @Override
    public String toString() {
        return "AddParcelsTOTrain{" +
                "bookingId=" + bookingId +
                ", sendingStation='" + sendingStation + '\'' +
                ", recoveringStation='" + recoveringStation + '\'' +
                ", trackingId='" + trackingId + '\'' +
                ", status='" + status + '\'' +
                ", item='" + item + '\'' +
                ", deliver_status='" + deliver_status + '\'' +
                ", scheduleId=" + scheduleId +
                '}';
    }
}
