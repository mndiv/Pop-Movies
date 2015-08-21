package com.divya.android.movies.popmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DivyaM on 7/22/2015.
 */
public class Results implements Parcelable{

    @SerializedName("results")
    @Expose
    private List<MovieInfo> results = new ArrayList<MovieInfo>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public List<MovieInfo> getResults(){
        return results;
    }

    public void setResults(List<MovieInfo> results){
        this.results = results;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
    public Results(Parcel in){
        this.results = in.readArrayList(null);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
        public void writeToParcel(Parcel dest, int flags)         {
            dest.writeList(this.results);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public Results createFromParcel(Parcel in){return new Results(in);}

        @Override
        public Results[] newArray(int size) {
            return new Results[size];
        }

    };
}
