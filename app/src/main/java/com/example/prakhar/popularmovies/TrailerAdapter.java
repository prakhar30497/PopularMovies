package com.example.prakhar.popularmovies;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder>{

    private List<Trailer> mTrailers;
    final private TrailerItemClickHandler mOnClickListener;

    public interface TrailerItemClickHandler{
        public void onTrailerItemClick(Trailer clickedTrailer);
    }

    public TrailerAdapter(List<Trailer> trailers, TrailerItemClickHandler listener) {
        mTrailers = trailers;
        mOnClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Context context = holder.mView.getContext();
        Trailer trailer = mTrailers.get(position);

        holder.mTrailer = trailer;

        String thumbnailUrl = "http://img.youtube.com/vi/" + trailer.getKey() + "/hqdefault.jpg";
        Picasso.with(context)
                .load(thumbnailUrl)
                .into(holder.mThumbnail);

        holder.mView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mOnClickListener.onTrailerItemClick(holder.mTrailer);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public void setTrailersData(List<Trailer> trailers){
        mTrailers = trailers;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public Trailer mTrailer;
        public ImageView mThumbnail;
        public final View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mThumbnail = (ImageView) itemView.findViewById(R.id.trailerImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onTrailerItemClick(mTrailer);
        }
    }

}
