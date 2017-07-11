package com.example.prakhar.popularmovies;

public class Movie {
    private String mTitle;
    private String mPoster;
    private String mOverview;
    private String mReleaseDate;
    private String mUserRating;

    public Movie(String title, String poster, String overview, String releaseDate, String userRating){
        mTitle = title;
        mPoster = poster;
        mOverview = overview;
        mReleaseDate = releaseDate;
        mUserRating = userRating;
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
}
