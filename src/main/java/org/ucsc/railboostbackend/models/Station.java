package org.ucsc.railboostbackend.models;

public class Station {
    private String stationCode;
    private String stationName;
    private String address;

    //    previous station as a string
    private String prevStation;
    //    next station as a string
    private String nextStation;
//    setters and getters for previous and next stations


    private String line;
    private String contactNo;

    //    station constructor
    public Station() {
    }

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

    public void setNextStation(String nextStation) {
        this.nextStation = nextStation;
    }

    public String getNextStation() {
        return nextStation;
    }


    public String getPrevStation() {
        return prevStation;
    }

    public void setPrevStation(String prevStation) {
        this.prevStation = prevStation;
    }
}


