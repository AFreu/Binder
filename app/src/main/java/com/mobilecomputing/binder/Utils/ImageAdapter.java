package com.mobilecomputing.binder.Utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

        /*ArrayList<String> urls = new ArrayList<>();
        Random r = new Random();
        for(int i = 1; i < 13; i++) {
            int rand = r.nextInt(1000 - 1) + 1;
            urls.add("http://covers.openlibrary.org/b/ID/" + rand + "-L.jpg");
        }

        this.books = urls;*/
    }

    public void setImageAdapterListener(ImageAdapterListener imageAdapterListener) {
        this.imageAdapterListener = imageAdapterListener;
    }

    /**
     * Sets the mode of the adapter for images to be that of a background grid of images
     */
    public void setBackgroundGridMode() {
        hideActionArea = true;
        mockData();
    }

    private void mockData() {

        Random r = new Random();
        for(int i = 1; i < 13; i++) {
            int rand = r.nextInt(1000 - 1) + 1;
            books.add(new Book("", "", "", "http://covers.openlibrary.org/b/ID/" + rand + "-L.jpg"));
        }

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

        //holder.image = r.findViewById(R.id.image_layout_image);
        //r.setTag(holder);

        String url = books.get(position).getImageUrl();
        Picasso.with(context).load(url).into((ImageView)v.findViewById(R.id.book_card_image));

        if(hideActionArea) {
            v.findViewById(R.id.book_card_action_area).setVisibility(View.INVISIBLE);
        } else {
            ((TextView)v.findViewById(R.id.book_card_author)).setText(books.get(position).getAuthor());
            ((TextView)v.findViewById(R.id.book_card_title)).setText(books.get(position).getTitle());
            v.findViewById(R.id.book_card_button).setOnClickListener(listener -> {

                if(imageAdapterListener != null)
                    imageAdapterListener.onLearnMoreClick(books.get(position));

            });
        }

        return v;
    }

    @Override
    public int getCount() {
        return books.size();
    }
}
