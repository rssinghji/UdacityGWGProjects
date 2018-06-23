package com.project.android.popularmoviestage2.data;

import android.provider.BaseColumns;

public class FavoriteSelectionContract {

    public static final class FavoriteSelectionEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorite_movies";
        public static final String MOVIE_ID = "movie_id";
        public static final String MOVIE_NAME = "movie_name";
        public static final String IS_FAVORITE = "is_favorite";
        public static final String SYNOPSIS = "synopsis";
        public static final String POSTER_URL = "poster";
        public static final String RATING = "rating";
        public static final String RELEASE_DATE = "release";
    }
}
