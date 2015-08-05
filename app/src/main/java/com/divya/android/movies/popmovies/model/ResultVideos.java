package com.divya.android.movies.popmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KeerthanaS on 8/5/2015.
 */
public class ResultVideos {
    @SerializedName("results")
    @Expose
    private List<VideoInfo> results = new ArrayList<VideoInfo>();

    public List<VideoInfo> getResults() {
        return results;
    }

    public class VideoInfo {
        @SerializedName("key")
        @Expose
        private String key;

        @SerializedName("name")
        @Expose
        private String name;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @SerializedName("type")
        @Expose
        private String type;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
