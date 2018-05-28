package com.example.prakhar.popularmovies;

import android.app.ActivityOptions;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prakhar.popularmovies.data.MovieContract;
import com.example.prakhar.popularmovies.data.MovieDBHelper;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieItemClickHandler, LoaderManager.LoaderCallbacks<List<Movie>>{

    private static final int MOVIE_LOADER_ID = 1;
    private static final int FAVOURITES_LOADER_ID = 2;

    private static final String CALLBACK = "callback";

    private RecyclerView recyclerView;
    private MovieAdapter mAdapter;
    private List<Movie> movieList;

    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessage;

    public String MoviesUrl = null;

    public static final String POPULAR_MOVIES_BASE_URL = "https://api.themoviedb.org/3/movie/popular?";
    public static final String TOP_RATED_MOVIES_BASE_URL = "https://api.themoviedb.org/3/movie/top_rated?";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private String mSortBy = CONSTANTS.MOST_POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null){
            mSortBy = savedInstanceState.getString(CALLBACK);
            Log.d("SORT_BY", mSortBy);
        }


        recyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        movieList = new ArrayList<>();
        mAdapter = new MovieAdapter(movieList, this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.progressBar);
        mErrorMessage = (TextView) findViewById(R.id.error_message);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected()){
            loadMoviesList();
        }
        else{
            mLoadingIndicator.setVisibility(View.GONE);
            mErrorMessage.setText("No Internet Connection");
        }

    }

    @Override
    public void onListItemClick(Movie movieItem){
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(CONSTANTS.EXTRA_MOVIE, movieItem);
        startActivity(intent);

    }

    private void loadMoviesList() {
        showMoviesDataView();
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
    }

    private void showMoviesDataView() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }
    private void showErrorMessage() {
        recyclerView.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.VISIBLE);
        mErrorMessage.setText("Error!");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CALLBACK, mSortBy);
    }

    private String createMoviesUri(){
        Uri uri = null;
        if(mSortBy.equals(CONSTANTS.MOST_POPULAR)){
            uri = Uri.parse(POPULAR_MOVIES_BASE_URL);
        }
        else if(mSortBy.equals(CONSTANTS.TOP_RATED)){
            uri = Uri.parse(TOP_RATED_MOVIES_BASE_URL);
        }
        else{
            uri = Uri.parse(POPULAR_MOVIES_BASE_URL);
        }
        Uri api_uri = null;
        api_uri = uri.buildUpon()
                .appendQueryParameter(CONSTANTS.API_KEY, BuildConfig.THE_MOVIE_DB_API_TOKEN)
                .build();

        MoviesUrl = api_uri.toString();

        return MoviesUrl;
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        if(id == MOVIE_LOADER_ID){
            return new MovieLoader(this, createMoviesUri());
        }
        else if(id == FAVOURITES_LOADER_ID){
            return new FavouritesLoader(this, MovieContract.MovieEntry.CONTENT_URI);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        mLoadingIndicator.setVisibility(View.GONE);
        if(data != null){
            showMoviesDataView();
            mAdapter.setMovieData(data);
        }
        else{
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sort_by_most_popular:
                mSortBy = CONSTANTS.MOST_POPULAR;
                getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
                break;

            case R.id.sort_by_top_rated:
                mSortBy = CONSTANTS.TOP_RATED;
                getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
                break;

            case R.id.sort_by_favourites:
                mSortBy = CONSTANTS.FAVOURITE;
                getLoaderManager().restartLoader(FAVOURITES_LOADER_ID, null, this);
                break;


            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
