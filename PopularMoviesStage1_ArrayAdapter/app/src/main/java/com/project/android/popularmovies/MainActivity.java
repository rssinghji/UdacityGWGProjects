package com.project.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.net.URL;

import model.Movie;
import utilities.JsonUtility;
import utilities.NetworkUtility;

public class MainActivity extends AppCompatActivity {

    private static Movie[] mMovieListPopular;
    private static Movie[] mMovieListTopRated;
    private static Movie[] mMoviesData;

    //Create default sort option
    private String SORT_OPTION = "popular";
    private final String MOVIE_DETAILS = "movie_details";
    public static final String BUNDLE_STATE = "movies";
    public static final String SPINNER_POSITION = "spinner_position";

    private Context mContext;
    private MovieAdapter mMovieAdapter;
    private Spinner mSpinner;
    private static int mSpinnerPosition = -1;
    private boolean bToggle = false;

    private GridView mGridView;
    //Create place holders for error message display and progress bar
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Attach error message TextView to its layout
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        //Attach progress Bar to its layout
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mGridView = (GridView) findViewById(R.id.gv_movies_posters);

        mContext = this;

        if(savedInstanceState != null)
        {
            if(savedInstanceState.containsKey(BUNDLE_STATE) && savedInstanceState.containsKey(SPINNER_POSITION))
            {
                mMoviesData = (Movie[]) savedInstanceState.getParcelableArray(BUNDLE_STATE);
                mSpinnerPosition = savedInstanceState.getInt(SPINNER_POSITION);
                bToggle = true;
                mMovieAdapter = MovieAdapter.getMovieAdapterInstance();
                mMovieAdapter.setContext(mContext);
                mMovieAdapter.updateAdapterMovies(mMoviesData);
                mGridView.setAdapter(mMovieAdapter);
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        launchDetailActivity(position);
                    }
                });
            }
        }
        else {

            mMovieAdapter = MovieAdapter.getMovieAdapterInstance();
            mMovieAdapter.setContext(mContext);
            mMovieAdapter.setMovies(mMoviesData);
            //Finally call the function to load call APIs to load movies data
            loadMoviesData();
        }
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
        Function to start rendering movie posters on screen with other views getting hidden
        inputs(s): none
        output: void
     */
    private void showMoviesDataView()
    {
        //Start with removing error message
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);

        //Set up visibility for recycler view
        mGridView.setVisibility(View.VISIBLE);
    }

    /*
        Async task to start retrieving movie information in a separate thread
     */
    public class FetchMoviesData extends AsyncTask<String, Void, Movie[]> {

        //At loading time, show the loading indicator as onPreExecute()
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        //Intensive work to be done in background
        @Override
        protected Movie[] doInBackground(String... params) {
            URL moviesDataRequestUrl = NetworkUtility.buildUrl(SORT_OPTION);
            try
            {
                String moviesResponse = NetworkUtility.getResponseFromHttpUrl(moviesDataRequestUrl);
                if(SORT_OPTION.equals("popular"))
                {
                    mMovieListPopular = JsonUtility.getParsedMovieDetailsinMovieArray(moviesResponse);
                    return mMovieListPopular;
                }
                else {
                    mMovieListTopRated = JsonUtility.getParsedMovieDetailsinMovieArray(moviesResponse);
                    return mMovieListTopRated;
                }
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
                return null;
            }
        }

        //Finishing touch
        @Override
        protected void onPostExecute(Movie[] moviesData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if(moviesData != null)
            {
                showMoviesDataView();
                mMoviesData = moviesData;
                mMovieAdapter = MovieAdapter.getMovieAdapterInstance();
                mMovieAdapter.setContext(mContext);
                mMovieAdapter.setMovies(mMoviesData);
                mGridView.setAdapter(mMovieAdapter);
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        launchDetailActivity(position);
                    }
                });
            }
            else
            {
                showErrorMessage();
            }
        }
    }

    private void launchDetailActivity(int position)
    {
        Intent intent = new Intent(this,DetailActivity.class);
        Movie movie = mMoviesData[position];
        intent.putExtra(MOVIE_DETAILS,movie);
        startActivity(intent);
    }

    /*
        Function to show Error when loading or rendering fails by setting relevant views visibility info
        input(s): none
        output: void
     */
    private void showErrorMessage()
    {
        //First hide recycler view from action
        mGridView.setVisibility(View.INVISIBLE);

        //Show the error message if loading doesn't show the movies info
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.spinner_menu, menu);

        //MenuItem item = menu.findItem(R.id.spinner);
        //Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        mSpinner = (Spinner) menu.findItem(R.id.spinner).getActionView();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.options_list, R.layout.custom_spinner_item);
        adapter.setDropDownViewResource(R.layout.custom_drop_down);
        mSpinner.setAdapter(adapter);
        //8388613 = "end"  5 = "right"
        mSpinner.setGravity(8388613);

        if(bToggle)
            mSpinner.setSelection(mSpinnerPosition);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                String optionSelected = adapterView.getItemAtPosition(position).toString();
                if(optionSelected.equals("Sort By: Rating"))
                {
                    SORT_OPTION = "top_rated";
                    if(mMovieListTopRated == null)
                        loadMoviesData();
                    else {
                        mMovieAdapter.updateAdapterMovies(mMovieListTopRated);
                        mMoviesData = mMovieListTopRated;
                    }
                }
                else
                {
                    SORT_OPTION = "popular";
                    if(mMovieListPopular == null)
                        loadMoviesData();
                    else {
                        mMovieAdapter.updateAdapterMovies(mMovieListPopular);
                        mMoviesData = mMovieListPopular;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle)
    {
        super.onSaveInstanceState(bundle);
        bundle.putParcelableArray(BUNDLE_STATE, mMoviesData);
        if (SORT_OPTION.equals("popular")) {
            bundle.putInt(SPINNER_POSITION, 0);
        } else {
            bundle.putInt(SPINNER_POSITION, 1);
        }
    }
}
