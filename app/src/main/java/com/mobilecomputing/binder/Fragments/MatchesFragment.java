package com.mobilecomputing.binder.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilecomputing.binder.R;
import com.squareup.picasso.Picasso;



/**
 * A simple {@link Fragment} subclass.
 */
public class MatchesFragment extends BasicFragment {

    private TextView nameText;
    private TextView percentText;
    private ImageView profileImage;

    public MatchesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_matches, container, false);

        initUI(view);

        return view;
    }

    public void initUI(View view) {
        nameText = view.findViewById(R.id.profile_name);
        profileImage = view.findViewById(R.id.profile_picture);

        populateUI();
    }

    /**
     * Populates the user interface based on Google user data
     */
    public void populateUI() {

        //nameText.setText();
        //Picasso.with(getContext()).load();
    }

}
