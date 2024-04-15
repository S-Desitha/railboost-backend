package org.ucsc.railboostbackend.models;

public class Login {
    private String username;
    private int userId;
    private String name;
    private String password;
    private boolean isSuccessful;
    private Role role;
    private String jwt;
//    private int userId;


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

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public Role getRole() {
        return role;
    }
    public String getName() {return name;}
    public void setName(String name) {this.name =name;}
    public void setUserId(int userId){this.userId = userId;}
    public int getUserId(){return userId;}

    public void setRole(Role role) {
        this.role = role;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public String toString() {
        return "Login{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", isSuccessful=" + isSuccessful +
                '}';
    }


    public void setUserID(int userId) {
        this.userId = userId;
    }
}
