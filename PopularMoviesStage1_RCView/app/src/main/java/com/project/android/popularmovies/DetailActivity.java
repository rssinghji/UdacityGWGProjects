package com.project.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import com.squareup.picasso.Picasso;
import android.support.v7.app.ActionBar;

import model.Movie;
import utilities.JsonUtility;

//Create a separate activity to show movie detail screen
public class DetailActivity extends AppCompatActivity {

    //A json string to receive whole movie detail.
    private static final String EXTRA_DATA = "movie_data";

    //Creating corresponding views for text and poster
    private TextView mMovieOriginalTitleTextView;
    private TextView mMovieReleaseDateTextView;
    private TextView mMovieRatingTextView;
    private TextView mMovieLengthTextView;
    private TextView mMovieSynopsisTextView;
    private ImageView mMoviePosterImageView;

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

        //Initialize views of detail screen
        mMovieOriginalTitleTextView = (TextView) findViewById(R.id.tv_movie_name);
        mMovieReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        mMovieRatingTextView = (TextView) findViewById(R.id.tv_rating);
        mMovieLengthTextView = (TextView) findViewById(R.id.tv_movie_length);
        mMovieSynopsisTextView = (TextView) findViewById(R.id.tv_synopsis);
        mMoviePosterImageView = (ImageView) findViewById(R.id.iv_movie_poster);

        //Get intent passed from Main activity to use the json string containing details.
        Intent intent = getIntent();
        if(intent == null)
            closeOnError();
        String movie = intent.getStringExtra(EXTRA_DATA);
        if(movie.equals(null))
        {
            closeOnError();
            return;
        }

        //Use the Movie class to fit in data
        Movie movieData = null;
        try{
            //Parse json to get movie details for views.
            movieData = JsonUtility.getParsedMovieDetails(movie);
            if(movieData == null)
            {
                closeOnError();
                return;
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        //Populate the UI with parsed data
        populateUI(movieData);
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
    private void populateUI(Movie movie)
    {
        mMovieSynopsisTextView.setText(movie.getPlotSynopsis());
        mMovieReleaseDateTextView.setText(movie.getReleaseDate());
        mMovieRatingTextView.setText(movie.getUserRating());
        mMovieOriginalTitleTextView.setText(movie.getOriginalTitle());
        mMovieLengthTextView.setText(movie.getMovieLength());
        Picasso.with(this).load(movie.getMoviePosterImage()).into(mMoviePosterImageView);
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
        return super.onOptionsItemSelected(item);
    }
}
