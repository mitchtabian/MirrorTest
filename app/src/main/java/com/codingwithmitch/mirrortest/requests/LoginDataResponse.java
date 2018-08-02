package com.codingwithmitch.mirrortest.requests;

public class LoginDataResponse {

    private String api_token;
    private String user_token;

    public LoginDataResponse(String api_token, String user_token) {
        this.api_token = api_token;
        this.user_token = user_token;
    }

    public LoginDataResponse() {
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
        return "LoginDataResponse{" +
                ", api_token='" + api_token + '\'' +
                ", user_token='" + user_token + '\'' +
                '}';
    }
}
