package com.mobilecomputing.binder.Fragments;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mobilecomputing.binder.Activities.HomeActivity;
import com.mobilecomputing.binder.Objects.Book;
import com.mobilecomputing.binder.Objects.Match;
import com.mobilecomputing.binder.R;
import com.mobilecomputing.binder.Utils.MatchesAdapter;

import static java.util.stream.Collectors.toList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    private List<Match> matchList = new ArrayList<>();

    ArrayList<Book> booksToAdd = new ArrayList<>();
    private List<Book> likedBooks  = new ArrayList<>();
    private ArrayList<Book> featuredBooks = new ArrayList<>();

    public MatchesFragment() {
        // Required empty public constructor
    }
    // called from HomeActivity when user swipes on a book
    public void setLikedBooks(List<Book> likedBooks) {
        this.likedBooks = likedBooks;
    }
    public void setMatches(List<Match> matches) {
       this.matchList = matches;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_matches, container, false);

        list = view.findViewById(R.id.matches_list);
        list.setEnabled(false);

        fetchData(5);

        Collections.sort(matchList, new Match());

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

        list.setOnItemClickListener((parent, view, position, id) -> {

            switchToMatch(matchList.get(position));


        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void fetchData(int max) {

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String urlPrefix = "https://openlibrary.org/subjects/";
        String urlSufix = ".json#sort=edition_count&ebooks=true";


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    urlPrefix + "romance" + urlSufix,
                    response -> {

                        JSONObject json;

                        try {
                            json = new JSONObject(response);

                            JSONArray worksArray = json.getJSONArray("works");

                            if(worksArray != null) {

                                // TODO filter what books to fetch in some way..
                                for(int j = 0; j < worksArray.length(); j++){
                                    if(j >= max) break;
                                    Book book = new Book(worksArray.get(j).toString());
                                    booksToAdd.add(book);
                                    if((j%2) == 0){
                                        featuredBooks.add(book);
                                    }

                                }


                            } else {
                                Log.d("CardFragment", "no works found..");
                            }

                        } catch (JSONException e) { e.printStackTrace(); }

                    }, error -> {
                Log.d("CardFragment", "That didn't work..");
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);


        queue.addRequestFinishedListener(listener -> {

            for (Match match: matchList){
                match.setBooks(booksToAdd, likedBooks);
                match.setFeaturedBooks(booksToAdd);
                /* Adding one review per book */
                //booksToAdd.stream().forEach(b -> {b.setReviewTextForUser("This book has an awesome story! I like how it is impossible to guess what happens next!", match );});
            }

            list.setEnabled(true);
            matches.notifyDataSetChanged();


        });




    }



}
