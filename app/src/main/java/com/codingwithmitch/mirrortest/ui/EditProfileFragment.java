package com.codingwithmitch.mirrortest.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class EditProfileFragment extends Fragment implements
    View.OnClickListener
{

    private static final String TAG = "EditProfileFragment";

    public static EditProfileFragment newInstance(){
        return new EditProfileFragment();
    }

    //widgets
    private EditText mName, mBirthdate, mLocation;
    private TextView mEmail;

    //vars
    private IMainActivity mIMainActivity;
    private User mUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            mUser = getArguments().getParcelable(getString(R.string.user_object));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        mEmail = view.findViewById(R.id.update_email);
        mName = view.findViewById(R.id.update_name);
        mBirthdate = view.findViewById(R.id.update_birthdate);
        mLocation = view.findViewById(R.id.update_location);


        view.findViewById(R.id.btn_update_profile).setOnClickListener(this);

        updateUserData();

        return view;
    }

    private void updateUserData(){
        Log.d(TAG, "updating user information. " + mUser.toString());
        mEmail.setText(mUser.getEmail());
        mName.setText(mUser.getName());
        if(mUser.getBirthdate() == null || mUser.getLocation() == null){
            buildDialog("Error","Please fill out the birthdate and location fields").show();
        }
        mBirthdate.setText(mUser.getBirthdate());
        mLocation.setText(mUser.getLocation());
    }

    private void updateProfileInfo(){
        String name = mName.getText().toString();
        String birthdate = mBirthdate.getText().toString();
        String location = mLocation.getText().toString();

        // check if fields are filled out
        if(Check.isStringEmpty(name)
                && Check.isStringEmpty(birthdate)
                && Check.isStringEmpty(location)){

            // Make sure date is formatted correctly
            if(Check.isValidFormat("yyyy-mm-dd", birthdate)){
                String month = birthdate.substring(5, 7);
                String day = birthdate.substring(8, 10);
                if(Check.isValidMonth(month)){
                    if(Check.isValidDay(day)){
                        User user = new User(mUser.getEmail(), name, birthdate, location);
                        mIMainActivity.updateUserInfo(user);
                    }
                    else{
                        showSnackbar("Not a valid day", Snackbar.LENGTH_SHORT);
                    }
                }
                else{
                    showSnackbar("Not a valid month", Snackbar.LENGTH_SHORT);
                }
            }
            else{
                buildDialog("Formatting Error","Birthdate format must be: yyyy-mm-dd").show();
            }
        }
        else{
            showSnackbar("You must fill out all the fields.", Snackbar.LENGTH_SHORT);
        }
    }

    private Dialog buildDialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if(!title.equals("")){
            builder.setTitle(title);
        }
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_update_profile){
            updateProfileInfo();
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



















