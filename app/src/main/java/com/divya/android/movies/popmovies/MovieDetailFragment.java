package com.divya.android.movies.popmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.divya.android.movies.popmovies.api.ApiClient;
import com.divya.android.movies.popmovies.api.GetMovieDataApi;
import com.divya.android.movies.popmovies.model.MovieInfo;
import com.divya.android.movies.popmovies.model.ResultVideos;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {

    static final String MOVIES_BASE_URL ="http://api.themoviedb.org/3";
    protected final String TAG = getClass().getSimpleName();
    protected GetMovieDataApi videoService;
    private String mPosterPath;

    String api_key = "2fc475941d44b7da433d1f18e24e2551";
    //String api_key = ; /*Please use your own api_key for moviedb*/

    public MovieDetailFragment() {
    }

    public void GetTrailers(String id){
        int videoId = Integer.parseInt(id);

        videoService = ApiClient.TrailerDataApiInterface();

        videoService.getMovieTrailersFromApi(videoId,api_key, new Callback<ResultVideos>() {
            @Override
            public void success(final ResultVideos resultVideos, Response response) {
                Log.d(TAG,"No. of Trailers : "+resultVideos.getResults().size());
                TextView textView1 = (TextView) getActivity().findViewById(R.id.trailer1);
                String trailer1 = resultVideos.getResults().get(0).getType();
                textView1.setText(trailer1);
                Log.d(TAG,"Trailer Name : " + trailer1);

                ImageButton b1 = (ImageButton) getActivity().findViewById(R.id.button1);
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String key = resultVideos.getResults().get(0).getKey();
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.youtube.com/watch?v=" + key)));
                    }
                });

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "failure: " + error);
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_details, container, false);

        Intent intent = getActivity().getIntent();

        Bundle data = intent.getExtras();
        MovieInfo obj = data.getParcelable("MovieInfo");

        TextView movieTitle = (TextView)rootView.findViewById(R.id.movieTitle);
        movieTitle.setText(obj.getOriginalTitle());

        ImageView view = (ImageView)rootView.findViewById(R.id.posterPath);
        Picasso.with(getActivity()).load(obj.getPosterPath()).into(view);


        TextView releaseDate = (TextView)rootView.findViewById(R.id.releaseDate);
        String str = obj.getReleaseDate().substring(0,4);
        releaseDate.setText(str);

       // TextView duration = (TextView)rootView.findViewById(R.id.duration);
       // duration.setText(obj.get());

        TextView userRating = (TextView)rootView.findViewById(R.id.userRating);
        userRating.setText(Double.toString(obj.getVoteAverage())+"/10");

        TextView overview = (TextView)rootView.findViewById(R.id.overview);
        overview.setText(obj.getOverview());

        GetTrailers(obj.getId());

        return rootView;
    }
}
