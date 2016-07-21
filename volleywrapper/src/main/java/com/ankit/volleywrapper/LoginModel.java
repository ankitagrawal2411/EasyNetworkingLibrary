package com.ankit.volleywrapper;

/**
 * Created by user on 18/4/16.
 */
public class LoginModel {
    private String userName;
    private String password;
    private String deviceId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
