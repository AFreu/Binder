package com.mobilecomputing.binder.Utils;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilecomputing.binder.Objects.Book;
import com.mobilecomputing.binder.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Lovis on 04/10/17.
 */

public class ChooseBooksAdapter extends ArrayAdapter {


    private Context mContext;

    public ChooseBooksAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
        mContext = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        Book book = (Book) getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.book_list_item2, parent, false);
        }

        TextView bookTitle = (TextView) convertView.findViewById(R.id.book_title);
        TextView bookAuthor = (TextView) convertView.findViewById(R.id.book_author);
        ImageView bookPicture = (ImageView) convertView.findViewById(R.id.book_picture);

        bookTitle.setText(book.getTitle());
        //TODO fix this
        bookAuthor.setText("By " + book.getAuthor());
        String url = book.getImageUrl().replace("-M.jpg", "-L.jpg");
        Picasso.with(mContext).load(url).into(bookPicture);

        return convertView;
    }
}
