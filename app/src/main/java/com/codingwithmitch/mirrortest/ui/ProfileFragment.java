package com.codingwithmitch.mirrortest.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codingwithmitch.mirrortest.Constants;
import com.codingwithmitch.mirrortest.ICallback;
import com.codingwithmitch.mirrortest.R;
import com.codingwithmitch.mirrortest.models.User;
import com.codingwithmitch.mirrortest.persistence.AppDatabase;
import com.codingwithmitch.mirrortest.persistence.UserDataEntity;

public class ProfileFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener
{

    private static final String TAG = "ProfileFragment";

    public static ProfileFragment newInstance(){
        return new ProfileFragment();
    }

    //widgets
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mEmail, mName, mBirthdate, mLocation;

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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mEmail = view.findViewById(R.id.profile_email);
        mName = view.findViewById(R.id.profile_name);
        mBirthdate = view.findViewById(R.id.profile_birthdate);
        mLocation = view.findViewById(R.id.profile_location);

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        view.findViewById(R.id.tv_update_profile).setOnClickListener(this);

        return view;
    }

    private void updateUserData(){
        Log.d(TAG, "updating user information. " + mUser.toString());
        mEmail.setText(mUser.getEmail());
        mName.setText(mUser.getName());
        if(mUser.getBirthdate() == null || mUser.getLocation() == null){
            mIMainActivity.inflateEditProfileFragment(mUser);
            mIMainActivity.insertNewUser(mUser);
        }

        mBirthdate.setText(mUser.getBirthdate());
        mLocation.setText(mUser.getLocation());
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh: called.");
            if(mUser.getBirthdate() != null || mUser.getLocation() != null){

                // THREE cases
                // case1: if t0 <= softTTL
                //  -> show data from data store and do not make API call

                // case2: if T0 >= hardTTL
                //  -> make api call and refresh data store

                // case3: if hardTTL > T0 > softTTL
                //  -> display data from data store
                //  -> Refresh data store in background

                AppDatabase db = mIMainActivity.getAppDatabase();
                UserDataEntity userDataEntity = db.userDataDao().getUser(mUser.getEmail());

                Long currentTime = System.currentTimeMillis()/1000;
                Long TTL = 0l;
                try{
                    TTL = userDataEntity.timestamp;
                }catch (NullPointerException e){
                    Log.e(TAG, "onRefresh: NullPointerException: " + e.getMessage() );
                    mIMainActivity.insertNewUser(mUser);
                    mSwipeRefreshLayout.setRefreshing(false);
                    return;
                }

                Log.d(TAG, "onRefresh: \ncurrentTime: " + currentTime + "\n"
                    + "TTL: " + TTL + "\n"
                    + "(time - TTL): " + (currentTime - TTL) + " seconds have passed.\n");

                // Case 1
                if((currentTime - TTL) <= Constants.SOFT_TTL){
                    Log.d(TAG, "onRefresh: difference is less than soft TTL.");
                    mUser.setEmail(userDataEntity.email);
                    mUser.setName(userDataEntity.name);
                    mUser.setBirthdate(userDataEntity.birthdate);
                    mUser.setLocation(userDataEntity.location);
                    updateUserData();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                else if((currentTime - TTL) > Constants.HARD_TTL){
                    Log.d(TAG, "onRefresh: difference is greater than hard TTL.");
                    db.userDataDao().deleteUsers(userDataEntity);
                    mIMainActivity.getUserInfoApiCall(
                            new ICallback() {
                                @Override
                                public void getUserInfoCallback(User user) {
                                    mUser = user;
                                    updateUserData();
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    mIMainActivity.insertNewUser(user);
                                }
                            }
                    );
                }
                // Case 3
                else if((currentTime - TTL) > Constants.SOFT_TTL && (currentTime - TTL) <= Constants.HARD_TTL){
                    Log.d(TAG, "onRefresh: difference is between soft and hard TTL.");
                    mUser.setEmail(userDataEntity.email);
                    mUser.setName(userDataEntity.name);
                    mUser.setBirthdate(userDataEntity.birthdate);
                    mUser.setLocation(userDataEntity.location);
                    updateUserData();

                    mSwipeRefreshLayout.setRefreshing(false);
                    mIMainActivity.refreshDataStore();
                }
            }
            else{
                mSwipeRefreshLayout.setRefreshing(false);
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

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.tv_update_profile){
            mIMainActivity.inflateEditProfileFragment(mUser);
        }
    }


    /**
     * Checks the cache for user data.
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: called.");

        // check the cache for user data
        AppDatabase db = mIMainActivity.getAppDatabase();
        UserDataEntity userDataEntity = db.userDataDao().getUser(mUser.getEmail());
        mUser.setEmail(userDataEntity.email);
        mUser.setName(userDataEntity.name);
        mUser.setBirthdate(userDataEntity.birthdate);
        mUser.setLocation(userDataEntity.location);
        updateUserData();

    }
}















