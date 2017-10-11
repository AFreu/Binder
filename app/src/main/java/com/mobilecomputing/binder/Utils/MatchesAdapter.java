package com.mobilecomputing.binder.Utils;
import com.mobilecomputing.binder.Objects.Match;


import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.mobilecomputing.binder.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Lovis on 04/10/17.
 */

public class MatchesAdapter extends ArrayAdapter {


    public MatchesAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        Match match = (Match) getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.match_item, parent, false);
        }

        TextView matchName = (TextView) convertView.findViewById(R.id.match_name);
        TextView matchPercent = (TextView) convertView.findViewById(R.id.match_percent);
        ImageView matchPicture = (ImageView) convertView.findViewById(R.id.match_picture);

        matchName.setText(match.name + " " + match.age);
        matchPercent.setText(match.percent + " % match");
        Picasso.with(getContext()).load(match.pictureUrl).into(matchPicture);


        return convertView;
    }
}
