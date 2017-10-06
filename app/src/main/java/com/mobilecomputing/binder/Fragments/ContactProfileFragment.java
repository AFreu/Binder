package com.mobilecomputing.binder.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.mobilecomputing.binder.R;
import com.mobilecomputing.binder.Utils.ImageAdapter;
import com.mobilecomputing.binder.Views.ExpandableHeightGridView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactProfileFragment extends BasicFragment {

    String TAG = "ContactProfileFragment";


    ExpandableHeightGridView book_grid_1;
    ExpandableHeightGridView book_grid_2;

    ImageAdapter imageAdapter1;
    ImageAdapter imageAdapter2;


    public ContactProfileFragment() {
        // Required empty public constructor
    }

    public static ContactProfileFragment newInstance() {
        ContactProfileFragment fragment = new ContactProfileFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_profile, container, false);

        book_grid_1 = view.findViewById(R.id.book_grid_1);
        book_grid_2 = view.findViewById(R.id.book_grid_2);

        book_grid_2.setExpanded(true);


        imageAdapter1 = new ImageAdapter(getContext(), R.layout.image_layout);
        imageAdapter2 = new ImageAdapter(getContext(), R.layout.image_layout);

        book_grid_1.setAdapter(imageAdapter1);
        book_grid_2.setAdapter(imageAdapter2);

        Log.d(TAG,"Number of images 1: " + imageAdapter1.getCount() + " ");
        Log.d(TAG,"Number of images 2: " + imageAdapter2.getCount() + " ");


        return view;
    }

}
