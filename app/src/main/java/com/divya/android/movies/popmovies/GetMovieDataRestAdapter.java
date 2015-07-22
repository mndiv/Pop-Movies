package com.divya.android.movies.popmovies;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by KeerthanaS on 7/20/2015.
 */
public class GetMovieDataRestAdapter {
    protected final String TAG = getClass().getSimpleName();
    protected RestAdapter mRestAdapter;
    protected GetMovieDataApi mApi;
    static final String MOVIES_BASE_URL ="http://api.themoviedb.org/3/discover/movie?";

    public GetMovieDataRestAdapter() {

        Gson gson = new GsonBuilder()
                .create();

        mRestAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(MOVIES_BASE_URL)
                .setConverter(new GsonConverter(gson))
                .build();

        mApi = mRestAdapter.create(GetMovieDataApi.class);
        Log.d(TAG, "GetMovieDataRestAdapter -- created" );
    }

    public void testMovieDataApi(String sortBy, String apiKey, Callback<Results> callback){
        mApi.getMovieDataFromApi(sortBy, apiKey, callback);

    }
}
