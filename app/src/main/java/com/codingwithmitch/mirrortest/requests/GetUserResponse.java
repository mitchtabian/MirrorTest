package com.codingwithmitch.mirrortest.requests;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetUserResponse {

    @SerializedName("data")
    @Expose
    private GetUserDataResponse data;

    @SerializedName("message")
    @Expose
    private String message;

    public GetUserDataResponse getData() {
        return data;
    }

    public void setData(GetUserDataResponse data) {
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
        return "GetUserResponse{" +
                "data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
