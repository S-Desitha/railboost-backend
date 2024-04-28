package org.ucsc.railboostbackend.models;

public class VerifyParcel {

    private String bookingId;
    private String receiverName;
    private String recoveringStation;
    private String receiverEmail;
    private String item;
    private  String receiverNIC;
    private String receiverAddress;

    public String getBookingId() {
        return bookingId;
    }
    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
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

    public String getReceiverNIC() {
        return receiverNIC;
    }

    public void setReceiverNIC(String receiverNIC) {
        this.receiverNIC = receiverNIC;
    }

    @Override
    public String toString() {
        return "VerifyParcel{" +
                "bookingId='" + bookingId + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", recoveringStation='" + recoveringStation + '\'' +
                ", receiverEmail='" + receiverEmail + '\'' +
                ", item='" + item + '\'' +
                ", receiverNIC=" + receiverNIC +
                '}';
    }
}
