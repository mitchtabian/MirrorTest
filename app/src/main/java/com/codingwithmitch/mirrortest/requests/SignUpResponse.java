package com.codingwithmitch.mirrortest.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUpResponse {

    @SerializedName("data")
    @Expose
    private SignUpDataResponse data;

    @SerializedName("message")
    @Expose
    private String message;

    public SignUpDataResponse getData() {
        return data;
    }

    public void setData(SignUpDataResponse data) {
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
        return "SignUpResponse{" +
                "data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
