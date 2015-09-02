package com.divya.android.movies.popmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by KeerthanaS on 9/2/2015.
 */
public class VideoInfo implements Parcelable {
    @SerializedName("key")
    @Expose
    private String key;

    @SerializedName("name")
    @Expose
    private String name;


    @SerializedName("type")
    @Expose
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public VideoInfo(String type, String key, String name) {
        this.type = type;
        this.key = key;
        this.name = name;
    }

    //Parcelling Part
    public VideoInfo(Parcel in) {
        String[] data = new String[3];
        in.readStringArray(data);
        this.key = data[0];
        this.name = data[1];
        this.type = data[2];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.key,
                this.name,
                this.type});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public VideoInfo createFromParcel(Parcel in) {
            return new VideoInfo(in);
        }

        public VideoInfo[] newArray(int size) {
            return new VideoInfo[size];
        }

    };
}
