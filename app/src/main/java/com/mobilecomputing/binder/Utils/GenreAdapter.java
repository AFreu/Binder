package com.mobilecomputing.binder.Utils;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilecomputing.binder.Objects.Match;
import com.mobilecomputing.binder.R;

import java.util.List;


/**
 * Created by Libra on 06/10/17.
 */

public class GenreAdapter extends ArrayAdapter {


    public GenreAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String genre = (String) getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.add_genre, parent, false);
        }

        TextView text = convertView.findViewById(R.id.add_genre_text);


        text.setText(genre);

        return convertView;
    }
}
