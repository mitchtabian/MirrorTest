package com.codingwithmitch.mirrortest;

public class Constants {

    // Fragments
    public static final String FRAGMENT_LOGIN = "com.codingwithmitch.mirrortest.login";
    public static final String FRAGMENT_SIGN_UP = "com.codingwithmitch.mirrortest.sign_up";
    public static final String FRAGMENT_PROFILE = "com.codingwithmitch.mirrortest.profile";
    public static final String FRAGMENT_EDIT_PROFILE = "com.codingwithmitch.mirrortest.edit_profile";

    // Server Requests
    public static String BASE_URL = "https://dev.refinemirror.com/api/v1/";
    public static String AUTH_SIGN_UP = "signup";
    public static String AUTH_LOGIN = "login";
    public static String PATCH_URL = "me";
    public static String API_TYPE_JSON = "json";
    public static String RESPONSE_SIGN_UP_SUCCESS = "ok";
    public static String RESPONSE_LOGIN_SUCCESS = "ok";
    public static String RESPONSE_GET_USER_INFO_SUCCESS = "ok";
    public static String RESPONSE_PATCH_USER_INFO_SUCCESS = "ok";


    public static final int SOFT_TTL = 2;
    public static final int HARD_TTL = 5;
}
