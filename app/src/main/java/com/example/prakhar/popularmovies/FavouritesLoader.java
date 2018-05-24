package com.example.prakhar.popularmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.prakhar.popularmovies.data.MovieContract;

import java.util.List;

public class FavouritesLoader extends AsyncTaskLoader<List<Movie>>{
    private static final String LOG_TAG = MovieLoader.class.getName();
    private Uri mUrl;

    private ProgressBar mLoadingIndicator;

    public FavouritesLoader(Context context, Uri url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "test: onStartLoading() called.");
        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground(){
        if(mUrl == null)
            return null;

        return (List<Movie>) getContext().getContentResolver().query(mUrl, MovieContract.MovieEntry.MOVIE_COLUMNS, null, null, null);
    }
}
