package com.mobilecomputing.binder.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.mobilecomputing.binder.R;
import com.squareup.picasso.Picasso;
import com.wefika.flowlayout.FlowLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BasicFragment {

    private GoogleSignInAccount userAccount;
    private TextView nameText;
    private ImageView profileImage;
    private FlowLayout flowLayout;

    private View.OnClickListener clickRemoveListener;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        clickRemoveListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FlowLayout)view.getParent()).removeView(view);
            }
        };
        initUI(view);



        return view;
    }

    public void initUI(View view) {
        nameText = view.findViewById(R.id.profile_name);
        profileImage = view.findViewById(R.id.profile_picture);

        flowLayout = view.findViewById(R.id.profile_flowlayout);


        TextView t = new TextView(getContext());
        t.setText("Vadsomhelst!");
        t.setTextSize(20);
        t.setOnClickListener(clickRemoveListener);
        //t.getLayoutParams().width = FlowLayout.LayoutParams.WRAP_CONTENT;


        flowLayout.addView(t);

        TextView b = new TextView(getContext());
        b.setText("En knapp");
        b.setTextSize(20);
        b.setOnClickListener(clickRemoveListener);

        //b.getLayoutParams().width = FlowLayout.LayoutParams.WRAP_CONTENT;

        flowLayout.addView(b);

        TextView q = new TextView(getContext());
        q.setText("hej");
        q.setTextSize(20);
        q.setOnClickListener(clickRemoveListener);

        flowLayout.addView(q);

        flowLayout.postInvalidate();

        populateUI();
    }

    /**
     * Populates the user interface based on Google user data
     */
    public void populateUI() {

        if(userAccount != null && nameText != null) {
            nameText.setText(userAccount.getDisplayName());
            Picasso.with(getContext()).load(userAccount.getPhotoUrl()).into(profileImage);
        }

    }

    public void setUserAccount(GoogleSignInAccount userAccount) {
        this.userAccount = userAccount;

        populateUI();
    }




}
