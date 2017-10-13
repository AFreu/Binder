package com.mobilecomputing.binder.Views;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilecomputing.binder.Objects.Book;
import com.mobilecomputing.binder.R;
import com.mobilecomputing.binder.Utils.User;
import com.squareup.picasso.Picasso;


/**
 * Created by Libra on 06/10/17.
 */

public class BookBottomSheet extends BottomSheetDialogFragment {

    private Book book;
    private User me;

    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView bookDescription;
    private ImageView bookImage;

    private TextView myBookReview;

    private boolean showMyReview = false;

    public BookBottomSheet() {

    }

    public void setUser(User user) { this.me = user; }
    public void setBook(Book book) {
        this.book = book;
    }
    public void showMyReview() { this.showMyReview = true; }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.book_bottom_sheet, container);
        bookTitle = view.findViewById(R.id.book_bottom_sheet_title);
        bookAuthor = view.findViewById(R.id.book_bottom_sheet_author);
        bookDescription = view.findViewById(R.id.book_bottom_sheet_description);
        bookImage = view.findViewById(R.id.book_bottom_sheet_image);

        myBookReview = view.findViewById(R.id.book_bottom_sheet_my_review);



        if(book != null) {
            bookTitle.setText(book.getTitle());
            bookAuthor.setText(book.getAuthor());
            bookDescription.setText(book.getDescription());
            bookTitle.setText(book.getTitle());
            Picasso.with(getContext()).load(book.getImageUrl()).into(bookImage);

            myBookReview.setText(book.getReviewTextByUser(me));
        }

        if(showMyReview) view.findViewById(R.id.book_bottom_sheet_my_review_layout).setVisibility(View.VISIBLE);



        return view;
    }
}
