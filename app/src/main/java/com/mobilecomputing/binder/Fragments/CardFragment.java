package com.mobilecomputing.binder.Fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.ArraySet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;
import com.mobilecomputing.binder.Activities.HomeActivity;
import com.mobilecomputing.binder.Objects.Book;
import com.mobilecomputing.binder.R;
import com.mobilecomputing.binder.Utils.ImageAdapter;
import com.mobilecomputing.binder.Utils.User;
import com.mobilecomputing.binder.Views.BookBottomSheet;
import com.squareup.picasso.Picasso;
import com.yuyakaido.android.cardstackview.CardStackView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class CardFragment extends BasicFragment
    implements ImageAdapter.ImageAdapterListener{

    private User userAccount;
    private List<Book> books;
    private Set<String> ignoreGenres = new HashSet<>();

    private ImageView profileImage;
    private TextView profileName;
    private CardStackView cardStack;

    private BookBottomSheet bookBottomSheet;

    private ImageAdapter imageAdapter;

    public CardFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        initUI(view);

        books = new ArrayList<>();

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initUI(View view) {
        profileImage = view.findViewById(R.id.card_profile_image);
        profileName = view.findViewById(R.id.card_text_name);
        cardStack = view.findViewById(R.id.card_stack);
        imageAdapter = new ImageAdapter(getActivity(), R.layout.image_layout);
        imageAdapter.setImageAdapterListener(this);
        cardStack.setAdapter(imageAdapter);

        fetchData(ignoreGenres);

        populateUI();
    }

    /**
     * Retrieves data from book api and ignores books of any genre found in ignoreGenres
     * @param genresToIgnore genres to ignore
     * @return list of books
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void fetchData(Set<String> genresToIgnore) {

        Log.d("CardFragment", "fetching..");

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String urlPrefix = "https://openlibrary.org/subjects/";
        String urlSufix = ".json#sort=edition_count&ebooks=true";

        genresToIgnore.forEach(g -> Log.d("CardFragment", "ignore genre: " + g));

        HomeActivity.allGenres.forEach(genre -> {
            if(!genresToIgnore.contains(genre)) {
                Log.d("CardFragment", "Request for: " + genre);

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        urlPrefix + genre + urlSufix,
                        response -> {

                            JSONObject json;

                            try {
                                json = new JSONObject(response);

                                JSONArray worksArray = json.getJSONArray("works");

                                if(worksArray != null) {

                                    // TODO filter what books to fetch in some way..
                                    for(int j = 0; j < worksArray.length(); j++)
                                        books.add(new Book(worksArray.get(j).toString()));

                                } else {
                                    Log.d("CardFragment", "no works found..");
                                }

                            } catch (JSONException e) { e.printStackTrace(); }

                        }, error -> {
                    Log.d("CardFragment", "That didn't work..");
                });

                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });

        queue.addRequestFinishedListener(listener -> imageAdapter.setBooks(books));
    }

    public void populateUI() {

        if(userAccount != null && profileImage != null) {
            profileName.setText(userAccount.getGivenName() + ".");
            Picasso.with(getContext()).load(userAccount.getImageUrl()).into(profileImage);
        }
    }

    public void setUserAccount(User userAccount) {
        this.userAccount = userAccount;

        populateUI();
    }

    public void addBookToTop(String strBook) {
        Book book = new Gson().fromJson(strBook, Book.class);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String urlPrefix = "https://openlibrary.org";
        String urlSufix = ".json";

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                urlPrefix + book.getKey() + urlSufix,
                response -> {

                    JSONObject json;

                    try {
                        json = new JSONObject(response);


                        if (json != null) {

                            // TODO add all books from a genre and not only the first one
                            books.add(0, new Book(json.toString()));
                            Log.d("CardFragment", "num of books: " + books.size());

                        } else {
                            Log.d("CardFragment", "no works found..");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
            Log.d("CardFragment", "That didn't work..");
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    @Override
    public void onLearnMoreClick(Book b) {
        bookBottomSheet = new BookBottomSheet();
        bookBottomSheet.setBook(b);
        bookBottomSheet.show(getActivity().getSupportFragmentManager(), bookBottomSheet.getTag());
    }

    public Set<String> getIgnoreGenres() {
        return ignoreGenres;
    }
}
