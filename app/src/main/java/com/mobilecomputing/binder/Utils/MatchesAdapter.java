package com.mobilecomputing.binder.Utils;
import com.mobilecomputing.binder.Objects.Book;
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

        TextView matchName = convertView.findViewById(R.id.match_name);
        TextView matchPercent = convertView.findViewById(R.id.match_percent);
        ImageView matchPicture = convertView.findViewById(R.id.match_picture);

        matchName.setText(match.getGivenName() + " " + match.getAge());
        matchPercent.setText(match.percent + " % match");
        Picasso.with(getContext()).load(match.getImageUrl()).into(matchPicture);

        ImageView matchBook1 = convertView.findViewById(R.id.match_book_1);
        ImageView matchBook2 = convertView.findViewById(R.id.match_book_2);
        ImageView matchBook3 = convertView.findViewById(R.id.match_book_3);


        TextView title = convertView.findViewById(R.id.match_interest);


        List<Book> fB = match.getFeaturedBooks();
        if(!fB.isEmpty()){
            Picasso.with(getContext()).load(fB.get(0).getImageUrl()).into(matchBook1);
            matchBook1.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            if(fB.size() > 1){
                Picasso.with(getContext()).load(fB.get(1).getImageUrl()).into(matchBook2);
                matchBook2.setVisibility(View.VISIBLE);
            }else{
                matchBook2.setVisibility(View.GONE);
            }
            if(fB.size() > 2){
                Picasso.with(getContext()).load(fB.get(2).getImageUrl()).into(matchBook3);
                matchBook3.setVisibility(View.VISIBLE);
            }else{
                matchBook3.setVisibility(View.GONE);
            }

        }else{
            matchBook1.setVisibility(View.GONE);
            title.setVisibility(View.INVISIBLE);
        }









        return convertView;
    }
}
