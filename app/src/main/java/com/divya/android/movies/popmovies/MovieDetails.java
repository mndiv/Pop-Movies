package com.divya.android.movies.popmovies;


//New class is defined to hold the Movie Data
public class MovieDetails {

    private String mMovieTitle;

    public String getPosterImage() {
        return mPosterImage;
    }

    public void setPosterImage(String posterImage) {
        mPosterImage = posterImage;
    }

    private String mPosterImage;
    private String mOverview;
    private double mUserRating;
    private String mReleaseDate;




}
