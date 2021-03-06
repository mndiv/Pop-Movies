package com.divya.android.movies.popmovies.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

//New class is defined to hold the Movie Data
public class MovieInfo implements Parcelable{

    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("original_title")
    @Expose
    private String originalTitle;

    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @SerializedName("vote_average")
    @Expose
    private double voteAverage;

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getBackdropPath() { return ("http://image.tmdb.org/t/p/w500" + backdropPath);  }
    public void setBackdropPath(String backdropPath) {  this.backdropPath = backdropPath; }
    public String getId() { return id;  }
    public void setId(String id) {  this.id = id; }
    public String getOriginalTitle() {
        return originalTitle;
    }
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }
    public String getOverview() {
        return overview;
    }
    public void setOverview(String overview) {
        this.overview = overview;
    }
    public String getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    public String getPosterPath() {
        return ("http://image.tmdb.org/t/p/w185" +posterPath);
    }
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
    public double getVoteAverage() {
        return voteAverage;
    }
    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }


    public MovieInfo(String movieTitle, String posterImg, String overView,
                     String releaseDt, double userRating,
                     String movieId,String backdropImg) {
        originalTitle = movieTitle;
        posterPath = posterImg;
        overview = overView;
        voteAverage = userRating;
        releaseDate = releaseDt;
        id = movieId;
        backdropPath = backdropImg;

    }

    //Parcelling Part
    public MovieInfo(Parcel in){
        String[] data = new String[7];

        in.readStringArray(data);
        this.backdropPath = data[0];
        this.id = data[1];
        this.originalTitle = data[2];
        this.posterPath = data[3];
        this.overview = data[4];
        this.releaseDate = data[5];
        this.voteAverage = Double.parseDouble(data[6]);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.backdropPath,
                                            this.id,
                                            this.originalTitle,
                                            this.posterPath,
                                            this.overview,
                                            this.releaseDate,
                                            Double.toString(this.voteAverage)});
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
