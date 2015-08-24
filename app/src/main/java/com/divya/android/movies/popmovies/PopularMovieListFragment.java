package com.divya.android.movies.popmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.divya.android.movies.popmovies.api.ApiClient;
import com.divya.android.movies.popmovies.api.GetMovieDataApi;
import com.divya.android.movies.popmovies.data.MovieContract.FavMovieEntry;
import com.divya.android.movies.popmovies.data.MovieContract.MoviePopularityEntry;
import com.divya.android.movies.popmovies.data.MovieContract.MovieVoteAverageEntry;
import com.divya.android.movies.popmovies.model.Results;
import com.divya.android.movies.popmovies.model.UriData;

import java.util.Vector;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class PopularMovieListFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int MOVIE_LOADER = 0;
    private GridView gridView;
    private SharedPreferences sharedPrefs;
    static final String MOVIES_BASE_URL = "http://api.themoviedb.org/3";
    protected final String TAG = getClass().getSimpleName();
    protected GetMovieDataApi service;
    final String SORT_PARAM = "sort_by";
    final String API_KEY = "api_key";
    Results res;
    private MovieAdapter mMovieAdapter;
    private String sortBy = "";
    private Uri mUri;

    public PopularMovieListFragment() {

    }


    void onSettingsChanged( ) {
        sortBy = sharedPrefs.getString(getString(R.string.pref_sortby_key), getString(R.string.pref_sortby_default));
        if(sortBy.equals(getString(R.string.pref_sortby_favorite))){

            mUri = FavMovieEntry.CONTENT_URI;
        }
        else {
            if (sortBy.equals(getString(R.string.pref_sortby_default))) {
                mUri = MoviePopularityEntry.CONTENT_URI;
            } else {
                mUri = MovieVoteAverageEntry.CONTENT_URI;
            }
            updateMovieList();
        }
        Log.d(TAG, "mUri in SettingsChanged function : " + mUri);
        getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
    }


    private void updateMovieList() {

        String api_key = "2fc475941d44b7da433d1f18e24e2551";
        //String api_key = ; /*Please use your own api_key for moviedb*/
        Log.d(TAG, "mUri in SettingsChanged function : " + mUri);

        service.getMovieDataFromApi(sortBy, api_key, new Callback<Results>() {
            @Override
            public void success(Results results, Response response) {
                res = results;
                Vector<ContentValues> cVVector = new Vector<ContentValues>(results.getResults().size());
                // Insert the new weather information into the database
                if (sortBy.equals(getString(R.string.pref_sortby_default))) {
                    Log.d(TAG, "size of movie db = " + results.getResults().size());
                    for (int i = 0; i < results.getResults().size(); i++) {
                        ContentValues movieValues = new ContentValues();

                        movieValues.put(MoviePopularityEntry.COLUMN_MOVIE_BACKDROPPATH, results.getResults().get(i).getBackdropPath());
                        movieValues.put(MoviePopularityEntry.COLUMN_MOVIE_ID, results.getResults().get(i).getId());
                        movieValues.put(MoviePopularityEntry.COLUMN_MOVIE_TITLE, results.getResults().get(i).getOriginalTitle());
                        movieValues.put(MoviePopularityEntry.COLUMN_MOVIE_POSTERPATH, results.getResults().get(i).getPosterPath());
                        movieValues.put(MoviePopularityEntry.COLUMN_MOVIE_OVERVIEW, results.getResults().get(i).getOverview());
                        movieValues.put(MoviePopularityEntry.COLUMN_MOVIE_RELEASEDATE, results.getResults().get(i).getReleaseDate());
                        movieValues.put(MoviePopularityEntry.COLUMN_MOVIE_AVERAGEVOTE, results.getResults().get(i).getVoteAverage());
                        movieValues.put(MoviePopularityEntry.COLUMN_MOVIE_FAV, 0);

                        cVVector.add(movieValues);

                        mUri = MoviePopularityEntry.CONTENT_URI;

                    }

                } else {
                    Log.d(TAG, "size of movie db = " + results.getResults().size());
                    for (int i = 0; i < results.getResults().size(); i++) {
                        ContentValues movieValues = new ContentValues();

                        movieValues.put(MovieVoteAverageEntry.COLUMN_MOVIE_BACKDROPPATH, results.getResults().get(i).getBackdropPath());
                        movieValues.put(MovieVoteAverageEntry.COLUMN_MOVIE_ID, results.getResults().get(i).getId());
                        movieValues.put(MovieVoteAverageEntry.COLUMN_MOVIE_TITLE, results.getResults().get(i).getOriginalTitle());
                        movieValues.put(MovieVoteAverageEntry.COLUMN_MOVIE_POSTERPATH, results.getResults().get(i).getPosterPath());
                        movieValues.put(MovieVoteAverageEntry.COLUMN_MOVIE_OVERVIEW, results.getResults().get(i).getOverview());
                        movieValues.put(MovieVoteAverageEntry.COLUMN_MOVIE_RELEASEDATE, results.getResults().get(i).getReleaseDate());
                        movieValues.put(MovieVoteAverageEntry.COLUMN_MOVIE_AVERAGEVOTE, results.getResults().get(i).getVoteAverage());
                        movieValues.put(MovieVoteAverageEntry.COLUMN_MOVIE_FAV, 0);

                        cVVector.add(movieValues);
                        mUri = MovieVoteAverageEntry.CONTENT_URI;
                    }
                }

                int inserted = 0;
                // add to database
                if (cVVector.size() > 0) {
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    inserted = getActivity().getContentResolver().bulkInsert(mUri, cvArray);
                }
                Log.d(TAG, "PopMovies Complete. " + inserted + " Inserted");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "failure: " + error);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated");

        // initialize loader
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        mMovieAdapter = new MovieAdapter(getActivity(), null, 0, MOVIE_LOADER);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridview);

        if (savedInstanceState != null) {
            res = savedInstanceState.getParcelable("KEY_RESULTS_LIST");
//            imageAdapter = new ImageAdapter(getActivity(), res);
//            gridView.setAdapter(imageAdapter);  // divya
        }


        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sortBy = sharedPrefs.getString(getString(R.string.pref_sortby_key), getString(R.string.pref_sortby_default));
        if(sortBy.equals(getString(R.string.pref_sortby_favorite))){

            mUri = FavMovieEntry.CONTENT_URI;
        }
        else {
            if (sortBy.equals(getString(R.string.pref_sortby_default))) {
                mUri = MoviePopularityEntry.CONTENT_URI;
            } else {
                mUri = MovieVoteAverageEntry.CONTENT_URI;
            }
            service = ApiClient.MovieDataApiInterface();
            updateMovieList();
        }


        gridView.setAdapter(mMovieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // increment the position to match Database Ids indexed starting at 1
               int uriId = position + 1;
                Uri uri;
                // append Id to uri
                uri = ContentUris.withAppendedId(mUri,uriId);

                Intent intent = new Intent(getActivity(), MovieDetail.class);
                UriData obj = new UriData(uriId, uri);
                intent.putExtra("MovieInfo", obj);
                startActivity(intent);

            }
        });
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("KEY_RESULTS_LIST", res);
        super.onSaveInstanceState(outState);
    }

    // Attach loader to our flavors database query
    // run when loader is initialized
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                mUri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        data.moveToFirst();
        DatabaseUtils.dumpCursor(data);
        mMovieAdapter.swapCursor(data);


    }

    // reset CursorAdapter on Loader Reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
    }
}
