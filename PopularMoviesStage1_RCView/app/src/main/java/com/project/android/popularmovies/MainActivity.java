package com.project.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;

import utilities.JsonUtility;
import utilities.NetworkUtils;

//Main Activity implementing Popular movies layout and its recycler view adpater
public class MainActivity extends AppCompatActivity implements MovieDetailsAdapter.MovieDetailsAdapterOnClickHandler{

    //Create the members pr place holders for recycler view and its adapter
    private RecyclerView mRecyclerView;
    private MovieDetailsAdapter mMovieDetailsAdapter;

    //Create place holders for error message display and progress bar
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    //Create the number of rows or columns supposed to be displayed in a screen frame
    private static int SPAN_COUNT = 2;

    //A string to send data to detail activity
    private static String EXTRA_DATA = "movie_data";

    //Create default sort option
    private static String SORT_OPTION = "popular";

    //Create an array of strings to store JSON API Response
    private String[] mMovieDetails;

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

        //Finally call the function to load call APIs to load movies data
        loadMoviesData();
    }

    /*
        A local function to start loading and rendering of data on screen
        input(s): none
        output: void
     */
    private void loadMoviesData()
    {
        //Show the recycler view in action
        showMoviesDataView();

        //Run an async task(a separate thread) to load movies data using theMoviesDB APIs
        new FetchMoviesData().execute();
    }

    /*
        Overriding onClick event for any item being clicked in recycler view to launch detail activity
        with the movie detail json string
        input(s): String of movie detail in Json
        output: void
     */
    @Override
    public void onClick(String movie) {
        Context context = this;
        launchDetailActivity(movie);
    }

    /*
        Function to launch detail activity
        input(s): String of movie detail in Json
        output: void
     */
    private void launchDetailActivity(String movie)
    {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(EXTRA_DATA, movie);
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
        Async task to start retrieving movie information in a separate thread
     */
    public class FetchMoviesData extends AsyncTask<String, Void, String[]> {

        //At loading time, show the loading indicator as onPreExecute()
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        //Intensive work to be done in background
        @Override
        protected String[] doInBackground(String... params) {
            URL moviesDataRequestUrl = NetworkUtils.buildUrl(SORT_OPTION);
            try
            {
                String moviesResponse = NetworkUtils.getResponseFromHttpUrl(moviesDataRequestUrl);
                mMovieDetails = JsonUtility.getMoviesResults(moviesResponse);
                return mMovieDetails;
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
                return null;
            }
        }

        //Finishing touch
        @Override
        protected void onPostExecute(String[] moviesData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if(moviesData != null)
            {
                showMoviesDataView();
                mMovieDetailsAdapter.setMoviesData(moviesData);
            }
            else
            {
                showErrorMessage();
            }
        }
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
                loadMoviesData();
                break;

            default:
                SORT_OPTION = "popular";
                loadMoviesData();
                break;
        }
        return true;
    }
}
