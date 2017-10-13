package com.mobilecomputing.binder.Utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mobilecomputing.binder.Activities.ContactActivity;
import com.mobilecomputing.binder.Fragments.ContactChatFragment;
import com.mobilecomputing.binder.Fragments.ContactProfileFragment;
import com.mobilecomputing.binder.Objects.Match;
import com.mobilecomputing.binder.R;

/**
 * Created by mpq on 2017-10-04.
 */

public class ContactPagerAdapter extends FragmentPagerAdapter
{
    private ContactActivity mContactActivity;
    private Match mContact;
    private User mUser;

    public ContactPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public ContactPagerAdapter(FragmentManager fm, ContactActivity contactActivity, Match contact, User user) {
        super(fm);
        mContactActivity = contactActivity;
        mContact = contact;
        mUser = user;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                return ContactProfileFragment.newInstance(mContact, mUser);
            case 1:
                return ContactChatFragment.newInstance(mContact);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
}

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContactActivity.getString(R.string.Match);
            case 1:
                return mContactActivity.getString(R.string.Chat);
            case 2:
                return null;
        }
        return null;
    }
}