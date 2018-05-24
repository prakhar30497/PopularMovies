package com.example.prakhar.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String AUTHORITY = "com.example.prakhar.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public MovieContract(){
    }

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER = "poster_url";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE = "released_date";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_BACKDROP = "backdrop";

        public static final String[] MOVIE_COLUMNS = {
                COLUMN_ID,
                COLUMN_TITLE,
                COLUMN_POSTER,
                COLUMN_OVERVIEW,
                COLUMN_RELEASE,
                COLUMN_RATING,
                COLUMN_BACKDROP
        };
    }
}
