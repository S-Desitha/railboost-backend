package org.ucsc.railboostbackend.models;

public class ParcelBooking {

    private String bookingId;
    private  String trackingId;
    private  Integer userId;
    private  String sendingStation;
    private  String senderAddress;
    private  String SenderNIC;
    private  String recoveringStation;
    private  String receiverName;
    private  String receiverNIC;
    private  String receiverAddress;
    private  String receiverTelNo;
    private  String receiverEmail;
    private  String item;
    private  User user;
    private String status;
    private  Integer category;

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSendingStation() {
        return sendingStation;
    }

    public void setSendingStation(String sendingStation) {
        this.sendingStation = sendingStation;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getSenderNIC() {
        return SenderNIC;
    }

    public void setSenderNIC(String senderNIC) {
        SenderNIC = senderNIC;
    }

    public String getRecoveringStation() {
        return recoveringStation;
    }

    public void setRecoveringStation(String recoveringStation) {
        this.recoveringStation = recoveringStation;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverNIC() {
        return receiverNIC;
    }

    public void setReceiverNIC(String receiverNIC) {
        this.receiverNIC = receiverNIC;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverTelNo() {
        return receiverTelNo;
    }

    public void setReceiverTelNo(String receiverTelNo) {
        this.receiverTelNo = receiverTelNo;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "ParcelBooking{" +
                "bookingId='" + bookingId + '\'' +
                ", trackingId='" + trackingId + '\'' +
                ", userId=" + userId +
                ", sendingStation='" + sendingStation + '\'' +
                ", senderAddress='" + senderAddress + '\'' +
                ", SenderNIC='" + SenderNIC + '\'' +
                ", recoveringStation='" + recoveringStation + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", receiverNIC='" + receiverNIC + '\'' +
                ", receiverAddress='" + receiverAddress + '\'' +
                ", receiverTelNo='" + receiverTelNo + '\'' +
                ", receiverEmail='" + receiverEmail + '\'' +
                ", item='" + item + '\'' +
                ", user=" + user +
                ", status='" + status + '\'' +
                ", category=" + category +
                '}';
    }
}
