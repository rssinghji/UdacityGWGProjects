package com.project.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import utilities.JsonUtility;

//Adapter created for Recycler View
public class MovieDetailsAdapter extends RecyclerView.Adapter<MovieDetailsAdapter.MovieDetailsAdapterViewHolder> {
    //String array to hold data retrieved from MoviesDB API
    private String[] mMoviesData;

    //Create a place holder URL for a movie image
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";

    //Set a size for the image which could be altered in future if needed
    private static final String BASE_IMAGE_SIZE = "w185/";

    //A container for Context
    private static Context mContext;

    //A handler variable for adapter
    private final MovieDetailsAdapterOnClickHandler mClickHandler;

    //An interface for handling on click options to be implemented in MainActivity
    public interface MovieDetailsAdapterOnClickHandler{
        void onClick(String movie);
    }

    //Store the on click handler value
    public MovieDetailsAdapter(MovieDetailsAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

    //Holder of views to update recycler view
    public class MovieDetailsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Image view created to store the image poster to show on screen
        public final ImageView mMovieImageView;

        //Update the view holder of adapter and image view
        public MovieDetailsAdapterViewHolder(View view) {
            super(view);
            mMovieImageView = (ImageView) view.findViewById(R.id.iv_movie_data);
            view.setOnClickListener(this);
        }

        //Override onClick function to set up the position of item pressed for details in the recycler
        //view and call detail activity from main activity
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String movie = mMoviesData[adapterPosition];
            mClickHandler.onClick(movie);
        }
    }
    //Update view holder with view group anf view type
    @Override
    public MovieDetailsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        int layoutIdForGridItem = R.layout.movie_list;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForGridItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieDetailsAdapterViewHolder(view);
    }

    //Bind views of adapter to the positions and show image poster on that position
    @Override
    public void onBindViewHolder(MovieDetailsAdapterViewHolder movieDetailsAdapterViewHolder, int position) {
        String currentResult = mMoviesData[position];
        try {
            String imagePath = JsonUtility.getImagePath(currentResult);
            String imageURL = BASE_IMAGE_URL + BASE_IMAGE_SIZE + imagePath;
            Picasso.with(mContext).load(imageURL).into(movieDetailsAdapterViewHolder.mMovieImageView);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    //Sanity check for movie count
    @Override
    public int getItemCount() {
        if(null == mMoviesData)
            return 0;
        else
            return mMoviesData.length;
    }

    //Store the data retrieved from API call
    public void setMoviesData(String[] moviesData)
    {
        mMoviesData = moviesData;
        notifyDataSetChanged();
    }
}
