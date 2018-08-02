package com.codingwithmitch.mirrortest.requests;

public class SignUpDataResponse {

    private String user_uuid;
    private String api_token;
    private String user_token;

    public SignUpDataResponse(String user_uuid, String api_token, String user_token) {
        this.user_uuid = user_uuid;
        this.api_token = api_token;
        this.user_token = user_token;
    }

    public SignUpDataResponse() {
    }

    public String getUser_uuid() {
        return user_uuid;
    }

    public void setUser_uuid(String user_uuid) {
        this.user_uuid = user_uuid;
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    @Override
    public String toString() {
        return "SignUpDataResponse{" +
                "user_uuid='" + user_uuid + '\'' +
                ", api_token='" + api_token + '\'' +
                ", user_token='" + user_token + '\'' +
                '}';
    }
}
