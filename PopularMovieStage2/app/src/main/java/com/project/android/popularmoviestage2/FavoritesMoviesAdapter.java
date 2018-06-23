package com.project.android.popularmoviestage2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import model.Movie;

public class FavoritesMoviesAdapter extends RecyclerView.Adapter<FavoritesMoviesAdapter.FavoritesMoviesAdapterViewHolder> {
    //String array to hold data retrieved from MoviesDB API
    private Movie[] mMovies;

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
    public class FavoritesMoviesAdapterViewHolder extends RecyclerView.ViewHolder/* implements View.OnClickListener*/{

        //Image view created to store the image poster to show on screen
        public final ImageView mFavoritePosterImageView;
        public final TextView mSynopsisTextView;
        public final TextView mRatingTextView;
        public final TextView mReleaseDateTextView;
        public final TextView mTitleTextView;

        //Update the view holder of adapter and image view
        public FavoritesMoviesAdapterViewHolder(View view) {
            super(view);
            mFavoritePosterImageView = (ImageView) view.findViewById(R.id.iv_favorite_poster);
            mSynopsisTextView = (TextView) view.findViewById(R.id.tv_favorite_synopsis);
            mRatingTextView = (TextView) view.findViewById(R.id.tv_favorite_rating);
            mReleaseDateTextView = (TextView) view.findViewById(R.id.tv_favorite_release_date);
            mTitleTextView = (TextView) view.findViewById(R.id.tv_favorite_title);
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
    public FavoritesMoviesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.favorite_list;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new FavoritesMoviesAdapterViewHolder(view);
    }

    //Bind views of adapter to the positions and show image poster on that position
    @Override
    public void onBindViewHolder(FavoritesMoviesAdapterViewHolder holder, int position) {
        Movie currentResult = mMovies[position];
        holder.mSynopsisTextView.setText(currentResult.getPlotSynopsis());
        holder.mRatingTextView.setText(currentResult.getUserRating());
        holder.mReleaseDateTextView.setText(currentResult.getReleaseDate());
        holder.mTitleTextView.setText(currentResult.getOriginalTitle());
        Picasso.with(mContext).load(currentResult.getMoviePosterImage()).into(holder.mFavoritePosterImageView);
    }

    public int getItemCount() {
        if(null == mMovies)
            return 0;
        else
            return mMovies.length;
    }

    //Store the data retrieved from API call
    public void setFavoriteMovies(Movie[] movies)
    {
        mMovies = movies;
        notifyDataSetChanged();
    }
}
