package org.ucsc.railboostbackend.models;

public class User {
    private int userId;
    private String username;
    private String password;
    private String fName;
    private String lName;
    private String dob;
    private String gender;
    private String email;
    private String telNo;

    public User(){}

    public User(String username, String password, String fName, String lName, String dob, String gender, String email, String telNo) {
        this.username = username;
        this.password = password;
        this.fName = fName;
        this.lName = lName;
        this.dob = dob;
        this.gender = gender;
        this.email = email;
        this.telNo = telNo;
    }

    public int getUserId() {
        return userId;
    }

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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
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

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", dob='" + dob + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", telNo='" + telNo + '\'' +
                '}';
    }
}
