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
    ArrayList<Match> matchList;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_matches, container, false);

        list = view.findViewById(R.id.matches_list);
        list.setEnabled(false);


        matchList = new ArrayList<>();


        fetchData(5);

        Match match1 = new Match("Lovisa", 26, null, 0, "http://cdn-fashionisers.fcpv4ak.maxcdn-edge.com/wp-content/uploads/2014/03/top_80_updo_hairstyles_2014_for_women_Emma_Stone_updos2.jpg", 0);
        match1.percent = calculateMatchProcent(match1);
        matchList.add(match1);
        Match match2 = new Match("Mikael", 24, null, 1, "https://www.aceshowbiz.com/images/photo/ryan_gosling.jpg", 0);
        match2.percent = calculateMatchProcent(match2);
        matchList.add(match2);
        Match match3 = new Match("Anton", 73, null, 2, "http://akns-images.eonline.com/eol_images/Entire_Site/20161129/rs_300x300-161229151358-ap.jpg?downsize=300:*&crop=300:300;left,top", 0);
        match3.percent = calculateMatchProcent(match3);
        matchList.add(match3);
        Match match4 = new Match("Jimmy", 45, null, 3, "http://3.bp.blogspot.com/-a71LPYXKmYs/T5KQLsCOQNI/AAAAAAAAErw/vyC3o5j7JoA/s1600/Orlando+Bloom+(1).jpg", 100);
        match4.percent = calculateMatchProcent(match4);
        matchList.add(match4);

        Collections.sort(matchList, new Match());

        matches = new MatchesAdapter(getContext(), R.layout.match_item, matchList);
        initUI(view);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private int calculateMatchProcent(Match match) {
        List<List<String>> likedBooks = new ArrayList<List<String>>();
        String list1[] = {"/works/OL13101191W", "/works/OL13101191W","/works/OL20600W","/works/OL362703W","/works/OL262758W","/works/OL10279W","/works/OL676009W","/works/OL82592W","/works/OL71175W","/works/OL45891W","/works/OL71174W", "/works/OL71172W", "/works/OL15638539W", "/works/OL10279W", "/works/OL262758W", "/works/OL362703W", "/works/OL20600W"};
        String list2[] = {"/works/OL13101191W","/works/OL20600W","/works/OL362703W","/works/OL262758W","/works/OL10279W","/works/OL676009W","/works/OL82592W","/works/OL71175W","/works/OL45891W","/works/OL71174W", "/works/OL71172W", "/works/OL15638539W"};
        String list3[] = {"/works/OL13101191W","/works/OL262758W","/works/OL10279W","/works/OL676009W","/works/OL82592W","/works/OL71175W","/works/OL45891W","/works/OL71174W", "/works/OL71172W", "/works/OL15638539W"};
        String list4[] = {"/works/OL13101191W","/works/OL362703W","/works/OL262758W","/works/OL10279W","/works/OL676009W","/works/OL82592W","/works/OL71175W","/works/OL45891W","/works/OL71174W", "/works/OL71172W", "/works/OL15638539W"};

        likedBooks.add(Arrays.asList(list1));
        likedBooks.add(Arrays.asList(list2));
        likedBooks.add(Arrays.asList(list3));
        likedBooks.add(Arrays.asList(list4));
        List<String> myLikedBooks = myLikedBooks();
        List<String> matchBooks = getLikedBooks(match.id, likedBooks, myLikedBooks);

        return (int) (((float)matchBooks.size()/ (float)myLikedBooks.size()) * 100);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private List<String> getLikedBooks(int id, List<List<String>> likedBooks, List<String> myLikedBooks) {

        return myLikedBooks.stream().filter(likedBooks.get(id)::contains).collect(toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<String> myLikedBooks() {
        //String list1[] = {"/works/OL262758W","/works/OL10279W","/works/OL676009W","/works/OL82592W","/works/OL71175W","/works/OL45891W","/works/OL71174W", "/works/OL71172W", "/works/OL15638539W", "/works/OL10279W", "/works/OL262758W", "/works/OL362703W", "/works/OL20600W"};
        List<String> list2 = likedBooks.stream().map(book -> {return book.getKey();}).collect(toList());
        return list2;
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
