package com.divya.android.movies.popmovies;


import android.os.Parcel;
import android.os.Parcelable;

//New class is defined to hold the Movie Data
public class MovieDetails implements Parcelable{

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

    //Parcelling Part
    public MovieDetails(Parcel in){
        String[] data = new String[5];
        double rating;

        in.readStringArray(data);
        this.mMovieTitle = data[0];
        this.mPosterImage = data[1];
        this.mOverview = data[2];
        this.mReleaseDate = data[3];
        this.mUserRating = Double.parseDouble(data[4]);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.mMovieTitle,
                                            this.mPosterImage,
                                            this.mOverview,
                                            this.mReleaseDate,
                                            Double.toString(this.mUserRating)});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public MovieDetails createFromParcel(Parcel in){
            return new MovieDetails(in);
        }
        public MovieDetails[] newArray(int size){
            return new MovieDetails[size];
        }
    };
}
