package com.mobilecomputing.binder.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilecomputing.binder.Activities.ContactActivity;
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


    public static final String MATCH_ID = "match ID";

    private TextView nameText;
    private TextView percentText;
    private ImageView profileImage;
    ListView list;
    MatchesAdapter matches;
    ArrayList<Match> matchList;

    public MatchesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_matches, container, false);

        list = view.findViewById(R.id.matches_list);

        matchList = new ArrayList<>();

        matchList.add(new Match("Lovisa", 26, null, null, "http://cdn-fashionisers.fcpv4ak.maxcdn-edge.com/wp-content/uploads/2014/03/top_80_updo_hairstyles_2014_for_women_Emma_Stone_updos2.jpg", 55));
        matchList.add(new Match("Mikael", 24, null, null, "https://www.aceshowbiz.com/images/photo/ryan_gosling.jpg", 67));

        matches = new MatchesAdapter(getContext(), R.layout.match_item, matchList);
        initUI(view);
        return view;
    }

    public void initUI(View view) {
        nameText = view.findViewById(R.id.profile_name);
        profileImage = view.findViewById(R.id.profile_picture);
        list.setAdapter(matches);
        populateUI();
    }

    /**
     * Populates the user interface based on Google user data
     */
    public void populateUI() {

        list.setClickable(true);
        list.setOnItemClickListener((parent, view, position, id) -> {

            Intent intent = new Intent(getActivity(), ContactActivity.class);
            intent.putExtra("contact", matchList.get(position));
            System.out.println("List object clicked" + intent.getExtras());
            getActivity().startActivity(intent);

        });
        //nameText.setText();
        //Picasso.with(getContext()).load();
    }

}
