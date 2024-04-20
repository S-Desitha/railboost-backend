package org.ucsc.railboostbackend.models;

import java.time.LocalDate;

public class User {
    private int userId;
    private String username;
    private String password;
    private String fName;
    private String lName;
    private LocalDate dob;
    private String gender;
    private String email;
    private String telNo;
    private String dp;
    private String homeStation;
    private String homeStCode;
    private Role role;
    private boolean isStaff;
    private String tempUID;


    public int getUserId() {
        return userId;
    }
    public String getTempUID(){return tempUID;}
    public void setTempUID(String tempUID){ this.tempUID=tempUID;}

    public String getDp() {
        return dp;
    }

    public String getHomeStation(){ return homeStation; }
    public String getHomeStCode(){ return homeStCode; }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setDp(String dp){ this.dp=dp; }
    public void setHomeStation(String homeStation) {
        this.homeStation = homeStation;
    }
    public void setHomeStCode(String homeStCode) {
        this.homeStCode = homeStCode;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isStaff() {
        return isStaff;
    }

    public void setStaff(boolean staff) {
        isStaff = staff;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", dob=" + dob +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", telNo='" + telNo + '\'' +
                ", role='" + role + '\'' +
                ", isStaff=" + isStaff +
                '}';
    }
}
