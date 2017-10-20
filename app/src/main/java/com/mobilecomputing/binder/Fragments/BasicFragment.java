package com.mobilecomputing.binder.Fragments;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.mobilecomputing.binder.Activities.ContactActivity;
import com.mobilecomputing.binder.Objects.Book;
import com.mobilecomputing.binder.Objects.Match;
import com.mobilecomputing.binder.Utils.ImageAdapter;
import com.mobilecomputing.binder.Utils.Review;
import com.mobilecomputing.binder.Objects.User;
import com.mobilecomputing.binder.Views.BookBottomSheet;
import com.mobilecomputing.binder.Views.ReviewBottomSheet;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BasicFragment extends Fragment implements ImageAdapter.ImageAdapterListener, BookBottomSheet.BookBottomSheetListener {

    private String TAG = "BasicFragment";

    BookBottomSheet bookBottomSheet;
    User userAccount;

    public BasicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onLearnMoreClick(Book book) {
        bookBottomSheet = new BookBottomSheet();
        bookBottomSheet.setBook(book);
        bookBottomSheet.setUser(userAccount);
        bookBottomSheet.setBookBottomSheetListener(this);
        bookBottomSheet.show(getActivity().getSupportFragmentManager(), bookBottomSheet.getTag());
    }

    @Override
    public void reviewClicked(Review review) {

        /* Making a match out of the user */
        User reviewer = review.getReviewUser();
        List<Book> books = reviewer.getBooks();
        Match match = new Match(reviewer.getGivenName(), reviewer.getAge(), reviewer.getCity(), 7, reviewer.getImageUrl(), 65);
        match.setBooks(books, userAccount.getBooks());




        switchToMatch(match);
    }

    @Override
    public void myReviewClicked(Book book) {
        displayReviewBottomSheet(book);
    }


    protected void showBook(Book book){
        showBook(book, userAccount);
    }

    protected void showBook(Book b, User u){
        bookBottomSheet = new BookBottomSheet();
        bookBottomSheet.setBook(b);
        bookBottomSheet.setUser(u);
        bookBottomSheet.setBookBottomSheetListener(this);
        bookBottomSheet.show(getActivity().getSupportFragmentManager(), bookBottomSheet.getTag());
    }

    /**
     * Displays bottom sheet dialog when user swipes right on a book
     */
    public void displayReviewBottomSheet(Book book) {

        ReviewBottomSheet reviewBottomSheet = new ReviewBottomSheet();
        reviewBottomSheet.setBook(book);
        reviewBottomSheet.setUser(userAccount);
        reviewBottomSheet.show(getActivity().getSupportFragmentManager(), reviewBottomSheet.getTag());
    }

    /**
     * Starts a contact activity for a match
     * @param match - the match to populate contact activity with
     */
    protected void switchToMatch(Match match){
        Intent intent = new Intent(getActivity(), ContactActivity.class);
        intent.putExtra("contact", match);
        intent.putExtra("user", userAccount);
        System.out.println("List object clicked" + intent.getExtras());
        getActivity().startActivity(intent);
    }



    public void setUserAccount(User userAccount) {
        this.userAccount = userAccount;
    }

    public User getUserAccount() {
        return userAccount;
    }
}
