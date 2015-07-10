package com.divya.android.movies.popmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    private String mPosterPath;
    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_details, container, false);

        Intent intent = getActivity().getIntent();
        mPosterPath = intent.getStringExtra(Intent.EXTRA_TEXT);

        if ((intent != null) && intent.hasExtra(intent.EXTRA_TEXT))
            ((TextView) rootView.findViewById(R.id.posterPath)).setText(mPosterPath);
        return rootView;
    }
}
