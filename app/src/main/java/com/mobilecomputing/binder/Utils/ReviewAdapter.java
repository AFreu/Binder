package com.mobilecomputing.binder.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilecomputing.binder.Objects.Book;
import com.mobilecomputing.binder.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Libra on 13/10/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder>  {

    private Context mContext;
    private List<Review> reviewList;

    private View.OnClickListener reviewAdapterListener;


    public class ReviewHolder extends RecyclerView.ViewHolder {
        public TextView review;
        public ImageView image;

        public ReviewHolder(View view) {
            super(view);
            review = view.findViewById(R.id.review_item_text);
            image =  view.findViewById(R.id.review_item_picture);


        }
    }

    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public void setOnClickListener(View.OnClickListener reviewAdapterListener) {
        this.reviewAdapterListener = reviewAdapterListener;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);


        itemView.setOnClickListener(reviewAdapterListener);
        mContext = parent.getContext();

        return new ReviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.review.setText(review.getReviewText());
        Picasso.with(mContext).load(review.getReviewUser().getImageUrl()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public Review getReview(int position){
        return reviewList.get(position);
    }
}
