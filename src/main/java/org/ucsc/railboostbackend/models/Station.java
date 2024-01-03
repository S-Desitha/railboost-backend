package org.ucsc.railboostbackend.models;

public class Station {
    private String stationCode;
    private String stationName;
    private String address;
    private String line;
    private String contactNo;

    public Station(String stationCode, String stationName, String address, String line, String contactNo) {
        this.stationCode = stationCode;
        this.stationName = stationName;
        this.address = address;
        this.line = line;
        this.contactNo = contactNo;
    }

    public Station(String stationCode, String stationName) {
        this.stationCode = stationCode;
        this.stationName = stationName;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
}
