package com.mobilecomputing.binder.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.mobilecomputing.binder.R;
import com.mobilecomputing.binder.Utils.User;
import com.mobilecomputing.binder.Views.BottomSheet;
import com.mobilecomputing.binder.Views.ChipButton;
import com.mobilecomputing.binder.Views.ChipView;
import com.squareup.picasso.Picasso;
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BasicFragment {

    private String TAG = "ProfileFragment";

    private User userAccount;
    private TextView nameText;
    private ImageView profileImage;
    private FlowLayout flowLayout;
    private BottomSheet bottomSheet = new BottomSheet();

    private List<String> allGenres;
    private ChipButton chipButton;

    private View.OnClickListener clickRemoveListener;
    private View.OnClickListener clickAddListener;
    private AdapterView.OnItemClickListener clickGenreListener;

    public ProfileFragment() {
        // Required empty public constructor
        allGenres = new ArrayList<>();
        allGenres.add("science");
        allGenres.add("biography");
        allGenres.add("drama");
        allGenres.add("sci-fi");
        allGenres.add("romance");
        allGenres.add("fantasy");
        allGenres.add("action");
        allGenres.add("horror");

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        clickRemoveListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ChipView c = (ChipView) view;

                ((FlowLayout)c.getParent()).removeView(c);

                allGenres.add(c.getText());

                chipButton.setVisibility(View.VISIBLE);

            }
        };

        clickAddListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.addToBackStack(null);

                bottomSheet.setGenres(allGenres);
                bottomSheet.setOnItemClickListener(clickGenreListener);
                bottomSheet.show(ft, "dialog");
            }
        };

        clickGenreListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "clicking genre");

                addDislikedGenre((String)adapterView.getAdapter().getItem(i));
                allGenres.remove(adapterView.getAdapter().getItem(i));
                bottomSheet.dismiss();

                if(allGenres.isEmpty()){
                    chipButton.setVisibility(View.INVISIBLE);
                }

            }
        };
        initUI(view);






        return view;
    }

    public void initUI(View view) {
        nameText = view.findViewById(R.id.profile_name);
        profileImage = view.findViewById(R.id.profile_picture);

        flowLayout = view.findViewById(R.id.profile_flowlayout);


        chipButton = new ChipButton(getContext());
        chipButton.setOnClickListener(clickAddListener);
        chipButton.setLayoutParams(new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,FlowLayout.LayoutParams.WRAP_CONTENT));
        flowLayout.addView(chipButton);


        populateUI();
    }

    /**
     * Populates the user interface based on Google user data
     */
    public void populateUI() {

        Log.d("HomeActivity", "populating..");

        if(userAccount != null && nameText != null) {
            nameText.setText(userAccount.getGivenName());
            Picasso.with(getContext()).load(userAccount.getImageUrl()).into(profileImage);
        }
    }

    public void setUserAccount(User userAccount) {
        this.userAccount = userAccount;

        populateUI();
    }

    private void addDislikedGenre(String name){

        ChipView c = new ChipView(getContext());
        c.setText(name);
        c.setOnClickListener(clickRemoveListener);
        c.setLayoutParams(new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,FlowLayout.LayoutParams.WRAP_CONTENT));

        flowLayout.addView(c, flowLayout.getChildCount() - 1);


    }


    private void reLayoutChildren(View view) {
        view.measure(
                View.MeasureSpec.makeMeasureSpec(view.getMeasuredWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), View.MeasureSpec.EXACTLY));
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
    }




}
