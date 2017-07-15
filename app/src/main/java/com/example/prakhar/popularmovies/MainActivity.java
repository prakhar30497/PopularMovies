package com.example.prakhar.popularmovies;

import android.app.ActivityOptions;
import android.content.Intent;
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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieItemClickHandler{

    private RecyclerView recyclerView;
    private MovieAdapter mAdapter;
    private List<Movie> movieList;

    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessage;

    public static final String POPULAR_MOVIES_BASE_URL = "https://api.themoviedb.org/3/movie/popular?";
    public static final String TOP_RATED_MOVIES_BASE_URL = "https://api.themoviedb.org/3/movie/top_rated?";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private String mSortBy = CONSTANTS.MOST_POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        movieList = new ArrayList<>();
        mAdapter = new MovieAdapter(movieList, this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.progressBar);
        mErrorMessage = (TextView) findViewById(R.id.error_message);

        loadMoviesList();
    }

    @Override
    public void onListItemClick(Movie movieItem){
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(CONSTANTS.EXTRA_MOVIE, movieItem);
        startActivity(intent);

    }

    private void loadMoviesList() {
        showMoviesDataView();
        new FetchMoviesTask().execute(createMoviesUri());
    }

    private void showMoviesDataView() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }
    private void showErrorMessage() {
        recyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setText("Error!");
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    private String  createMoviesUri(){
        Uri uri = null;
        if(mSortBy==CONSTANTS.MOST_POPULAR){
            uri = Uri.parse(POPULAR_MOVIES_BASE_URL);
        }
        else if(mSortBy==CONSTANTS.TOP_RATED){
            uri = Uri.parse(TOP_RATED_MOVIES_BASE_URL);
        }
        else{
            uri = Uri.parse(POPULAR_MOVIES_BASE_URL);
        }
        Uri api_uri = null;
        api_uri = uri.buildUpon()
                .appendQueryParameter("api_key", "de5ea3329b4b66e12ecf3e79737fa82b")
                .build();

        return api_uri.toString();
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            else{
                Log.d(LOG_TAG, "Movie list returned");
            }
            List<Movie> movies = MovieUtils.fetchMovieData(params[0]);
            return movies;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if(movies != null){
                showMoviesDataView();
                mAdapter.setMovieData(movies);
            }
            else{
                showErrorMessage();
            }
        }
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
                mSortBy=CONSTANTS.MOST_POPULAR;
                loadMoviesList();
                break;

            case R.id.sort_by_top_rated:
                mSortBy=CONSTANTS.TOP_RATED;
                loadMoviesList();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
