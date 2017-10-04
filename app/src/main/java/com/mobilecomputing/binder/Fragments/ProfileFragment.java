package com.mobilecomputing.binder.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.mobilecomputing.binder.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BasicFragment {

    private GoogleSignInAccount userAccount;
    private TextView nameText;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initUI(view);

        return view;
    }

    public void initUI(View view) {
        nameText = view.findViewById(R.id.profile_name);

        populateUI();
    }

    /**
     * Populates the user interface based on Google user data
     */
    public void populateUI() {

        if(userAccount != null && nameText != null) {
            nameText.setText(userAccount.getDisplayName());
        }
    }

    public void setUserAccount(GoogleSignInAccount userAccount) {
        this.userAccount = userAccount;

        populateUI();
    }
}
