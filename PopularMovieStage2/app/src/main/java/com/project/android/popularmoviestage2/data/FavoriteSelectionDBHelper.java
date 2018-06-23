package com.project.android.popularmoviestage2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoriteSelectionDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;

    public FavoriteSelectionDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_FAVORITE_MOVIES_TABLE = "CREATE TABLE " +
        FavoriteSelectionContract.FavoriteSelectionEntry.TABLE_NAME + " (" +
        FavoriteSelectionContract.FavoriteSelectionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        FavoriteSelectionContract.FavoriteSelectionEntry.MOVIE_ID + " INTEGER NOT NULL, " +
        FavoriteSelectionContract.FavoriteSelectionEntry.MOVIE_NAME + " TEXT NOT NULL, " +
        FavoriteSelectionContract.FavoriteSelectionEntry.IS_FAVORITE + " INTEGER NOT NULL, " +
        FavoriteSelectionContract.FavoriteSelectionEntry.SYNOPSIS + " TEXT NOT NULL, " +
        FavoriteSelectionContract.FavoriteSelectionEntry.POSTER_URL + " TEXT NOT NULL, " +
        FavoriteSelectionContract.FavoriteSelectionEntry.RATING + " TEXT NOT NULL, " +
        FavoriteSelectionContract.FavoriteSelectionEntry.RELEASE_DATE + " TEXT NOT NULL " + ");";
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int index, int index1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteSelectionContract.FavoriteSelectionEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
