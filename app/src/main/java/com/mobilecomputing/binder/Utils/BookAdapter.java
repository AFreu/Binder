package com.mobilecomputing.binder.Utils;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilecomputing.binder.Objects.Book;
import com.mobilecomputing.binder.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Libra on 11/10/17.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    private Context mContext;

    public BookAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
        mContext = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View v, @NonNull ViewGroup parent) {


        Book book = getItem(position);

        if (v == null){
            v = LayoutInflater.from(getContext()).inflate(R.layout.book_item, parent, false);
        }

        ImageView bookImage = v.findViewById(R.id.book_item_image);


        String url = book.getImageUrl();
        Picasso.with(mContext).load(url).into(bookImage);

        return v;
    }
}
