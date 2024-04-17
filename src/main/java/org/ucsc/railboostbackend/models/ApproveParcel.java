package org.ucsc.railboostbackend.models;

public class ApproveParcel {

    private String bookingId;
    private String sendingStation;
    private String senderName;
    private String receiverName;
    private String recoveringStation;
    private String item;
    private Float weight;
    private String status;
    private Float totalprice;
    private Integer itemId;

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getSendingStation() {
        return sendingStation;
    }

    public void setSendingStation(String sendingStation) {
        this.sendingStation = sendingStation;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getRecoveringStation() {
        return recoveringStation;
    }

    public void setRecoveringStation(String recoveringStation) {
        this.recoveringStation = recoveringStation;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Float getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(Float totalprice) {
        this.totalprice = totalprice;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    @Override
    public String toString() {
        return "ApproveParcel{" +
                "bookingId='" + bookingId + '\'' +
                ", sendingStation='" + sendingStation + '\'' +
                ", senderName='" + senderName + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", recoveringStation='" + recoveringStation + '\'' +
                ", item='" + item + '\'' +
                ", weight=" + weight +
                ", status='" + status + '\'' +
                ", totalprice=" + totalprice +
                ", itemId=" + itemId +
                '}';
    }
}