package com.example.prakhar.popularmovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.prakhar.popularmovies.data.MovieContract;
import com.example.prakhar.popularmovies.data.MovieContract.MovieEntry;

import java.util.ArrayList;
import java.util.List;

public class MovieDB {

    public boolean isMovieFavorite(ContentResolver contentResolver, int id){
        boolean r = false;
        Cursor cursor = contentResolver.query(Uri.parse(MovieEntry.CONTENT_URI + "/" + id), null, null, null, null, null);
        if (cursor != null && cursor.moveToNext()){
            r = true;
            cursor.close();
        }
        return r;
    }

    public void addMovie(ContentResolver contentResolver, Movie movie){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieEntry.COLUMN_ID, movie.getId());
        contentValues.put(MovieEntry.COLUMN_TITLE, movie.getTitle());
        contentValues.put(MovieEntry.COLUMN_POSTER, movie.getPoster());
        contentValues.put(MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        contentValues.put(MovieEntry.COLUMN_RELEASE, movie.getReleaseDate());
        contentValues.put(MovieEntry.COLUMN_RATING, movie.getUserRating());
        contentValues.put(MovieEntry.COLUMN_BACKDROP, movie.getBackdrop());

        contentResolver.insert(MovieEntry.CONTENT_URI, contentValues);
    }

    public void removeMovie(ContentResolver contentResolver, int id){
        Uri uri = Uri.parse(MovieEntry.CONTENT_URI + "/" + id);
        contentResolver.delete(uri, null, new String[]{id + ""});
    }

    public List<Movie> getFavoriteMovies(ContentResolver contentResolver){

        Cursor cursor = contentResolver.query(MovieEntry.CONTENT_URI, null, null, null, null, null);
        ArrayList <Movie> movies = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()){
            do {
                int id = cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_ID));
                String display_name = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_TITLE));
                String poster_url = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_POSTER));
                String overview = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_OVERVIEW));
                String released_date = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE));
                String rating = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_RATING));
                String backdrop = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_BACKDROP));
                movies.add(new Movie(id, display_name, poster_url, overview, released_date, rating, backdrop));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return movies;
    }
}
