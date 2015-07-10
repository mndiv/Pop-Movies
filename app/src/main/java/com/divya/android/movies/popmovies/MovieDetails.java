package com.divya.android.movies.popmovies;


//New class is defined to hold the Movie Data
public class MovieDetails {

    private String mMovieTitle;

    public String getPosterImage() {
        return mPosterImage;
    }

    private String mPosterImage;
    private String mOverview;
    private String mReleaseDate;
    private double mUserRating;

    public MovieDetails(String movieTitle, String posterImage, String overview, String releaseDate, double userRating) {
        mMovieTitle = movieTitle;
        mPosterImage = posterImage;
        mOverview = overview;
        mUserRating = userRating;
        mReleaseDate = releaseDate;
    }
}
