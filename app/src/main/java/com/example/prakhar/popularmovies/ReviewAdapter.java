package com.example.prakhar.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<Review> mReviews;

    public ReviewAdapter(List<Review> reviews){
        mReviews = reviews;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item, parent, false);
        return new ReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Context context = holder.mView.getContext();
        Review review = mReviews.get(position);

        holder.mReview = review;

        holder.mReviewAuthor.setText(review.getAuthor());
        holder.mReviewContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public void setReviewsData(List<Review> reviews){
        mReviews = reviews;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public Review mReview;
        public final View mView;
        public TextView mReviewAuthor;
        public TextView mReviewContent;
        public TextView mReviewExpand;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mReviewAuthor = (TextView) itemView.findViewById(R.id.tv_review_author);
            mReviewContent = (TextView) itemView.findViewById(R.id.tv_review_content);
            mReviewExpand = (TextView) itemView.findViewById(R.id.tv_review_expand);

            mReviewExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mReviewExpand.getText().toString().equals("Show More")){
                        mReviewExpand.setText("Show Less");
                        mReviewContent.setMaxLines(100000);
                    }
                    else if(mReviewExpand.getText().toString().equals("Show Less")){
                        mReviewExpand.setText("Show More");
                        mReviewContent.setMaxLines(3);
                    }
                }
            });
        }
    }
}
