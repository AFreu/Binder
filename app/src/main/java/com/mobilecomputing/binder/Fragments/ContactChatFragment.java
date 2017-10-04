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
public class ContactChatFragment extends BasicFragment {


    public ContactChatFragment() {
        // Required empty public constructor
    }

    public static ContactChatFragment newInstance() {
        ContactChatFragment fragment = new ContactChatFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_chat, container, false);
    }

}
