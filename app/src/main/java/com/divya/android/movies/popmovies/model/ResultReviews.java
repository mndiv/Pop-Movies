package com.divya.android.movies.popmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DivyaM on 8/8/2015.
 */
public class ResultReviews {

    @SerializedName("results")
    @Expose
    private List<ReviewInfo> results = new ArrayList<ReviewInfo>();

    public ResultReviews(List<ReviewInfo> results) {
        this.results = results;
    }

    public List<ReviewInfo> getResults() { return results;  }

    public void setResults(List<ReviewInfo> results) {  this.results = results;   }

    public class ReviewInfo{
        @SerializedName("author")
        @Expose
        private String author;

        @SerializedName("content")
        @Expose
        private String content;

        public String getAuthor() {  return author;  }
        public void setAuthor(String author) { this.author = author; }
        public String getContent() {  return content;  }
        public void setContent(String content) {  this.content = content; }


    }
}
