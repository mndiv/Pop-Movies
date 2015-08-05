package com.divya.android.movies.popmovies.api;

import retrofit.RestAdapter;

/**
 * Created by KeerthanaS on 7/26/2015.
 */
public class ApiClient {
    static final String MOVIES_BASE_URL ="http://api.themoviedb.org/3";
   // private static final String VIDEOS_BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static GetMovieDataApi service;

    public static GetMovieDataApi MovieDataApiInterface(){
        if(service == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(MOVIES_BASE_URL)
                    .build();
            service = restAdapter.create(GetMovieDataApi.class);
        }
        return service;
    }

    public  static GetMovieDataApi TrailerDataApiInterface(){
        if(service == null){
            RestAdapter restAdapter= new RestAdapter.Builder()
                    .setEndpoint(MOVIES_BASE_URL)
                    .build();
            service = restAdapter.create(GetMovieDataApi.class);
        }
        return service;
    }
}
