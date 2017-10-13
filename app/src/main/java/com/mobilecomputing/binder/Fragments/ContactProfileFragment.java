package com.mobilecomputing.binder.Fragments;


import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilecomputing.binder.Objects.Book;
import com.mobilecomputing.binder.Objects.Match;
import com.mobilecomputing.binder.R;
import com.mobilecomputing.binder.Utils.BookAdapter;
import com.mobilecomputing.binder.Utils.ImageAdapter;
import com.mobilecomputing.binder.Utils.User;
import com.mobilecomputing.binder.Views.ExpandableHeightGridView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactProfileFragment extends BasicFragment {

    String TAG = "ContactProfileFragment";

    Match mContact;

    ExpandableHeightGridView book_grid_1;
    ExpandableHeightGridView book_grid_2;

    BookAdapter bookAdapter1;
    BookAdapter bookAdapter2;

    TextView profileName;
    TextView profileMatchInfo;
    TextView gridSplit;

    ImageView profileImage;


    public ContactProfileFragment() {
        // Required empty public constructor
    }

    public static ContactProfileFragment newInstance(Match contact, User user) {
        ContactProfileFragment fragment = new ContactProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable("contact", contact);
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContact = (Match)getArguments().getSerializable("contact");
        userAccount = (User)getArguments().getSerializable("user");

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_profile, container, false);

        profileImage = view.findViewById(R.id.contact_profile_image);
        profileMatchInfo = view.findViewById(R.id.contact_profile_match_info);
        profileName = view.findViewById(R.id.contact_profile_name);
        gridSplit = view.findViewById(R.id.contact_profile_grid_split);

        book_grid_1 = view.findViewById(R.id.book_grid_1);
        book_grid_2 = view.findViewById(R.id.book_grid_2);


        populateUI();
        return view;
    }

    private void populateUI(){

        updateContactInfo();

        book_grid_1.setExpanded(true);
        book_grid_2.setExpanded(true);

        bookAdapter1 = new BookAdapter(getContext(), R.layout.book_item, mContact.getMatchingBooks());
        bookAdapter2 = new BookAdapter(getContext(), R.layout.book_item, mContact.getBooks());

        book_grid_1.setAdapter(bookAdapter1);
        book_grid_2.setAdapter(bookAdapter2);

        book_grid_1.setOnItemClickListener((parent, view, position, id) -> {
            showBook(bookAdapter1.getItem(position), mContact);
        });
        book_grid_2.setOnItemClickListener((parent, view, position, id) -> {
            showBook(bookAdapter2.getItem(position), mContact);
        });

    }

    private void updateContactInfo(){

        profileName.setText(mContact.getGivenName());
        profileMatchInfo.setText(mContact.percent + getString(R.string.contact_profile_percent_match));
        gridSplit.setText("Other books that " + mContact.getGivenName().split("\\s+")[0] + " loves");
        Picasso.with(getContext()).load(mContact.getImageUrl()).into(profileImage);
    }
}
