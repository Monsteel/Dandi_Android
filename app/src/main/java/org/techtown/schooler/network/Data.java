package org.techtown.schooler.network;

import org.techtown.schooler.Model.User;

public class Data {

    // User
    private User user;

    // token
    private String token;

    private String authCode;

    // IsOverlapped (true, false 를 확인하는 것이다.)
    // true 면 중복 false 면 중복 X
    private Boolean isOverlapped;


    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getOverlapped() {
        return isOverlapped;
    }

    public void setOverlapped(Boolean overlapped) {
        isOverlapped = overlapped;
    }





}