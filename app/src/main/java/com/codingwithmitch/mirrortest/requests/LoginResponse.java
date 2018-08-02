package com.codingwithmitch.mirrortest.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("data")
    @Expose
    private LoginDataResponse data;

    @SerializedName("message")
    @Expose
    private String message;

    public LoginDataResponse getData() {
        return data;
    }

    public void setData(LoginDataResponse data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
