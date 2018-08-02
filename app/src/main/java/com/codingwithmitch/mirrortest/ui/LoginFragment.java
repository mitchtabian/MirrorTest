package com.codingwithmitch.mirrortest.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.codingwithmitch.mirrortest.R;
import com.codingwithmitch.mirrortest.models.User;
import com.codingwithmitch.mirrortest.util.Check;

public class LoginFragment extends Fragment implements
        View.OnClickListener
{

    private static final String TAG = "LoginFragment";

    public static LoginFragment newInstance(){
        return new LoginFragment();
    }

    //widgets
    private EditText mEmail, mPassword;

    //vars
    private IMainActivity mIMainActivity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mEmail = view.findViewById(R.id.login_email);
        mPassword = view.findViewById(R.id.login_password);

        view.findViewById(R.id.sign_up).setOnClickListener(this);
        view.findViewById(R.id.sign_in).setOnClickListener(this);

        return view;
    }

    private void login(){
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        // check if fields are filled out
        if(Check.isStringEmpty(email)
                && Check.isStringEmpty(password)){

                mIMainActivity.login(
                        email,
                        password
                );
        }
        else{
            showSnackbar("You must fill out all the fields.", Snackbar.LENGTH_SHORT);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.sign_up:{
                mIMainActivity.inflateSignUpFragment();
            }

            case R.id.sign_in:{
                login();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mIMainActivity = (IMainActivity) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "ClassCastException: " + e.getMessage());
        }
    }

    public void showSnackbar(String message, int length) {
        Snackbar.make(this.getView(), message, length).show();
    }
}





