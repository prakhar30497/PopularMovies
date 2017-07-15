package com.example.prakhar.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static android.content.Intent.EXTRA_TEXT;
import static android.content.Intent.EXTRA_TITLE;

public class MovieDetailsActivity extends AppCompatActivity {

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        Intent startingActivityIntent = getIntent();
        if(startingActivityIntent != null){
            if(startingActivityIntent.hasExtra(CONSTANTS.EXTRA_MOVIE)){
                mMovie = startingActivityIntent.getParcelableExtra(CONSTANTS.EXTRA_MOVIE);
            }
        }

        ImageView backdrop = (ImageView) findViewById(R.id.iv_backdrop);
        Picasso.with(getApplicationContext())
                .load(mMovie.getBackdrop()).into(backdrop);

        ImageView poster = (ImageView) findViewById(R.id.iv_movie_poster);
        Picasso.with(getApplication())
                .load(mMovie.getPoster()).into(poster);

        TextView title = (TextView) findViewById(R.id.tv_movie_title);
        title.setText(mMovie.getTitle());

        TextView releaseDate = (TextView) findViewById(R.id.tv_movie_release_date);
        releaseDate.setText(mMovie.getReleaseDate());

        TextView userRating = (TextView) findViewById(R.id.tv_movie_user_rating);
        userRating.setText(mMovie.getUserRating()+" / 10");

        TextView overview = (TextView) findViewById(R.id.tv_movie_overview);
        overview.setText(mMovie.getOverview());

    }
}
