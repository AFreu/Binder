package com.mobilecomputing.binder.Fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilecomputing.binder.Objects.Book;
import com.mobilecomputing.binder.Objects.Match;
import com.mobilecomputing.binder.R;
import com.mobilecomputing.binder.Utils.ImageAdapter;
import com.mobilecomputing.binder.Views.ExpandableHeightGridView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactProfileFragment extends BasicFragment  {

    String TAG = "ContactProfileFragment";

    Match mContact;

    ArrayList<Book> mBooksToAdd;

    ExpandableHeightGridView book_grid_1;
    ExpandableHeightGridView book_grid_2;

    ImageAdapter imageAdapter1;
    ImageAdapter imageAdapter2;

    TextView profileName;
    TextView profileMatchInfo;
    TextView gridSplit;

    ImageView profileImage;


    public ContactProfileFragment() {
        // Required empty public constructor
    }

    public static ContactProfileFragment newInstance(Match contact) {
        ContactProfileFragment fragment = new ContactProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable("contact", contact);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContact = (Match)getArguments().getSerializable("contact");

        mBooksToAdd = new ArrayList<>();
        mBooksToAdd.addAll(mContact.getBooks());
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

        imageAdapter1 = new ImageAdapter(getContext(), R.layout.image_layout);
        imageAdapter2 = new ImageAdapter(getContext(), R.layout.image_layout);

        imageAdapter1.setLessInfo();
        imageAdapter2.setLessInfo();

        imageAdapter1.setBooks(mBooksToAdd.subList(1,3));
        imageAdapter2.setBooks(mBooksToAdd);

        imageAdapter1.setImageAdapterListener(this);
        imageAdapter2.setImageAdapterListener(this);

        book_grid_1.setAdapter(imageAdapter1);
        book_grid_2.setAdapter(imageAdapter2);

    }

    private void updateContactInfo(){

        profileName.setText(mContact.name);
        profileMatchInfo.setText(mContact.percent + getString(R.string.contact_profile_percent_match));
        gridSplit.setText("Other books that " + mContact.name.split("\\s+")[0] + " loves");
        Picasso.with(getContext()).load(mContact.pictureUrl).into(profileImage);
    }

}
