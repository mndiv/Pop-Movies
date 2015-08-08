package com.divya.android.movies.popmovies.api;

import com.divya.android.movies.popmovies.model.ResultReviews;
import com.divya.android.movies.popmovies.model.ResultVideos;
import com.divya.android.movies.popmovies.model.Results;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by KeerthanaS on 7/20/2015.
 */
public interface GetMovieDataApi {
    @GET("/discover/movie")
    void getMovieDataFromApi(
            @Query("sort_by") String sortBy,
            @Query("api_key") String apiKey,
            Callback<Results> callback);

    @GET("/movie/{id}/videos")
    void getMovieTrailersFromApi( @Path("id") int videoId,
            @Query("api_key") String apiKey,
            Callback<ResultVideos> callback);

    @GET("/movie/{id}/reviews")
    void getMovieReviewsFromApi( @Path("id") int videoId,
                                  @Query("api_key") String apiKey,
                                  Callback<ResultReviews> callback);

}
