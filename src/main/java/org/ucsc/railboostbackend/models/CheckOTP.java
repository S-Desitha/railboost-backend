package org.ucsc.railboostbackend.models;

public class CheckOTP {
    private int bookingId;
    private int OTP;

    public CheckOTP(int bookingId, int OTP) {
        this.bookingId = bookingId;
        this.OTP = OTP;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getOTP() {
        return OTP;
    }

    public void setOTP(int OTP) {
        this.OTP = OTP;
    }

    @Override
    public String toString() {
        return "CheckOTP{" +
                "bookingId=" + bookingId +
                ", OTP=" + OTP +
                '}';
    }
}
