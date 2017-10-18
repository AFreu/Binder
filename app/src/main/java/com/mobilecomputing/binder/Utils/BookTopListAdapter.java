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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobilecomputing.binder.Objects.Book;
import com.mobilecomputing.binder.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Libra on 11/10/17.
 */

public class BookTopListAdapter extends ArrayAdapter<Book> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

    private Context mContext;
    private ArrayList<Book> mData = new ArrayList<>();
    private TreeSet mSeparatorsSet = new TreeSet();


    private LayoutInflater mInflater;

    public BookTopListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
        mContext = context;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final Book item) {
        if (!mData.contains(item)) {
            mData.add(item);
            notifyDataSetChanged();
        }
    }

    public void addManyItems(final List<Book> items) {
        mData.addAll(items);
        notifyDataSetChanged();
    }

    public void addSeparatorItem(final Book item) {
        Book book = new Book();
        mData.add(book);
        // save separator position
        mSeparatorsSet.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Book getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        int type = getItemViewType(position);
        System.out.println("getView " + position + " " + convertView + " type = " + type);
        if (convertView == null) {
            switch (type) {
                case TYPE_ITEM:
                    if (convertView == null){
                        convertView = LayoutInflater.from(getContext()).inflate(R.layout.book_item, parent, false);
                    }
                    ImageView bookImage = convertView.findViewById(R.id.book_item_image);
                    Book book = mData.get(position);
                    String url = book.getImageUrl();
                    Picasso.with(mContext).load(url).into(bookImage);
                    break;
                case TYPE_SEPARATOR:
                    if (convertView == null){
                        convertView = LayoutInflater.from(getContext()).inflate(R.layout.add_toplist_item, parent, false);
                    }
                    break;
            }
        } else {
        }
        return convertView;
    }

}
