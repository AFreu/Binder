package com.mobilecomputing.binder.Utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.mobilecomputing.binder.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import jp.wasabeef.blurry.Blurry;

/**
 * Created by mikael on 2017-10-04.
 */

public class ImageAdapter extends ArrayAdapter {

    private ArrayList<String> imageUrls = new ArrayList();
    private int layoutResource;
    private Context context;

    public ImageAdapter(@NonNull Context context, int layoutResource) {
        super(context, layoutResource);

        this.context = context;
        this.layoutResource = layoutResource;

        ArrayList<String> urls = new ArrayList<>();

        Random r = new Random();
        for(int i = 1; i < 13; i++) {
            int rand = r.nextInt(1000 - 1) + 1;
            urls.add("http://covers.openlibrary.org/b/ID/" + rand + "-L.jpg");
        }

        this.imageUrls = urls;
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

            String url = imageUrls.get(position);
            Picasso.with(context).load(url).into(holder.image);

            return r;

        } else {
            final ViewHolder holder = (ViewHolder) row.getTag();
        }



        return row;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    static class ViewHolder {
        ImageView image;
    }
}
