package com.example.prakhar.popularmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class TrailerLoader extends AsyncTaskLoader<List<Trailer>>{
    private static final String LOG_TAG = TrailerLoader.class.getName();
    private String mUrl;

    public TrailerLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "test: onStartLoading() called.");
        forceLoad();
    }

    @Override
    public List<Trailer> loadInBackground() {
        Log.i(LOG_TAG, "test: loadInBackground() called.");
        if(mUrl == null)
            return null;

        return TrailerUtils.fetchTrailerData(mUrl);
    }
}
