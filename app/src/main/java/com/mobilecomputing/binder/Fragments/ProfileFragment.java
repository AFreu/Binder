package com.mobilecomputing.binder.Fragments;


import android.content.SharedPreferences;
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
import com.mobilecomputing.binder.Activities.HomeActivity;
import com.mobilecomputing.binder.R;
import com.mobilecomputing.binder.Utils.User;
import com.mobilecomputing.binder.Views.BottomSheet;
import com.mobilecomputing.binder.Views.ChipButton;
import com.mobilecomputing.binder.Views.ChipView;
import com.squareup.picasso.Picasso;
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BasicFragment {

    public interface ProfileFragmentListener {
        void onDislikedGenreAdded(String genre);
        void onDislikedGenreRemoved(String genre);
    }

    private ProfileFragmentListener profileFragmentListener;

    public void setProfileFragmentListener(ProfileFragmentListener profileFragmentListener) {
        this.profileFragmentListener = profileFragmentListener;
    }

    private SharedPreferences sharedPreferences;

    private String TAG = "ProfileFragment";

    private User userAccount;
    private TextView nameText;
    private ImageView profileImage;
    private FlowLayout flowLayout;
    private BottomSheet bottomSheet = new BottomSheet();

    private List<String> availableGenres = new ArrayList<>();
    private Set<String> ignoredGenres = new HashSet<>();
    private ChipButton chipButton;

    private View.OnClickListener clickRemoveListener;
    private View.OnClickListener clickAddListener;
    private AdapterView.OnItemClickListener clickGenreListener;

    public ProfileFragment() {
        // Required empty public constructor
        availableGenres.addAll(HomeActivity.allGenres);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // removes an ignored genre
        clickRemoveListener = view13 -> {

            ChipView c = (ChipView) view13;

            ((FlowLayout)c.getParent()).removeView(c);

            chipButton.setVisibility(View.VISIBLE);

            ignoredGenres.remove(c.getText());
            availableGenres.add(c.getText());

            // notifies listener about the genre to ignore
            if(profileFragmentListener != null)
                profileFragmentListener.onDislikedGenreRemoved(c.getText());

        };

        clickAddListener = view12 -> {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.addToBackStack(null);

            bottomSheet.setGenres(availableGenres);
            bottomSheet.setOnItemClickListener(clickGenreListener);
            bottomSheet.show(ft, "dialog");
        };

        // when a genre to ignore is clicked
        clickGenreListener = (adapterView, view1, i, l) -> {

            addDislikedGenre((String)adapterView.getAdapter().getItem(i));
            ignoredGenres.add((String)adapterView.getAdapter().getItem(i));

            // notifies listener about the genre to ignore
            if(profileFragmentListener != null)
                profileFragmentListener.onDislikedGenreAdded((String)adapterView.getAdapter().getItem(i));

            availableGenres.remove((String)adapterView.getAdapter().getItem(i));

            bottomSheet.dismiss();

            if(availableGenres.isEmpty())
                chipButton.setVisibility(View.INVISIBLE);
        };

        initUI(view);

        return view;
    }

    // Loads disliked genres from local storage
    public void refreshIgnoreGenres(Set<String> ignores) {
        ignoredGenres.addAll(ignores);
        availableGenres.removeAll(ignores);
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

        // adds all disliked genres to UI
        for(String genre : ignoredGenres)
            addDislikedGenre(genre);
    }

    public void setUserAccount(User userAccount) {
        this.userAccount = userAccount;
//        populateUI();
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
