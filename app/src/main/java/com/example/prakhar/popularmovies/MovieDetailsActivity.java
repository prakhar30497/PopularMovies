package com.example.prakhar.popularmovies;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.EXTRA_TEXT;
import static android.content.Intent.EXTRA_TITLE;

public class MovieDetailsActivity extends AppCompatActivity implements TrailerAdapter.TrailerItemClickHandler, LoaderManager.LoaderCallbacks{

    private static final int TRAILER_LOADER_ID = 1;
    private static final int REVIEW_LOADER_ID = 2;

    private Movie mMovie;
    private List<Trailer> trailerList;
    private List<Review> reviewList;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton floatingActionButton;

    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

    private RecyclerView trailer_recyclerView;
    private RecyclerView review_recyclerView;

    private TextView trailer_header;
    private TextView review_header;

    private Toast toast = null;

    public static final String BASE_URL = "https://api.themoviedb.org/3/movie/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent startingActivityIntent = getIntent();
        if(startingActivityIntent != null){
            if(startingActivityIntent.hasExtra(CONSTANTS.EXTRA_MOVIE)){
                mMovie = startingActivityIntent.getParcelableExtra(CONSTANTS.EXTRA_MOVIE);
            }
        }

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(mMovie.getTitle());
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        getWindow().setStatusBarColor(Color.TRANSPARENT);

        final ImageView backdrop = (ImageView) findViewById(R.id.iv_backdrop);
        Picasso.with(getApplicationContext())
                .load(mMovie.getBackdrop()).into(backdrop, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) backdrop.getDrawable()).getBitmap();
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette palette) {
                        applyPalette(palette);
                    }
                });
            }

            @Override
            public void onError() {

            }
        });

        ImageView poster = (ImageView) findViewById(R.id.iv_movie_poster);
        Picasso.with(getApplication())
                .load(mMovie.getPoster()).into(poster);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        
        updateFab();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentResolver contentResolver = getContentResolver();

                MovieDB mDB = new MovieDB();

                String m;
                if (mDB.isMovieFavorite(contentResolver, mMovie.getId())){
                    m = "Removed from Favourites";
                    mDB.removeMovie(contentResolver, mMovie.getId());
                    floatingActionButton.setImageDrawable(getDrawable(R.drawable.fav_add));

                    if(toast!=null){
                        toast.cancel();
                    }
                    toast.makeText(getApplicationContext(), m, Toast.LENGTH_SHORT).show();
                }
                else {
                    m = "Added to Favourites";
                    mDB.addMovie(contentResolver, mMovie);
                    floatingActionButton.setImageDrawable(getDrawable(R.drawable.fav_remove));

                    if(toast!=null){
                        toast.cancel();
                    }
                    toast.makeText(getApplicationContext(), m, Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView title = (TextView) findViewById(R.id.tv_movie_title);
        title.setText(mMovie.getTitle());

        TextView releaseDate = (TextView) findViewById(R.id.tv_movie_release_date);
        releaseDate.setText(mMovie.getReleaseDate());

        TextView userRating = (TextView) findViewById(R.id.tv_movie_user_rating);
        userRating.setText(mMovie.getUserRating());

        TextView overview = (TextView) findViewById(R.id.tv_movie_overview);
        overview.setText(mMovie.getOverview());

        trailerList = new ArrayList<>();
        reviewList = new ArrayList<>();

        trailer_recyclerView = (RecyclerView) findViewById(R.id.rv_trailers);
        review_recyclerView = (RecyclerView) findViewById(R.id.rv_reviews);

        mTrailerAdapter = new TrailerAdapter(trailerList, this);
        mReviewAdapter = new ReviewAdapter(reviewList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        trailer_recyclerView.setLayoutManager(mLayoutManager);
        trailer_recyclerView.setAdapter(mTrailerAdapter);

        review_recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        review_recyclerView.setAdapter(mReviewAdapter);

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(TRAILER_LOADER_ID, null, this);
        loaderManager.initLoader(REVIEW_LOADER_ID, null, this);

        trailer_header = (TextView) findViewById(R.id.tv_trailer_header);
        review_header = (TextView) findViewById(R.id.tv_review_header);

    }

    private void updateFab() {
        MovieDB movieDb = new MovieDB();

        if(movieDb.isMovieFavorite(getContentResolver(), mMovie.getId())){
            floatingActionButton.setImageDrawable(getDrawable(R.drawable.fav_remove));
        } else {
            floatingActionButton.setImageDrawable(getDrawable(R.drawable.fav_add));
        }
    }

    private void applyPalette(Palette palette) {
        int primaryDark = getResources().getColor(R.color.primary_dark);
        int primary = getResources().getColor(R.color.primary);
        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
        collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
        supportStartPostponedEnterTransition();
    }

    @Override
    public void onTrailerItemClick(Trailer clickedTrailer) {
        String url = "https://www.youtube.com/watch?v=".concat(clickedTrailer.getKey());
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public String createTrailerUrl(){
        Uri uri = Uri.parse(BASE_URL + mMovie.getId() + "/videos?");

        Uri api_uri = null;
        api_uri = uri.buildUpon()
                .appendQueryParameter(CONSTANTS.API_KEY, BuildConfig.THE_MOVIE_DB_API_TOKEN)
                .build();

        return api_uri.toString();
}

    public String createReviewUrl(){
        Uri uri = Uri.parse(BASE_URL + mMovie.getId() + "/reviews?");

        Uri api_uri = null;
        api_uri = uri.buildUpon()
                .appendQueryParameter(CONSTANTS.API_KEY, BuildConfig.THE_MOVIE_DB_API_TOKEN)
                .build();

        return api_uri.toString();
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        if(id == TRAILER_LOADER_ID){
            return new TrailerLoader(this, createTrailerUrl());
        }
        else if(id == REVIEW_LOADER_ID){
            return new ReviewLoader(this, createReviewUrl());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        int id = loader.getId();

        if(id == TRAILER_LOADER_ID){
            if(data != null){
                trailer_recyclerView.setVisibility(View.VISIBLE);
                mTrailerAdapter.setTrailersData((List<Trailer>) data);
            }
            else {
                trailer_header.setVisibility(View.GONE);
                trailer_recyclerView.setVisibility(View.GONE);
            }
        }
        else if(id == REVIEW_LOADER_ID){
            if(data != null){
                review_recyclerView.setVisibility(View.VISIBLE);
                mReviewAdapter.setReviewsData((List<Review>) data);
            }
            else {
                review_header.setVisibility(View.INVISIBLE);
                review_recyclerView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

}
