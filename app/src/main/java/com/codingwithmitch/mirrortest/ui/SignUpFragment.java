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
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.codingwithmitch.mirrortest.Constants;
import com.codingwithmitch.mirrortest.ICallback;
import com.codingwithmitch.mirrortest.R;
import com.codingwithmitch.mirrortest.models.User;
import com.codingwithmitch.mirrortest.requests.SignUpResponse;
import com.codingwithmitch.mirrortest.util.Check;

public class SignUpFragment extends Fragment implements
        View.OnClickListener
{

    private static final String TAG = "SignUpFragment";

    public static SignUpFragment newInstance(){
        return new SignUpFragment();
    }

    //widgets
    private EditText mEmail, mName, mPassword, mPassword2;

    //vars
    private IMainActivity mIMainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        mEmail = view.findViewById(R.id.sign_up_email);
        mName = view.findViewById(R.id.sign_up_name);
        mPassword = view.findViewById(R.id.sign_up_password);
        mPassword2 = view.findViewById(R.id.sign_up_password2);

        view.findViewById(R.id.register).setOnClickListener(this);

        return view;
    }


    private void signUp(){
        String email = mEmail.getText().toString();
        String name = mName.getText().toString();
        String password = mPassword.getText().toString();
        String password2 = mPassword2.getText().toString();

        // check if fields are filled out
        if(Check.isStringEmpty(email)
                && Check.isStringEmpty(name)
                && Check.isStringEmpty(password)
                && Check.isStringEmpty(password2)){

            // check if passwords match
            if(Check.isStringsMatch(password, password2)){
                User user = new User(email, name, "", "");
                mIMainActivity.signUp(
                        user,
                        password
                );
            }
            else{
                showSnackbar("Passwords must match.", Snackbar.LENGTH_SHORT);
            }
        }
        else{
            showSnackbar("You must fill out all the fields.", Snackbar.LENGTH_SHORT);
        }
    }



    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.register:{
                signUp();
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

















