package com.divya.android.movies.popmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.divya.android.movies.popmovies.model.MovieInfo;
import com.squareup.picasso.Picasso;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {

    private String mPosterPath;
    public MovieDetailFragment() {
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

        return rootView;
    }
}
