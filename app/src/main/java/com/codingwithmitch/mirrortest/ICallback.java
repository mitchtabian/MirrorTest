package com.codingwithmitch.mirrortest;

import com.codingwithmitch.mirrortest.models.User;
import com.codingwithmitch.mirrortest.requests.SignUpResponse;

public interface ICallback {

    void getUserInfoCallback(User user);
}
