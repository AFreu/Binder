package com.mobilecomputing.binder.Fragments;


import android.support.v4.app.Fragment;

import com.mobilecomputing.binder.Objects.Book;
import com.mobilecomputing.binder.Utils.ImageAdapter;
import com.mobilecomputing.binder.Utils.User;
import com.mobilecomputing.binder.Views.BookBottomSheet;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BasicFragment extends Fragment implements ImageAdapter.ImageAdapterListener {

    BookBottomSheet bookBottomSheet;
    User userAccount;

    public BasicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onLearnMoreClick(Book book) {
        bookBottomSheet = new BookBottomSheet();
        bookBottomSheet.setBook(book);
        bookBottomSheet.show(getActivity().getSupportFragmentManager(), bookBottomSheet.getTag());
    }

    protected void showBook(Book book, User user){
        bookBottomSheet = new BookBottomSheet();
        bookBottomSheet.setBook(book);
        bookBottomSheet.setUser(user);
        bookBottomSheet.showMyReview();
        bookBottomSheet.show(getActivity().getSupportFragmentManager(), bookBottomSheet.getTag());
    }

    public void setUserAccount(User userAccount) {
        this.userAccount = userAccount;
    }


}
