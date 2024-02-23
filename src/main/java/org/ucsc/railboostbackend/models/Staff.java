package org.ucsc.railboostbackend.models;

public class    Staff {
    private String staffId;
    private int userId;
    private String station;
    private User user;
    private String tempUID;

    public Staff() {
        user = new User();
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getTempUID() {
        return tempUID;
    }

    public void setTempUID(String tempUID) {
        this.tempUID = tempUID;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "staffId='" + staffId + '\'' +
                ", userId=" + userId +
                ", station='" + station + '\'' +
                ", user=" + user +
                '}';
    }
}
