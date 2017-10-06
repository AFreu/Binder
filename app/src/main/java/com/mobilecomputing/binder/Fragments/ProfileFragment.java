package com.mobilecomputing.binder.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.mobilecomputing.binder.R;
import com.mobilecomputing.binder.Views.ChipView;
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



        addDislikedGenre("Vad");
        addDislikedGenre("en knapp");


        addDislikedGenre("Vadsomhelst");
        addDislikedGenre("Fantasy");
        addDislikedGenre("Sci-fi");

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

    private void addDislikedGenre(String name){

        ChipView c = new ChipView(getContext());
        c.setText(name);
        c.setOnClickListener(clickRemoveListener);
        c.setLayoutParams(new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,FlowLayout.LayoutParams.WRAP_CONTENT));

        flowLayout.addView(c);

    }


    private void reLayoutChildren(View view) {
        view.measure(
                View.MeasureSpec.makeMeasureSpec(view.getMeasuredWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), View.MeasureSpec.EXACTLY));
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
    }




}
