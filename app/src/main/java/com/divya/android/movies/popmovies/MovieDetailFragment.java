package com.divya.android.movies.popmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

        /*mPosterPath = intent.getStringExtra(Intent.EXTRA_TEXT);

        if ((intent != null) && intent.hasExtra(intent.EXTRA_TEXT)) {
           // ((TextView) rootView.findViewById(R.id.posterPath)).setText(mPosterPath);
            ImageView view = (ImageView)rootView.findViewById(R.id.posterPath);
            Picasso.with(getActivity()).load(mPosterPath).into(view);
        }*/

        Bundle data = intent.getExtras();
        MovieInfo obj = data.getParcelable("MovieInfo");

        TextView movieTitle = (TextView)rootView.findViewById(R.id.movieTitle);
        movieTitle.setText(obj.getMovieTitle());

        ImageView view = (ImageView)rootView.findViewById(R.id.posterPath);
        Picasso.with(getActivity()).load(obj.getPosterImage()).into(view);


        TextView releaseDate = (TextView)rootView.findViewById(R.id.releaseDate);
        String str = obj.getReleaseDate().substring(0,4);
        releaseDate.setText(str);

       // TextView duration = (TextView)rootView.findViewById(R.id.duration);
       // duration.setText(obj.get());

        TextView userRating = (TextView)rootView.findViewById(R.id.userRating);
        userRating.setText(Double.toString(obj.getUserRating())+"/10");

       // TextView overview = (TextView)rootView.findViewById(R.id.overview);
       // userRating.setText(obj.getOverview());

        return rootView;
    }
}
