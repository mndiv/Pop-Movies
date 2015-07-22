package com.divya.android.movies.popmovies;

import java.util.List;

/**
 * Created by KeerthanaS on 7/22/2015.
 */
public class Results {
    public List<MovieInfo> mMovieInfo;

    public Results(List<MovieInfo> movieInfo) {
        mMovieInfo = movieInfo;
    }

    public List<MovieInfo> getMovieInfo() {return mMovieInfo;}
}
