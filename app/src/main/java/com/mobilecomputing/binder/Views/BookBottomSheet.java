package com.mobilecomputing.binder.Views;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobilecomputing.binder.Objects.Book;
import com.mobilecomputing.binder.R;
import com.mobilecomputing.binder.Utils.Review;
import com.mobilecomputing.binder.Utils.ReviewAdapter;
import com.mobilecomputing.binder.Objects.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Libra on 06/10/17.
 */

public class BookBottomSheet extends BottomSheetDialogFragment {

    private String TAG = "BookBottomSheet";

    public interface BookBottomSheetListener {
        void reviewClicked(Review review);
        void myReviewClicked(Book book);

    }
    private BookBottomSheet.BookBottomSheetListener bookBottomSheetListener;
    public void setBookBottomSheetListener(BookBottomSheetListener bookBottomSheetListener) {
        this.bookBottomSheetListener = bookBottomSheetListener;
    }

    private Book book;
    private User me;

    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView bookDescription;
    private ImageView bookImage;

    private TextView myBookReview;
    private ImageView myBookReviewImage;
    private LinearLayout myBookReviewLayout;

    private RecyclerView recyclerView;
    private ReviewAdapter adapter;

    public BookBottomSheet() {

    }

    public void setUser(User user) { this.me = user; }
    public void setBook(Book book) {
        this.book = book;
    }

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
        myBookReview = view.findViewById(R.id.review_item_text);
        myBookReviewImage = view.findViewById(R.id.review_item_picture);
        recyclerView = view.findViewById(R.id.book_bottom_sheet_list_review);
        myBookReviewLayout = view.findViewById(R.id.book_bottom_sheet_my_review_layout);

        populateUI();


        myBookReview.setOnClickListener(v -> {
            bookBottomSheetListener.myReviewClicked(book);
        });

        adapter.setOnClickListener(v -> {
            int itemPosition = recyclerView.indexOfChild(v);
            bookBottomSheetListener.reviewClicked(adapter.getReview(itemPosition));
        });


        return view;
    }

    private void populateUI(){

        List<Review> reviewsByOthers = new ArrayList<>();
        Review myReview = null;

        for(Review r : book.getReviews()){

            User other = r.getReviewUser();
            if(other != null){
                if(other.equals(me))
                {
                    myReview = r;
                }
                else
                {
                    reviewsByOthers.add(r);
                }
            }
        }


        /* Show my review if it exists */
        if(myReview != null)
        {
            myBookReviewLayout.setVisibility(View.VISIBLE);
            myBookReview.setText(myReview.getReviewText());
            Picasso.with(getContext()).load(myReview.getReviewUser().getImageUrl()).into(myBookReviewImage);
        }else
        {
            myBookReviewLayout.setVisibility(View.GONE);
        }


        adapter = new ReviewAdapter(book.getReviewsByOthers(me));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();


        if(book != null) {
            bookTitle.setText(book.getTitle());
            bookAuthor.setText(book.getAuthor());
            bookDescription.setText(book.getDescription());
            bookTitle.setText(book.getTitle());
            Picasso.with(getContext()).load(book.getImageUrl()).into(bookImage);
        }



    }
}
