package com.mobilecomputing.binder.Utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

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

    private List<Book> books;
    private int layoutResource;
    private Context context;

    public ImageAdapter(@NonNull Context context, int layoutResource) {
        super(context, layoutResource);

        this.context = context;
        this.layoutResource = layoutResource;
        this.books = new ArrayList<>();

        /*ArrayList<String> urls = new ArrayList<>();
        Random r = new Random();
        for(int i = 1; i < 13; i++) {
            int rand = r.nextInt(1000 - 1) + 1;
            urls.add("http://covers.openlibrary.org/b/ID/" + rand + "-L.jpg");
        }

        this.books = urls;*/
    }

    public void mockData() {

        Random r = new Random();
        for(int i = 1; i < 13; i++) {
            int rand = r.nextInt(1000 - 1) + 1;
            books.add(new Book("", "", "", "http://covers.openlibrary.org/b/ID/" + rand + "-L.jpg"));
        }

    }

    public void setContent(List<Book> books){
        books.clear();
        books.addAll(books);

        notifyDataSetChanged();
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
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            final View r = inflater.inflate(layoutResource, parent, false);
            final ViewHolder holder = new ViewHolder();

            holder.image = r.findViewById(R.id.image_layout_image);
            r.setTag(holder);

            String url = books.get(position).getImageUrl();
            Picasso.with(context).load(url).into(holder.image);

            return r;

        } else {
            final ViewHolder holder = (ViewHolder) row.getTag();
        }



        return row;
    }

    @Override
    public int getCount() {
        return books.size();
    }

    static class ViewHolder {
        ImageView image;
    }
}
