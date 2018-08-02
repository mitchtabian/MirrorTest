package com.codingwithmitch.mirrortest.ui;

import com.codingwithmitch.mirrortest.ICallback;
import com.codingwithmitch.mirrortest.models.User;
import com.codingwithmitch.mirrortest.persistence.AppDatabase;
import com.codingwithmitch.mirrortest.requests.GetUserResponse;

public interface IMainActivity {

    void inflateSignUpFragment();

    void inflateEditProfileFragment(User user);

    void signUp(User user, String password);

    void login(String email, String password);

    void getUserInfoApiCall(ICallback iCallback);

    void updateUserInfo(User user);

    void insertNewUser(User user);

    AppDatabase getAppDatabase();

    void refreshDataStore();
}
