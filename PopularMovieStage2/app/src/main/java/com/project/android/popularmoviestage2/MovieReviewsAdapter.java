package com.project.android.popularmoviestage2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import model.MovieReview;

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.MovieReviewsAdapterViewHolder> {
    //String array to hold data retrieved from MoviesDB API
    private MovieReview[] mMovieReviews;

    //A container for Context
    private static Context mContext;

    /*
    //A handler variable for adapter
    private final MovieReviewsAdapterOnClickHandler mClickHandler;

    //An interface for handling on click options to be implemented in MainActivity
    public interface MovieReviewsAdapterOnClickHandler{
        void onClick(MovieReview review);
    }

    //Store the on click handler value
    public MovieReviewsAdapter(MovieReviewsAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }
    */

    //Holder of views to update recycler view
    public class MovieReviewsAdapterViewHolder extends RecyclerView.ViewHolder/* implements View.OnClickListener*/{

        //Image view created to store the image poster to show on screen
        public final ImageView mMovieReviewIconImageView;
        public final TextView mReviewAuthorNameTextView;
        public final TextView mReviewContentTextView;

        //Update the view holder of adapter and image view
        public MovieReviewsAdapterViewHolder(View view) {
            super(view);
            mReviewAuthorNameTextView = (TextView) view.findViewById(R.id.tv_review_author_name);
            mMovieReviewIconImageView = (ImageView) view.findViewById(R.id.iv_review_icon);
            mReviewContentTextView = (TextView) view.findViewById(R.id.tv_review_content);
            //view.setOnClickListener(this);
        }

        /*
        //Override onClick function to set up the position of item pressed for details in the recycler
        //view and call detail activity from main activity
        @Override
        public void onClick(View v) {
            // Currently nothing to implement but probably in future an expansion of compact comment
        }*/
    }

    //Update view holder with view group and view type
    @Override
    public MovieReviewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.reviews_list;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieReviewsAdapterViewHolder(view);
    }

    //Bind views of adapter to the positions and show image poster on that position
    @Override
    public void onBindViewHolder(MovieReviewsAdapterViewHolder movieReviewsAdapterViewHolder, int position) {
        MovieReview currentResult = mMovieReviews[position];
        String authorName = currentResult.getAuthor();
        String reviewContent = currentResult.getContent();
        movieReviewsAdapterViewHolder.mReviewAuthorNameTextView.setText(authorName);
        movieReviewsAdapterViewHolder.mReviewContentTextView.setText(reviewContent);
        Picasso.with(mContext).load(R.drawable.avatar_icon).into(movieReviewsAdapterViewHolder.mMovieReviewIconImageView);
    }

    public int getItemCount() {
        if(null == mMovieReviews)
            return 0;
        else
            return mMovieReviews.length;
    }

    //Store the data retrieved from API call
    public void setMoviesReviews(MovieReview[] reviewsData)
    {
        mMovieReviews = reviewsData;
        notifyDataSetChanged();
    }
}
