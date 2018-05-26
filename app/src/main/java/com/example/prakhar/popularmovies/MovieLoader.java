package com.example.prakhar.popularmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;

import java.util.List;

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {
    private static final String LOG_TAG = MovieLoader.class.getName();
    private String mUrl;

    public MovieLoader(Context context, String url) {
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

        return MovieUtils.fetchMovieData(mUrl);
    }
}
