package com.divya.android.movies.popmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KeerthanaS on 7/22/2015.
 */
public class Results {


    @SerializedName("page")
    @Expose
    private String page;
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

}
