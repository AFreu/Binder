package com.mobilecomputing.binder.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mobilecomputing.binder.Objects.Match;
import com.mobilecomputing.binder.R;
import com.mobilecomputing.binder.Utils.MatchesAdapter;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MatchesFragment extends BasicFragment {

    private TextView nameText;
    private TextView percentText;
    private ImageView profileImage;
    ListView list;
    MatchesAdapter matches;

    public MatchesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_matches, container, false);
        list = view.findViewById(R.id.matches_list);
        ArrayList<Match> matchList = new ArrayList<Match>();
        matchList.add(new Match("Lovisa", 26, null, null, null));
        matches = new MatchesAdapter(getContext(), R.layout.match_item, matchList);
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
        list.setAdapter(matches);
        //nameText.setText();
        //Picasso.with(getContext()).load();
    }

}
