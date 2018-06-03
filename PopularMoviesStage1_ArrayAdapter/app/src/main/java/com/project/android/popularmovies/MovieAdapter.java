package com.project.android.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import model.Movie;

public class MovieAdapter extends BaseAdapter {
    private Context mContext;
    private Movie[] mMovies;

    /*public MovieAdapter(Context context, Movie[] movieList)
    {
        mContext = context;
        mMovies = movieList;
    }*/

    private MovieAdapter()
    {}

    private static MovieAdapter movieAdapter;
    public static MovieAdapter getMovieAdapterInstance()
    {
        if(movieAdapter == null)
            movieAdapter = new MovieAdapter();

        return movieAdapter;
    }

    @Override
    public int getCount() {
        return mMovies.length;
    }

    // 3
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Movie getItem(int position) {
        return mMovies[position];
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_grid, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_movie_data);
        try {
            Picasso.with(mContext).load(movie.getMoviePosterImage()).into(imageView);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        return convertView;
    }

    public void setContext(Context context)
    {
        mContext = context;
    }

    public void setMovies(Movie[] movies)
    {
        mMovies = movies;
    }

    public void updateAdapterMovies(Movie[] movies)
    {
        mMovies = movies;
        notifyDataSetChanged();
    }

}
