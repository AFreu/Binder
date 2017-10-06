package com.mobilecomputing.binder.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.mobilecomputing.binder.R;
import com.mobilecomputing.binder.Utils.ImageAdapter;
import com.squareup.picasso.Picasso;
import com.yuyakaido.android.cardstackview.CardStackView;


/**
 * A simple {@link Fragment} subclass.
 */
public class CardFragment extends BasicFragment {

    private GoogleSignInAccount userAccount;

    private ImageView profileImage;
    private TextView profileName;
    private CardStackView cardStack;

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
        profileName = view.findViewById(R.id.card_text_name);
        cardStack = view.findViewById(R.id.card_stack);
        ImageAdapter imageAdapter = new ImageAdapter(getActivity(), R.layout.image_layout);
        cardStack.setAdapter(imageAdapter);

        populateUI();
    }

    public void populateUI() {

        if(userAccount != null && profileImage != null) {
            profileName.setText(userAccount.getGivenName() + ".");
            Picasso.with(getContext()).load(userAccount.getPhotoUrl()).into(profileImage);
        }
    }

    public void setUserAccount(GoogleSignInAccount userAccount) {
        this.userAccount = userAccount;

        populateUI();
    }

}
