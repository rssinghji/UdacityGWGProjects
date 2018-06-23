package com.project.android.popularmoviestage2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.project.android.popularmoviestage2.data.FavoriteSelectionContract;
import com.project.android.popularmoviestage2.data.FavoriteSelectionDBHelper;

import model.Movie;

public class FavoritesActivity extends AppCompatActivity {

    private SQLiteDatabase mDB;

    //Create the members pr place holders for recycler view and its adapter
    private RecyclerView mRecyclerView;
    private FavoritesMoviesAdapter mFavoritesAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        //An action Bar to support back navigation from the movie detail screen
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_favorites);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
        false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mFavoritesAdapter = new FavoritesMoviesAdapter();
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mFavoritesAdapter);

        //Create a DB helper for the DB
        FavoriteSelectionDBHelper dbHelper = new FavoriteSelectionDBHelper(this);
        mDB = dbHelper.getReadableDatabase();
        populateUI();
    }

    private void populateUI() {

        String[] whereArgs = new String[]{String.valueOf(1)};
        Cursor cursor = mDB.query("favorite_movies",
        null,
        "is_favorite=?",
        whereArgs,
        null,
        null,
        null
        );
        Movie[] movies = new Movie[cursor.getCount()];
        for (int index = 0; index < cursor.getCount(); index++)
        {
            cursor.moveToPosition(index);
            Movie movie = new Movie();
            movie.setMoviePosterImage(cursor.getString(cursor.getColumnIndex(FavoriteSelectionContract.FavoriteSelectionEntry.POSTER_URL)));
            movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(FavoriteSelectionContract.FavoriteSelectionEntry.MOVIE_NAME)));
            movie.setPlotSynopsis(cursor.getString(cursor.getColumnIndex(FavoriteSelectionContract.FavoriteSelectionEntry.SYNOPSIS)));
            movie.setUserRating(cursor.getString(cursor.getColumnIndex(FavoriteSelectionContract.FavoriteSelectionEntry.RATING)));
            movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(FavoriteSelectionContract.FavoriteSelectionEntry.RELEASE_DATE)));
            movies[index] = movie;
        }
        mFavoritesAdapter.setFavoriteMovies(movies);
    }
}
