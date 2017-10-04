package com.mobilecomputing.binder.Utils;
import com.mobilecomputing.binder.Objects.Match;


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


import com.mobilecomputing.binder.R;

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
        ImageView matchPicture = convertView.findViewById(R.id.match_picture);

        matchName.setText(match.name);
        //TODO fix this
        matchPercent.setText("45 %");

        return convertView;
    }
}
