package com.codingwithmitch.mirrortest.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PatchUserResponse {


    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "PatchUserResponse{" +
                "message='" + message + '\'' +
                '}';
    }
}
