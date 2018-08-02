package com.codingwithmitch.mirrortest.requests;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MirrorApi {

    // request for sign *UP*
    @POST("auth/{sign_up_url}")
    Call<SignUpResponse> signUp(
            @Path("sign_up_url") String signUpUrl,
            @Query("email") String email,
            @Query("password") String password,
            @Query("password2") String password2,
            @Query("name") String name,
            @Query("api_type") String apiType
    );

    // request for *login*
    @POST("auth/{login_url}")
    Call<LoginResponse> login(
            @Path("login_url") String loginUrl,
            @Query("email") String email,
            @Query("password") String password,
            @Query("api_type") String apiType
    );

    // request for getting user info
    @GET("user/me")
    Call<GetUserResponse> getUserInfo(
            @HeaderMap Map<String, String> headers
            );

    @PATCH("user/{patch_url}")
    Call<PatchUserResponse> updateUserInfo(
            @HeaderMap Map<String, String> headers,
            @Path("patch_url") String patch_url,
            @Body PatchUserBody body
    );

}












