package org.techtown.schooler.network;

import org.techtown.schooler.Model.User;

public class Data {

    // User
    private User user;

    // token
    private String token;


    // IsOverlapped (true, false 를 확인하는 것이다.)
    // true 면 중복 false 면 중복 X
    private Boolean isOverlapped;




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