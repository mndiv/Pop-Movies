package com.divya.android.movies.popmovies.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DivyaM on 8/20/2015.
 */
public class UriData implements Parcelable {
    int uriId;
    Uri mUri;

    public UriData(int uriId, Uri uri) {
        this.uriId = uriId;
        mUri = uri;
    }

    public int getUriId() { return uriId; }

    public void setUriId(int uriId) {  this.uriId = uriId;  }

    public Uri getUri() {  return mUri;  }

    public void setUri(Uri uri) {   mUri = uri;  }

    //Parcelling Part
    public UriData(Parcel in){
        String[] data = new String[2];

        in.readStringArray(data);
        uriId = Integer.parseInt(data[0]);
        mUri = Uri.parse(data[1]);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{Integer.toString(uriId),
                mUri.toString()});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public UriData createFromParcel(Parcel in){
            return new UriData(in);
        }
        public UriData[] newArray(int size){
            return new UriData[size];
        }
    };
}