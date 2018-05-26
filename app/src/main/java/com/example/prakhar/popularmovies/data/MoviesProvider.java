package com.example.prakhar.popularmovies.data;

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

import static com.example.prakhar.popularmovies.data.MovieContract.MovieEntry.COLUMN_ID;
import static com.example.prakhar.popularmovies.data.MovieContract.MovieEntry.TABLE_NAME;

public class MoviesProvider extends ContentProvider{

    private MovieDBHelper dbHelper;

    public static final int MOVIES = 100;
    public static final int MOVIE_DETAILS = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        matcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES + "/#", MOVIE_DETAILS);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MovieDBHelper(getContext());
        Log.d("MoviesProvider","DBHelper used");
        return true;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase mDb = dbHelper.getWritableDatabase();
        Uri returnUri;

        switch (sUriMatcher.match(uri)){
            case MOVIES:{
                long id = mDb.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);

                } else{
                    throw new android.database.SQLException("Failed to insert new row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        Log.d("URI: ", String.valueOf(uri));

        final SQLiteDatabase mDb = dbHelper.getReadableDatabase();

        switch (sUriMatcher.match(uri)){
            case MOVIES: {
                cursor = mDb.query(
                        TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MOVIE_DETAILS: {
                String id = uri.getPathSegments().get(1);

                cursor = mDb.query(
                        TABLE_NAME,
                        projection,
                        COLUMN_ID + " = ?",
                        new String[]{id},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not needed");
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        final SQLiteDatabase mDb = dbHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)){
            case MOVIES: {
                count = mDb.delete(TABLE_NAME, selection, selectionArgs);
                break;
            }
            case MOVIE_DETAILS: {
                count = mDb.delete(TABLE_NAME, COLUMN_ID + " = ?", selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (count != 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }




}
