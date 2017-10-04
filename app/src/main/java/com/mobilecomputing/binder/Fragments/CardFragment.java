package com.mobilecomputing.binder.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.mobilecomputing.binder.R;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class CardFragment extends BasicFragment {

    private GoogleSignInAccount userAccount;

    private ImageView profileImage;


    public CardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        initUI(view);

        return view;
    }

    public void initUI(View view) {
        profileImage = view.findViewById(R.id.card_profile_image);

        populateUI();
    }

    public void populateUI() {

        if(userAccount != null && profileImage != null) {

            Picasso.with(getContext()).load(userAccount.getPhotoUrl()).into(profileImage);
        }
    }

    public void setUserAccount(GoogleSignInAccount userAccount) {
        this.userAccount = userAccount;

        populateUI();
    }

}
