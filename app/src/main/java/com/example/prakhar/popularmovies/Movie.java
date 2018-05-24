package com.example.prakhar.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable{
    private int mId;
    private String mTitle;
    private String mPoster;
    private String mOverview;
    private String mReleaseDate;
    private String mUserRating;
    private String mBackdrop;

    Movie(){
    }

    public Movie(int id, String title, String poster, String overview, String releaseDate, String userRating, String backdrop){
        mId = id;
        mTitle = title;
        mPoster = poster;
        mOverview = overview;
        mReleaseDate = releaseDate;
        mUserRating = userRating;
        mBackdrop = backdrop;
    }

    protected Movie(Parcel in) {
        mId = in.readInt();
        mTitle = in.readString();
        mPoster = in.readString();
        mOverview = in.readString();
        mReleaseDate = in.readString();
        mUserRating = in.readString();
        mBackdrop = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            Movie movie = new Movie();
            movie.mId = in.readInt();
            movie.mTitle = in.readString();
            movie.mPoster = in.readString();
            movie.mOverview = in.readString();
            movie.mReleaseDate = in.readString();
            movie.mUserRating = in.readString();
            movie.mBackdrop = in.readString();

            return movie;
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getId() {
        return mId;
    }
    public String getTitle(){
        return mTitle;
    }
    public String getPoster(){
        return mPoster;
    }
    public String getOverview(){
        return mOverview;
    }
    public String getReleaseDate(){
        return mReleaseDate;
    }
    public String getUserRating(){
        return mUserRating;
    }
    public String getBackdrop(){
        return mBackdrop;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mTitle);
        dest.writeString(mPoster);
        dest.writeString(mOverview);
        dest.writeString(mReleaseDate);
        dest.writeString(mUserRating);
        dest.writeString(mBackdrop);
    }
}
