package com.project.android.popularmoviestage2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import model.MovieTrailer;

public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.MovieTrailersAdapterViewHolder> {
    //String array to hold data retrieved from MoviesDB API
    private MovieTrailer[] mTrailers;

    //A container for Context
    private static Context mContext;

    //A handler variable for adapter
    private final MovieTrailersAdapterOnClickHandler mClickHandler;

    //An interface for handling on click options to be implemented in MainActivity
    public interface MovieTrailersAdapterOnClickHandler{
        void onClick(MovieTrailer trailers);
    }

    //Store the on click handler value
    public MovieTrailersAdapter(MovieTrailersAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

    //Holder of views to update recycler view
    public class MovieTrailersAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Image view created to store the image poster to show on screen
        public final ImageView mMovieTrailerIconImageView;
        public final TextView mTrailerNameTextView;
        public final TextView mTrailerTypeTextView;

        //Update the view holder of adapter and image view
        public MovieTrailersAdapterViewHolder(View view) {
            super(view);
            mTrailerNameTextView = (TextView) view.findViewById(R.id.tv_trailer_name);
            mMovieTrailerIconImageView = (ImageView) view.findViewById(R.id.iv_trailer_icon);
            mTrailerTypeTextView = (TextView) view.findViewById(R.id.tv_trailer_type);
            view.setOnClickListener(this);
        }

        //Override onClick function to set up the position of item pressed for details in the recycler
        //view and call detail activity from main activity
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            MovieTrailer movieTrailer = mTrailers[adapterPosition];
            mClickHandler.onClick(movieTrailer);
        }
    }

    //Update view holder with view group and view type
    @Override
    public MovieTrailersAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_list;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieTrailersAdapterViewHolder(view);
    }

    //Bind views of adapter to the positions and show image poster on that position
    @Override
    public void onBindViewHolder(MovieTrailersAdapterViewHolder movieTrailersAdapterViewHolder, int position) {
        MovieTrailer currentResult = mTrailers[position];
        String trailerName = currentResult.getName();
        String trailerType = currentResult.getType();
        movieTrailersAdapterViewHolder.mTrailerNameTextView.setText(trailerName);
        movieTrailersAdapterViewHolder.mTrailerTypeTextView.setText(trailerType);
        Picasso.with(mContext).load(R.drawable.youtube_icon).into(movieTrailersAdapterViewHolder.mMovieTrailerIconImageView);
    }

    //Sanity check for trailers count
    @Override
    public int getItemCount() {
        if(null == mTrailers)
            return 0;
        else
            return mTrailers.length;
    }

    //Store the data retrieved from API call
    public void setMoviesTrailers(MovieTrailer[] trailersData)
    {
        mTrailers = trailersData;
        notifyDataSetChanged();
    }
}
