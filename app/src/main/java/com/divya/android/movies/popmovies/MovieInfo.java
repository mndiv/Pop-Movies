package com.divya.android.movies.popmovies;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

//New class is defined to hold the Movie Data
public class MovieInfo implements Parcelable{

    @SerializedName("original_title")
    private String original_title;

    @SerializedName("overview")
    private String overview;

    @SerializedName("release_date")
    private String release_date;

    @SerializedName("poster_path")
    private String poster_path;

    @SerializedName("vote_average")
    private double vote_average;

    public String getOriginal_title() {
        return original_title;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public double getVote_average() {
        return vote_average;
    }



    public String getPoster_path() {
        return ("http://image.tmdb.org/t/p/w185" + poster_path);
    }



    public MovieInfo(String movieTitle, String posterImage, String overView, String releaseDate, double userRating) {
        original_title = movieTitle;
        poster_path = posterImage;
        overview = overView;
        vote_average = userRating;
        release_date = releaseDate;
    }

    //Parcelling Part
    public MovieInfo(Parcel in){
        String[] data = new String[5];
        double rating;

        in.readStringArray(data);
        this.original_title = data[0];
        this.poster_path = data[1];
        this.overview = data[2];
        this.release_date = data[3];
        this.vote_average = Double.parseDouble(data[4]);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.original_title,
                                            this.poster_path,
                                            this.overview,
                                            this.release_date,
                                            Double.toString(this.vote_average)});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public MovieInfo createFromParcel(Parcel in){
            return new MovieInfo(in);
        }
        public MovieInfo[] newArray(int size){
            return new MovieInfo[size];
        }
    };
}
