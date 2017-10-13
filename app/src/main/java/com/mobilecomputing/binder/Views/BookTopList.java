package com.mobilecomputing.binder.Views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobilecomputing.binder.Objects.Book;
import com.mobilecomputing.binder.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Lovis on 13/10/17.
 */

public class BookTopList extends LinearLayout {

    private ImageView remove;
    private ImageView readMore;
    private ImageView bookToplist;
    private Context context;

    public BookTopList(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init(){
        inflate(getContext(), R.layout.added_toplist, this);
        remove = findViewById(R.id.remove_toplist_button);
        readMore = findViewById(R.id.view_toplist_button);
        bookToplist = findViewById(R.id.book_toplist_image);
    }


    public void setPicture(Book book){
        String url = book.getImageUrl();
        Picasso.with(context).load(url).into(bookToplist);
    };

}
