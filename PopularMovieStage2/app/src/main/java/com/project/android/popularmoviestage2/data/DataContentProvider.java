package com.project.android.popularmoviestage2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

//Content Provider with minimum functionality for current app use cases
public class DataContentProvider extends ContentProvider {
    private FavoriteSelectionDBHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new FavoriteSelectionDBHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor;
        cursor = db.query(FavoriteSelectionContract.FavoriteSelectionEntry.TABLE_NAME, projection, selection,
        selectionArgs, null, null, null);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = db.insert(FavoriteSelectionContract.FavoriteSelectionEntry.TABLE_NAME, null, contentValues);
        if (id == -1) {
            Log.e("DataContentProvider IN:", " Failed to insert row for :" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long result = db.update(FavoriteSelectionContract.FavoriteSelectionEntry.TABLE_NAME, contentValues,
        selection,selectionArgs);
        if(result == 0)
            Log.d("DataContentProvider UP:", " No rows affected");
        return 0;
    }
}
