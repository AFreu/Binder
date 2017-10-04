package com.mobilecomputing.binder.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobilecomputing.binder.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactProfileFragment extends BasicFragment {


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
        return inflater.inflate(R.layout.fragment_contact_profile, container, false);
    }

}
