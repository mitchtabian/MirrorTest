package com.codingwithmitch.mirrortest.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.codingwithmitch.mirrortest.Constants;
import com.codingwithmitch.mirrortest.ICallback;
import com.codingwithmitch.mirrortest.R;
import com.codingwithmitch.mirrortest.models.User;
import com.codingwithmitch.mirrortest.persistence.AppDatabase;
import com.codingwithmitch.mirrortest.persistence.UserDataEntity;
import com.codingwithmitch.mirrortest.requests.GetUserResponse;
import com.codingwithmitch.mirrortest.requests.LoginResponse;
import com.codingwithmitch.mirrortest.requests.MirrorApi;
import com.codingwithmitch.mirrortest.requests.PatchUserBody;
import com.codingwithmitch.mirrortest.requests.PatchUserResponse;
import com.codingwithmitch.mirrortest.requests.SignUpResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements
    IMainActivity
{

    private static final String TAG = "MainActivity";

    // widgets
    private ProgressBar mProgressBar;

    // vars
    private String mAuthToken = "";
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.progressBar);

        mDb = AppDatabase.getInMemoryDatabase(getApplicationContext());

        inflateLoginFragment();
    }

    public void inflateLoginFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, LoginFragment.newInstance(), Constants.FRAGMENT_LOGIN);
        transaction.commit();
    }

    public void inflateProfileFragment(User user){
        ProfileFragment fragment = ProfileFragment.newInstance();
        if(user != null){
            Bundle bundle = new Bundle();
            bundle.putParcelable(getString(R.string.user_object), user);
            fragment.setArguments(bundle);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, fragment, Constants.FRAGMENT_PROFILE);
        transaction.commit();
    }

    @Override
    public void inflateSignUpFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, SignUpFragment.newInstance(), Constants.FRAGMENT_SIGN_UP);
        transaction.addToBackStack(Constants.FRAGMENT_SIGN_UP);
        transaction.commit();
    }

    @Override
    public void inflateEditProfileFragment(User user){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.user_object), user);

        EditProfileFragment fragment = EditProfileFragment.newInstance();
        fragment.setArguments(bundle);

        transaction.replace(R.id.main_container, fragment, Constants.FRAGMENT_EDIT_PROFILE);
        transaction.addToBackStack(Constants.FRAGMENT_EDIT_PROFILE);
        transaction.commit();
    }

    @Override
    public void signUp(User user, String password) {
        Log.d(TAG, "signUp: attempting to make POST sign-up request to server.");
        showProgressBar();
        hideKeyboard();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MirrorApi mirrorApi = retrofit.create(MirrorApi.class);

        Call<SignUpResponse> response = mirrorApi.signUp(
                Constants.AUTH_SIGN_UP,
                user.getEmail(),
                password,
                password,
                user.getName(),
                Constants.API_TYPE_JSON
                );

        response.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {

                Log.d(TAG, "onResponse: Server Response: " + response.toString());
                if(response.code() == 200){
                    if(response.body().getMessage().equals(Constants.RESPONSE_SIGN_UP_SUCCESS)){
                        showSnackbar("Sign-up success", Snackbar.LENGTH_SHORT);
//                        inflateLoginFragment();
                        getSupportFragmentManager().popBackStack();
                    }
                }
                else {
                    showSnackbar("Something went wrong", Snackbar.LENGTH_SHORT);
                }

                hideProgressBar();
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: Server Response: " + t.getMessage());
                showSnackbar("Something went wrong", Snackbar.LENGTH_SHORT);
                hideProgressBar();
            }
        });
    }

    /**
     * Retrieves auth_token and inserts user info into datastore
     * @param email
     * @param password
     */
    @Override
    public void login(final String email, String password) {
        Log.d(TAG, "login: attempting to make POST login request to server.");
        showProgressBar();

        hideKeyboard();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MirrorApi mirrorApi = retrofit.create(MirrorApi.class);

        Call<LoginResponse> response = mirrorApi.login(
                Constants.AUTH_LOGIN,
                email,
                password,
                Constants.API_TYPE_JSON
        );

        response.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.d(TAG, "onResponse: Server Response: " + response.toString());
                if(response.code() == 200){
                    if(response.body().getMessage().equals(Constants.RESPONSE_LOGIN_SUCCESS)){
                        showSnackbar("Login success", Snackbar.LENGTH_SHORT);

                        setAuth_token(response.body().getData().getApi_token());
                        getUserInfoApiCall(new ICallback() {
                            @Override
                            public void getUserInfoCallback(User user) {
                                insertNewUser(user);
                                inflateProfileFragment(user);
                            }
                        });
                    }
                    else {
                        showSnackbar("Failed to authenticate", Snackbar.LENGTH_SHORT);
                    }
                }
                else {
                    showSnackbar("Something went wrong", Snackbar.LENGTH_SHORT);
                }

                hideProgressBar();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: Server Response: " + t.getMessage());
                showSnackbar("Something went wrong", Snackbar.LENGTH_SHORT);
                hideProgressBar();
            }
        });
    }

    @Override
    public void getUserInfoApiCall(final ICallback iCallback) {
        Log.d(TAG, "getUserInfoApiCall: attempting to make GET request to server.");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MirrorApi mirrorApi = retrofit.create(MirrorApi.class);

        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        headerMap.put("Authorization", "Bearer " + getAuth_token());

        Call<GetUserResponse> response = mirrorApi.getUserInfo(headerMap);

        response.enqueue(new Callback<GetUserResponse>() {
            @Override
            public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> response) {
                Log.d(TAG, "onResponse: Server Response: " + response.toString());
                if(response.code() == 200){
                    if(response.body().getMessage().equals(Constants.RESPONSE_GET_USER_INFO_SUCCESS)){
                        User user = new User(
                                response.body().getData().getEmail(),
                                response.body().getData().getName(),
                                response.body().getData().getProfile().getBirthdate(),
                                response.body().getData().getProfile().getLocation()
                        );
                        iCallback.getUserInfoCallback(user);
                    }
                    else {
                        showSnackbar("Failed to get user information", Snackbar.LENGTH_SHORT);
                        iCallback.getUserInfoCallback(null);
                    }
                }
                else {
                    showSnackbar("Something went wrong", Snackbar.LENGTH_SHORT);
                    iCallback.getUserInfoCallback(null);
                }

                hideProgressBar();
            }

            @Override
            public void onFailure(Call<GetUserResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: Server Response: " + t.getMessage());
                showSnackbar("Something went wrong", Snackbar.LENGTH_SHORT);
                hideProgressBar();
                iCallback.getUserInfoCallback(null);
            }
        });
    }



    @Override
    public void updateUserInfo(final User user) {
        Log.d(TAG, "updateUserInfo: attempting to make PATCH request to server.");
        showProgressBar();
        hideKeyboard();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MirrorApi mirrorApi = retrofit.create(MirrorApi.class);

        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        headerMap.put("Authorization", "Bearer " + getAuth_token());

        PatchUserBody patchUserBody = new PatchUserBody(
                user.getName(),
                user.getBirthdate(),
                user.getLocation()
        );
        Call<PatchUserResponse> response = mirrorApi.updateUserInfo(
                headerMap,
                Constants.PATCH_URL,
                patchUserBody
        );

        response.enqueue(new Callback<PatchUserResponse>() {
            @Override
            public void onResponse(Call<PatchUserResponse> call, Response<PatchUserResponse> response) {
                Log.d(TAG, "onResponse: Server Response: " + response.toString());
                if(response.code() == 200){
                    if(response.body().getMessage().equals(Constants.RESPONSE_PATCH_USER_INFO_SUCCESS)){

                        insertNewUser(user);
                        getSupportFragmentManager().popBackStack();
                    }
                    else {
                        showSnackbar("Failed to update information", Snackbar.LENGTH_SHORT);
                    }
                }
                else {
                    showSnackbar("Something went wrong", Snackbar.LENGTH_SHORT);
                }

                hideProgressBar();
            }

            @Override
            public void onFailure(Call<PatchUserResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: Server Response: " + t.getMessage());
                showSnackbar("Something went wrong", Snackbar.LENGTH_SHORT);
                hideProgressBar();
            }
        });
    }


    /**
     * Method used for updating user and inserting new user
     * Email field is unique in datastore so it won't insert duplicates
     * @param user
     */
    @Override
    public void insertNewUser(User user) {
        Log.d(TAG, "insertNewUser: updating user data in datastore.");
        UserDataEntity userDataEntity = new UserDataEntity();
        userDataEntity.email = user.getEmail();
        userDataEntity.name = user.getName();
        userDataEntity.birthdate = user.getBirthdate();
        userDataEntity.location = user.getLocation();

        Long tsLong = System.currentTimeMillis()/1000;
        userDataEntity.timestamp = tsLong;
        mDb.userDataDao().insertUsers(userDataEntity);
    }

    @Override
    public AppDatabase getAppDatabase() {
        return mDb;
    }

    @Override
    public void refreshDataStore() {
        Log.d(TAG, "refreshDataStore: refreshing data store.");
        getUserInfoApiCall(
                new ICallback() {
                    @Override
                    public void getUserInfoCallback(User user) {
                        insertNewUser(user);
                    }
                }
        );
    }

    private void showProgressBar(){
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){
        mProgressBar.setVisibility(View.GONE);
    }

    public void showSnackbar(String message, int length) {
        try{
            Snackbar.make(this.getCurrentFocus().getRootView(), message, length).show();
        }catch (NullPointerException e){

        }
    }

    public String getAuth_token() {
        return mAuthToken;
    }

    public void setAuth_token(String authToken) {
        this.mAuthToken = authToken;
    }

    private void hideKeyboard(){
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}












