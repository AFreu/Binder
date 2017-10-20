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
import com.mobilecomputing.binder.Objects.Match;
import com.mobilecomputing.binder.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Lovis on 04/10/17.
 */

public class BooksAdapter extends ArrayAdapter {


    private Context mContext;

    public BooksAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
        mContext = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        Book book = (Book) getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.book_list_item, parent, false);
        }

        TextView matchName = (TextView) convertView.findViewById(R.id.book_title);
        TextView matchPercent = (TextView) convertView.findViewById(R.id.book_author);
        ImageView matchPicture = (ImageView) convertView.findViewById(R.id.book_picture);

        matchName.setText(book.getTitle());
        //TODO fix this
        matchPercent.setText("By " + book.getAuthor());
        String str = book.getImageUrl();
        if(str != "")
            str = str.replace("L.jpg", "M.jpg");
        try {
            Picasso.with(mContext).load(str).into(matchPicture);
        }
        catch (Exception e){

        }

        return convertView;
    }
}
