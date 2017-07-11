package com.example.prakhar.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter mAdapter;
    private List<Movie> movieList;

    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessage;

    public static final String POPULAR_MOVIES_BASE_URL = "https://api.themoviedb.org/3/movie/popular?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        movieList = new ArrayList<>();
        mAdapter = new MovieAdapter(movieList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.progressBar);
        mErrorMessage = (TextView) findViewById(R.id.error_message);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Movie movie = movieList.get(position);
                Toast.makeText(getApplicationContext(), movie.getTitle() + "is selected!", Toast.LENGTH_SHORT).show();
            }
        }));
        
        loadMoviesList();
    }

    private void loadMoviesList() {
        showMoviesDataView();
        new FetchMoviesTask().execute();
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

    public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>>{

        private String  createMoviesUri(){
            Uri uri = null;
            uri = Uri.parse(POPULAR_MOVIES_BASE_URL);

            Uri api_uri = null;
            api_uri = uri.buildUpon()
                    .appendQueryParameter("api_key", "de5ea3329b4b66e12ecf3e79737fa82b")
                    .build();



            return api_uri.toString();
        }

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

            List<Movie> movies = MovieUtils.fetchMovieData(createMoviesUri());
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
}
