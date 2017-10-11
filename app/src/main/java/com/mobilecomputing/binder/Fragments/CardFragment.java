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
import com.mobilecomputing.binder.Objects.Match;
import com.mobilecomputing.binder.R;
import com.mobilecomputing.binder.Utils.ImageAdapter;
import com.mobilecomputing.binder.Utils.User;
import com.mobilecomputing.binder.Views.BookBottomSheet;
import com.mobilecomputing.binder.Views.NewMatch;
import com.squareup.picasso.Picasso;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.SwipeDirection;

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

    private NewMatch newMatch;

    public interface CardFragmentListener {
        void bookLiked(Book book);
        void bookDisiked(Book book);
    }

    private CardFragmentListener cardFragmentListener;

    public void setCardFragmentListener(CardFragmentListener cardFragmentListener) {
        this.cardFragmentListener = cardFragmentListener;
    }

    private User userAccount;
    private List<Book> books;
    private Set<Book> likedBooks = new HashSet<>();
    private Set<Book> dislikedBooks = new HashSet<>();;
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
        newMatch = new NewMatch();
        
        cardStack.setCardEventListener(new CardStackView.CardEventListener() {
            @Override
            public void onCardDragging(float percentX, float percentY) {

            }

            @Override
            public void onCardSwiped(SwipeDirection direction) {

                switch (direction) {
                    case Right:
                        likedBooks.add(books.get(cardStack.getTopIndex()-1));

                        if(cardFragmentListener != null)
                            cardFragmentListener.bookLiked(books.get(cardStack.getTopIndex()-1));

                        books.remove(cardStack.getTopIndex()-1);
                        break;
                    case Left:
                        dislikedBooks.add(books.get(cardStack.getTopIndex()-1));

                        if(cardFragmentListener != null)
                            cardFragmentListener.bookDisiked(books.get(cardStack.getTopIndex()-1));

                        books.remove(cardStack.getTopIndex()-1);
                        break;
                }
            }

            @Override
            public void onCardReversed() {

            }

            @Override
            public void onCardMovedToOrigin() {

            }

            @Override
            public void onCardClicked(int index) {

            }
        });

        fetchData(ignoreGenres);

        populateUI();
    }

    // Loads disliked genres from local storage
    public void refreshIgnoreGenres(Set<String> ignores) {
        ignoreGenres.addAll(ignores);
    }

    /**
     * Retrieves data from book api and ignores books of any genre found in ignoreGenres
     * @param genresToIgnore genres to ignore
     * @return list of books
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void fetchData(Set<String> genresToIgnore) {

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String urlPrefix = "https://openlibrary.org/subjects/";
        String urlSufix = ".json#sort=edition_count&ebooks=true";

        genresToIgnore.forEach(g -> Log.d("CardFragment", "ignore genre: " + g));

        HomeActivity.allGenres.forEach(genre -> {

            if(!genresToIgnore.contains(genre)) {

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
                                    for(int j = 0; j < worksArray.length(); j++) {
                                        Book b = new Book(worksArray.get(j).toString());
                                        b.setGenre(genre);

                                        boolean contains = likedBooks.contains(b)
                                                || dislikedBooks.contains(b);

                                        // if the user haven't seen the book before, add it to books
                                        if(!contains)
                                            books.add(b);

                                    }

                                }

                            } catch (JSONException e) { e.printStackTrace(); }

                        }, error -> Log.d("CardFragment", "That didn't work.."));

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

    public void setLikedBooks(Set<Book> likedBooks) {
        this.likedBooks = likedBooks;
    }

    public void setDislikedBooks(Set<Book> dislikedBooks) {
        this.dislikedBooks = dislikedBooks;
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
                            String imgUrl = "http://covers.openlibrary.org/b/id/" + book.getImageUrl() + "-L.jpg";
                            books.add(0, new Book(book.getTitle(), book.getAuthor(), "", imgUrl, book.getKey()));
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
        queue.addRequestFinishedListener(listener -> {
            imageAdapter.setBooks(books);
        });

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
