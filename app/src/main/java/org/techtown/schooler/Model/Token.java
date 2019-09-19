package org.techtown.schooler.Model;

import com.google.gson.annotations.SerializedName;

public class Token {

    @SerializedName("x-access-token")
    private String token;

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}