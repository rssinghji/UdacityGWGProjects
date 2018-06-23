package com.project.android.popularmoviestage2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

import com.project.android.popularmoviestage2.data.FavoriteSelectionContract;
import com.project.android.popularmoviestage2.data.FavoriteSelectionDBHelper;
import com.squareup.picasso.Picasso;
import android.support.v7.app.ActionBar;

import java.net.URL;

import model.Movie;
import model.MovieReview;
import model.MovieTrailer;
import utilities.JsonUtility;
import utilities.NetworkUtility;

import static utilities.JsonUtility.getParsedReviews;
import static utilities.JsonUtility.getParsedTrailers;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

class MovieDetailLoader {
    public MovieTrailer[] trailers;
    public MovieReview[]  reviews;
}

//Create a separate activity to show movie detail screen
public class DetailActivity extends AppCompatActivity implements MovieTrailersAdapter.MovieTrailersAdapterOnClickHandler
{

    //A json string to receive whole movie detail.
    private static final String MOVIE_DETAILS = "movie_details";

    //Create a place holder for the trailers
    private static MovieTrailer[] mTrailers;
    private static int mId;

    //Create a place holder for reviews
    private static MovieReview[] mReviews;
    private static MovieDetailLoader mMovieTrailersReviews;

    //Creating corresponding views for text and poster and then trailers
    private TextView mMovieOriginalTitleTextView;
    private TextView mMovieReleaseDateTextView;
    private TextView mMovieRatingTextView;
    private TextView mMovieLengthTextView;
    private TextView mMovieSynopsisTextView;
    private ImageView mMoviePosterImageView;
    private RecyclerView mMovieTrailersRecyclerView;
    private MovieTrailersAdapter mMovieTrailersAdapter;
    private RecyclerView mMovieReviewsRecyclerView;
    private MovieReviewsAdapter mMovieReviewsAdapter;

    //DB variable to access DB and make DB queries
    private SQLiteDatabase mDB;
    private TextView mFavoriteSelectionTextView;
    private ImageView mFavoriteSelectionImageView;

    /*
        The function called when activity is started/created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //An action Bar to support back navigation from the movie detail screen
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        //Create a DB helper for the DB
        FavoriteSelectionDBHelper dbHelper = new FavoriteSelectionDBHelper(this);
        mDB = dbHelper.getWritableDatabase();
        mFavoriteSelectionTextView = (TextView) findViewById(R.id.tv_set_favorite);
        mFavoriteSelectionImageView = (ImageView) findViewById(R.id.iv_favorite_icon);

        //Initialize views of detail screen
        mMovieOriginalTitleTextView = (TextView) findViewById(R.id.tv_movie_name);
        mMovieReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        mMovieRatingTextView = (TextView) findViewById(R.id.tv_rating);
        mMovieLengthTextView = (TextView) findViewById(R.id.tv_movie_length);
        mMovieSynopsisTextView = (TextView) findViewById(R.id.tv_synopsis);
        mMoviePosterImageView = (ImageView) findViewById(R.id.iv_movie_poster);
        mMovieTrailersRecyclerView = (RecyclerView) findViewById(R.id.rv_trailers);
        mMovieReviewsRecyclerView = (RecyclerView) findViewById(R.id.rv_reviews);

        //Create a Linear layout manager for recycler view for trailers
        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
        false);

        //Attach Layout Manager to recycler view
        mMovieTrailersRecyclerView.setLayoutManager(trailerLayoutManager);

        //Use setHasFixedSize(true) on mRecyclerView to designate that all items in the list
        // will have the same size
        mMovieTrailersRecyclerView.setHasFixedSize(true);

        //Create and Connect Adapter to the Recycler View
        mMovieTrailersAdapter = new MovieTrailersAdapter(this);
        mMovieTrailersRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mMovieTrailersRecyclerView.setAdapter(mMovieTrailersAdapter);

        //Create a Linear Layout manager for recycler view for reviews
        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
        false);

        //Attach layout manager to recycler view
        mMovieReviewsRecyclerView.setLayoutManager(reviewLayoutManager);

        //Use setHasFixedSize(true) on mRecyclerView to designate that all items in the list
        // will have the same size
        mMovieReviewsRecyclerView.setHasFixedSize(true);

        //Create and Connect Adapter to the Recycler View
        mMovieReviewsAdapter = new MovieReviewsAdapter();
        mMovieReviewsRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mMovieReviewsRecyclerView.setAdapter(mMovieReviewsAdapter);

        //Get intent passed from Main activity to use the json string containing details.
        Intent intent = getIntent();
        if(intent == null)
            closeOnError();
        try{
            //Parse json to get movie details for views.
            Movie movieData = intent.getParcelableExtra(MOVIE_DETAILS);
            if(movieData == null)
            {
                closeOnError();
                return;
            }

            //Populate the UI with parsed data
            populateUI(movieData);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    /*
        A function to indicate an erroneous behavior
        input(s): None
        output: void
     */
    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detailed_error_message, Toast.LENGTH_SHORT).show();
    }

    /*
        A function to populate UI views with parsed data
        input(s): A Movie class object
        output: void
     */
    private void populateUI(final Movie movie)
    {
        mId = movie.getId();
        new FetchTrailersData().execute();
        new FetchReviewsData().execute();
        mMovieTrailersRecyclerView.setVisibility(View.VISIBLE);
        mMovieSynopsisTextView.setText(movie.getPlotSynopsis());
        mMovieReleaseDateTextView.setText(movie.getReleaseDate());
        mMovieRatingTextView.setText(movie.getUserRating());
        mMovieOriginalTitleTextView.setText(movie.getOriginalTitle());
        mMovieLengthTextView.setText(movie.getMovieLength());
        Picasso.with(this).load(movie.getMoviePosterImage()).into(mMoviePosterImageView);
        readOriginalFavoriteSelection(movie);
        mFavoriteSelectionImageView.setClickable(true);
        mFavoriteSelectionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInfoAvailable(movie);
            }
        });
    }

    private void checkInfoAvailable(Movie movie) {
        String[] whereArgs = new String[]{String.valueOf(movie.getId())};
        Cursor cursor = mDB.query("favorite_movies",
        null,
        "movie_id=?",
            whereArgs,
            null,
            null,
            null
        );
        int cursorCount = cursor.getCount();
        if(cursorCount == 0){
            //Insert Movie details and toggle favorite icon
            ContentValues contentValues = new ContentValues();
            contentValues.put(FavoriteSelectionContract.FavoriteSelectionEntry.MOVIE_ID, movie.getId());
            contentValues.put(FavoriteSelectionContract.FavoriteSelectionEntry.MOVIE_NAME, movie.getOriginalTitle());
            contentValues.put(FavoriteSelectionContract.FavoriteSelectionEntry.IS_FAVORITE, 1);
            contentValues.put(FavoriteSelectionContract.FavoriteSelectionEntry.SYNOPSIS, movie.getPlotSynopsis());
            contentValues.put(FavoriteSelectionContract.FavoriteSelectionEntry.POSTER_URL, movie.getMoviePosterImage());
            contentValues.put(FavoriteSelectionContract.FavoriteSelectionEntry.RATING, movie.getUserRating());
            contentValues.put(FavoriteSelectionContract.FavoriteSelectionEntry.RELEASE_DATE, movie.getReleaseDate());

            mDB.insert(FavoriteSelectionContract.FavoriteSelectionEntry.TABLE_NAME, null, contentValues);
            mFavoriteSelectionTextView.setText(R.string.set_favorite_string);
            Picasso.with(this).load(R.drawable.favorite_icon).into(mFavoriteSelectionImageView);
        }
        else
        {
            //Read the boolean value and toggle an update
            cursor.moveToPosition(0);
            int selection = cursor.getInt(cursor.getColumnIndex(FavoriteSelectionContract.FavoriteSelectionEntry.IS_FAVORITE));
            if(selection == 0)
                selection = 1;
            else
                selection = 0;
            ContentValues contentValues = new ContentValues();
            contentValues.put(FavoriteSelectionContract.FavoriteSelectionEntry.MOVIE_ID, movie.getId());
            contentValues.put(FavoriteSelectionContract.FavoriteSelectionEntry.MOVIE_NAME, movie.getOriginalTitle());
            contentValues.put(FavoriteSelectionContract.FavoriteSelectionEntry.IS_FAVORITE, selection);
            contentValues.put(FavoriteSelectionContract.FavoriteSelectionEntry.SYNOPSIS, movie.getPlotSynopsis());
            contentValues.put(FavoriteSelectionContract.FavoriteSelectionEntry.POSTER_URL, movie.getMoviePosterImage());
            contentValues.put(FavoriteSelectionContract.FavoriteSelectionEntry.RATING, movie.getUserRating());
            contentValues.put(FavoriteSelectionContract.FavoriteSelectionEntry.RELEASE_DATE, movie.getReleaseDate());
            mDB.update(FavoriteSelectionContract.FavoriteSelectionEntry.TABLE_NAME, contentValues,"movie_id=?",
            whereArgs);
            if(selection == 1)
            {
                mFavoriteSelectionTextView.setText(R.string.set_favorite_string);
                Picasso.with(this).load(R.drawable.favorite_icon).into(mFavoriteSelectionImageView);
            }
            else {
                mFavoriteSelectionTextView.setText(R.string.default_favorite_string);
                Picasso.with(this).load(R.drawable.unfavorite_icon).into(mFavoriteSelectionImageView);
            }
        }
    }

    private void readOriginalFavoriteSelection(Movie movie){
        String[] whereArgs = new String[]{String.valueOf(movie.getId())};
        Cursor cursor = mDB.query("favorite_movies",
        null,
        "movie_id=?",
        whereArgs,
        null,
        null,
        null
        );
        int cursorCount = cursor.getCount();
        if(cursorCount == 0){
            mFavoriteSelectionTextView.setText(R.string.default_favorite_string);
            Picasso.with(this).load(R.drawable.unfavorite_icon).into(mFavoriteSelectionImageView);
        }
        else {
            cursor.moveToPosition(0);
            int selection = cursor.getInt(cursor.getColumnIndex(FavoriteSelectionContract.FavoriteSelectionEntry.IS_FAVORITE));
            if(selection == 1)
            {
                mFavoriteSelectionTextView.setText(R.string.set_favorite_string);
                Picasso.with(this).load(R.drawable.favorite_icon).into(mFavoriteSelectionImageView);
            }
            else {
                mFavoriteSelectionTextView.setText(R.string.default_favorite_string);
                Picasso.with(this).load(R.drawable.unfavorite_icon).into(mFavoriteSelectionImageView);
            }
        }
    }

    /*
        Overriding onOptionsItemSelected function for back navigation support from detail screen, if home
        button is pressed,
        input(s): MenuItem object
        output: boolean response of selection or navigation
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home)
        {
            NavUtils.navigateUpFromSameTask(this);
        }
        else if(itemId == R.id.action_show_favorites)
            launchFavoriteActivity();
        else if(itemId == R.id.action_share)
        {
            item.setIntent(createShareTrailerIntent());
        }
        return super.onOptionsItemSelected(item);
    }

    /*
        Overriding onClick event for any item being clicked in recycler view to launch detail activity
        with the movie detail json string
        input(s): String of movie detail in Json
        output: void
     */
    @Override
    public void onClick(MovieTrailer movieTrailer) {
        Context context = this;
        //To implement the youtube trailer play here.
        openTrailerLink(movieTrailer.getSite());
    }

    private void openTrailerLink(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /*
        Async task to start retrieving movie trailers information in a separate thread
     */
    public class FetchTrailersData extends AsyncTask<String, Void, MovieTrailer[]> {

        //At loading time, show the loading indicator as onPreExecute()
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //Intensive work to be done in background
        @Override
        protected MovieTrailer[] doInBackground(String... params) {
            MovieTrailer[] trailers;

            //Give a call to get trailers JSON
            URL trailerURl = NetworkUtility.buildURLTrailers(mId);
            try {
                String trailerResponse = NetworkUtility.getResponseFromHttpUrl(trailerURl);
                trailers = getParsedTrailers(trailerResponse);
                Log.d("trailerLength",String.valueOf(trailers.length));
                return trailers;
            } catch (Exception exception) {
                exception.printStackTrace();
                return null;
            }
        }

        //Finishing touch
        @Override
        protected void onPostExecute(MovieTrailer[] trailers) {
            if(trailers != null) {
                //TODO - Check whether its(mTrailers) needed or not.
                mTrailers = trailers;
                mMovieTrailersAdapter.setMoviesTrailers(trailers);
            }
        }
    }

    /*
        Async task to start retrieving movie reviews information in a separate thread
     */
    public class FetchReviewsData extends AsyncTask<String, Void, MovieReview[]> {

        //At loading time, show the loading indicator as onPreExecute()
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //Intensive work to be done in background
        @Override
        protected MovieReview[] doInBackground(String... params) {
            MovieReview[] reviews;

            //Give a call to get trailers JSON
            URL reviewURl = NetworkUtility.buildURLReviews(mId);
            try {
                String reviewsResponse = NetworkUtility.getResponseFromHttpUrl(reviewURl);
                reviews = getParsedReviews(reviewsResponse);
                Log.d("no. of reviews ",String.valueOf(reviews.length));
                return reviews;
            } catch (Exception exception) {
                exception.printStackTrace();
                return null;
            }
        }

        //Finishing touch
        @Override
        protected void onPostExecute(MovieReview[] reviews) {
            if(reviews != null) {
                //TODO - Check whether its(mReviews) needed or not.
                mReviews = reviews;
                mMovieReviewsAdapter.setMoviesReviews(reviews);
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
        getMenuInflater().inflate(R.menu.list_favorites_menu, menu);
        return true;
    }

    private void launchFavoriteActivity() {
        Intent intent = new Intent(this,FavoritesActivity.class);
        startActivity(intent);
    }

    private Intent createShareTrailerIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
        .setType("text/plain")
        .setText(mTrailers[0].getSite())
        .getIntent();
        return shareIntent;
    }
}
