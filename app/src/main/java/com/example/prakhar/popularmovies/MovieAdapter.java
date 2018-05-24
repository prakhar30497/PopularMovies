package com.example.prakhar.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    private List<Movie> mMovies;
    final private MovieItemClickHandler mOnClickListener;


    public MovieAdapter(List<Movie> movies, MovieItemClickHandler listener) {
        mMovies = movies;
        mOnClickListener = listener;
    }

    public interface MovieItemClickHandler{
        public void onListItemClick(Movie clickedMovie);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Context context = holder.mView.getContext();
        Movie movie = mMovies.get(position);
        holder.mMovie = movie;
        holder.mTitle.setText(movie.getTitle());

        Picasso.with(context)
                .load(movie.getPoster())
                .into(holder.mPoster);

        holder.mView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mOnClickListener.onListItemClick(holder.mMovie);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final View mView;
        public Movie mMovie;
        public ImageView mPoster;
        public TextView mTitle;

        public MyViewHolder(View view){
            super(view);
            mView = view;
            mPoster = (ImageView) view.findViewById(R.id.iv_poster);
            mTitle = (TextView) view.findViewById(R.id.tv_movie_name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onListItemClick(mMovie);
        }
    }
    public void setMovieData(List<Movie> movies){
        mMovies = movies;
        notifyDataSetChanged();
    }
}
