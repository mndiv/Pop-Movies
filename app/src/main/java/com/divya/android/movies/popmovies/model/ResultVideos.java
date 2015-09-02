package com.divya.android.movies.popmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DivyaM on 8/5/2015.
 */
public class ResultVideos implements Parcelable {
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
    public ResultVideos(Parcel in){
        this.results = in.readArrayList(null);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.results);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ResultVideos createFromParcel(Parcel in) {
            return new ResultVideos(in);
        }

        @Override
        public ResultVideos[] newArray(int size) {
            return new ResultVideos[size];
        }

    };
}
