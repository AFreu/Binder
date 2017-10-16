package com.mobilecomputing.binder.Utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobilecomputing.binder.Objects.Book;
import com.mobilecomputing.binder.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by mikael on 2017-10-04.
 */

public class ImageAdapter extends ArrayAdapter {


    public interface ImageAdapterListener {
        void onLearnMoreClick(Book book);
    }

    private ImageAdapterListener imageAdapterListener;

    private List<Book> books = new ArrayList<>();
    private int layoutResource;
    private Context context;
    private boolean hideActionArea = false;

    public ImageAdapter(@NonNull Context context, int layoutResource) {
        super(context, layoutResource);

        this.context = context;
        this.layoutResource = layoutResource;
    }

    public void setImageAdapterListener(ImageAdapterListener imageAdapterListener) {
        this.imageAdapterListener = imageAdapterListener;
    }

    /**
     * Mocks data for this image adapter. Used for background grid..
     */
    public void mockData() {

        Random r = new Random();
        for(int i = 1; i < 13; i++) {
            int rand = r.nextInt(1000 - 1) + 1;
            books.add(new Book("", "", "", "http://covers.openlibrary.org/b/ID/" + rand + "-L.jpg"));
        }

    }

    /**
     * Hides the action area of all the images. Used for background grid...
     */
    public void hideActionArea(){
        hideActionArea = true;
    }

    /**
     * Updates data for this adapter and notifies about the changes.
     * @param books data to use.
     */
    public void setBooks(List<Book> books) {
        this.books = books;

        notifyDataSetChanged();
    }

    /**
     * Adds imageUrl to list of image data for this adapter and notifies about the changes.
     * @param book data to add.
     */
    public void addBook(Book book) {
        books.add(book);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(v == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            v = inflater.inflate(layoutResource, parent, false);
        }

        String url = books.get(position).getImageUrl();
        Picasso.with(context).load(url).into((ImageView)v.findViewById(R.id.book_card_image));

        String title = books.get(position).getTitle();
        String author = books.get(position).getAuthor();
        ((TextView)v.findViewById(R.id.book_card_title)).setText(title);
        ((TextView)v.findViewById(R.id.book_card_author)).setText(author);


        LinearLayout actionArea = (LinearLayout) v.findViewById(R.id.book_card_action_area);

        if(hideActionArea) actionArea.setVisibility(View.INVISIBLE);

        v.findViewById(R.id.book_card_button).setOnClickListener(listener -> {

            if(imageAdapterListener != null)
                imageAdapterListener.onLearnMoreClick(books.get(position));

        });

        return v;
    }

    @Override
    public int getCount() {
        return books.size();
    }
}
