package com.mobilecomputing.binder.Fragments;


import android.support.v4.app.Fragment;

import com.mobilecomputing.binder.Objects.Book;
import com.mobilecomputing.binder.Utils.ImageAdapter;
import com.mobilecomputing.binder.Views.BookBottomSheet;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BasicFragment extends Fragment implements ImageAdapter.ImageAdapterListener {

    BookBottomSheet bookBottomSheet;

    public BasicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onLearnMoreClick(Book book) {
        bookBottomSheet = new BookBottomSheet();
        bookBottomSheet.setBook(book);
        bookBottomSheet.show(getActivity().getSupportFragmentManager(), bookBottomSheet.getTag());
    }

    protected void showBook(Book book){
        bookBottomSheet = new BookBottomSheet();
        bookBottomSheet.setBook(book);
        bookBottomSheet.showMyReview();
        bookBottomSheet.show(getActivity().getSupportFragmentManager(), bookBottomSheet.getTag());
    }


}
