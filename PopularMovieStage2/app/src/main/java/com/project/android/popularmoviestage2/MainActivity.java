package com.project.android.popularmoviestage2;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v4.app.LoaderManager.LoaderCallbacks;

import java.net.URL;

import model.Movie;
import utilities.JsonUtility;
import utilities.NetworkUtility;

//Main Activity implementing Popular movies layout and its recycler view adpater
public class MainActivity extends AppCompatActivity implements MovieDetailsAdapter.MovieDetailsAdapterOnClickHandler,
LoaderManager.LoaderCallbacks<Movie[]>{

    //Create storage containers for retaining data for further operations.
    private static Movie[] mMovieListPopular;
    private static Movie[] mMovieListTopRated;
    private static Movie[] mMoviesData;

    //Create the members pr place holders for recycler view and its adapter
    private RecyclerView mRecyclerView;
    private MovieDetailsAdapter mMovieDetailsAdapter;

    //Create place holders for error message display and progress bar
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    //Create default sort option
    private static String SORT_OPTION = "popular";

    //integer for Loader
    private static final int MOVIES_LOADER_ID = 12;

    //onCreate function to initialize views and ready other data
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Attach recycler view to its layout
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_posters);

        //Attach error message TextView to its layout
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        //Attach progress Bar to its layout
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        //Create the number of rows or columns supposed to be displayed in a screen frame
        int SPAN_COUNT = 2;

        //Create a Grid Layout Manager for the Recycler View
        GridLayoutManager layoutManager = new GridLayoutManager(this, SPAN_COUNT,
        GridLayoutManager.VERTICAL, false);

        //Attach Layout Manager to recycler view
        mRecyclerView.setLayoutManager(layoutManager);

        //Use setHasFixedSize(true) on mRecyclerView to designate that all items in the list
        // will have the same size
        mRecyclerView.setHasFixedSize(true);

        //Create and Connect Adapter to the Recycler View
        mMovieDetailsAdapter = new MovieDetailsAdapter(this);
        mRecyclerView.setAdapter(mMovieDetailsAdapter);

        LoaderCallbacks<Movie[]> callback = MainActivity.this;
        getSupportLoaderManager().initLoader(MOVIES_LOADER_ID, null, callback);
    }

    /*
        Overriding onClick event for any item being clicked in recycler view to launch detail activity
        with the movie detail json string
        input(s): String of movie detail in Json
        output: void
     */
    @Override
    public void onClick(Movie movie) {
        Context context = this;
        launchDetailActivity(movie);
    }

    /*
        Function to launch detail activity
        input(s): String of movie detail in Json
        output: void
     */
    private void launchDetailActivity(Movie movie)
    {
        final String MOVIE_DETAILS = "movie_details";
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(MOVIE_DETAILS, movie);
        startActivity(intent);
    }

    /*
        Function to start rendering movie posters on screen with other views getting hidden
        inputs(s): none
        output: void
     */
    private void showMoviesDataView()
    {
        //Start with removing error message
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);

        //Set up visibility for recycler view
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /*
        Function to show Error when loading or rendering fails by setting relevant views visibility info
        input(s): none
        output: void
     */
    private void showErrorMessage()
    {
        //First hide recycler view from action
        mRecyclerView.setVisibility(View.INVISIBLE);

        //Show the error message if loading doesn't show the movies info
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    /*
        AsyncTaskLoader for complete LifeCycle Awareness
     */

    @Override
    public Loader<Movie[]> onCreateLoader(int i, Bundle bundle) {
        return new android.support.v4.content.AsyncTaskLoader<Movie[]>(this) {

            @Override
            protected void onStartLoading() {
                if (mMovieListPopular != null && SORT_OPTION.equals("popular")) {
                    deliverResult(mMovieListPopular);
                }
                else if(mMovieListTopRated != null && SORT_OPTION.equals("top_rated"))
                {
                    deliverResult(mMovieListTopRated);
                }
                else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public Movie[] loadInBackground() {
                URL moviesDataRequestUrl = NetworkUtility.buildUrl(SORT_OPTION);
                try
                {
                    String moviesResponse = NetworkUtility.getResponseFromHttpUrl(moviesDataRequestUrl);
                    if(SORT_OPTION.equals("popular"))
                    {
                        mMovieListPopular = JsonUtility.getParsedMovieDetailsInMovieArray(moviesResponse);
                        return mMovieListPopular;
                    }
                    else {
                        mMovieListTopRated = JsonUtility.getParsedMovieDetailsInMovieArray(moviesResponse);
                        return mMovieListTopRated;
                    }
                }
                catch (Exception exception)
                {
                    exception.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Movie[] data) {
                mMoviesData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Movie[]> loader, Movie[] data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if(mMoviesData != null)
        {
            showMoviesDataView();
            mMoviesData = data;
            mMovieDetailsAdapter.setMoviesData(mMoviesData);
        }
        else
        {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Movie[]> loader) {

    }

    /*
        Overriding onCreateOptionsMenu to create the Menu options for sorting
        input(s): Menu object
        output: boolean result(true in this case for inflation of menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /*
        Overriding onOptionsItemSelected to update screen for sorting option selected
        input(s): MenuItem object
        output: boolean true as we're always keeping a default option on selection
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemSelected = item.getItemId();
        switch(itemSelected)
        {
            case R.id.action_sort_top_rated:
                SORT_OPTION = "top_rated";
                if(mMovieListTopRated == null) {
                    LoaderCallbacks<Movie[]> callback = MainActivity.this;
                    getSupportLoaderManager().restartLoader(MOVIES_LOADER_ID, null, callback);
                }
                else
                {
                    mMovieDetailsAdapter.setMoviesData(mMovieListTopRated);
                    mMoviesData = mMovieListTopRated;
                }
                break;

            case R.id.action_sort_popular:
                SORT_OPTION = "popular";
                if(mMovieListPopular == null)
                {
                    LoaderCallbacks<Movie[]> callback = MainActivity.this;
                    getSupportLoaderManager().restartLoader(MOVIES_LOADER_ID, null, callback);
                }
                else
                {
                    mMovieDetailsAdapter.setMoviesData(mMovieListPopular);
                    mMoviesData = mMovieListPopular;
                }
            case R.id.action_show_favorites:
                launchFavoriteActivity();
                break;
        }
        return true;
    }

    private void launchFavoriteActivity() {
        Intent intent = new Intent(this,FavoritesActivity.class);
        startActivity(intent);
    }
}
