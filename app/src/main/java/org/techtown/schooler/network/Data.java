package org.techtown.schooler.network;

import org.techtown.schooler.Model.User;

public class Data {
    private User user;
    private Token token;

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}