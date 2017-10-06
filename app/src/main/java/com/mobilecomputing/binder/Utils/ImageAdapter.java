package com.mobilecomputing.binder.Utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.mobilecomputing.binder.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by mikael on 2017-10-04.
 */

public class ImageAdapter extends ArrayAdapter {

    private List<String> imageUrls;
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

    public void setContent(List<String> urls){
        imageUrls.clear();
        imageUrls.addAll(urls);

        notifyDataSetChanged();
    }

    /**
     * Updates data for this adapter and notifies about the changes.
     * @param imageUrls data to use.
     */
    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
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
