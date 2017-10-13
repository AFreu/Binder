package com.mobilecomputing.binder.Views;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilecomputing.binder.Objects.Book;
import com.mobilecomputing.binder.R;
import com.squareup.picasso.Picasso;


/**
 * Created by Libra on 06/10/17.
 */

public class ReviewBottomSheet extends BottomSheetDialogFragment {

    private Book book;

    private TextView bookTitle;
    private TextView bookAuthor;
    private TextInputEditText reviewInput;
    private Button doneButton;
    private Button skipButton;

    public ReviewBottomSheet() {

    }

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
        reviewInput = view.findViewById(R.id.review_text_input);

        doneButton = view.findViewById(R.id.review_done_button);
        skipButton = view.findViewById(R.id.review_skip_button);

        doneButton.setOnClickListener(doneListener -> {

            String review = reviewInput.getText().toString();
            // todo put review on Book
            dismiss();
        });

        skipButton.setOnClickListener(skipListener -> {
            dismiss();
        });

        if(book != null) {
            bookTitle.setText(book.getTitle());
            bookAuthor.setText(book.getAuthor());
        }

        return view;
    }
}
