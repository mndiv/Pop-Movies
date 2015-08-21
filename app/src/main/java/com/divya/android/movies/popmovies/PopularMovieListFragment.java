package com.divya.android.movies.popmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
    //gridView holds the id defined in fragment_main.xml
    private GridView gridView;
    //ImageAdapter imageAdapter;
    private SharedPreferences sharedPrefs;
    static final String MOVIES_BASE_URL = "http://api.themoviedb.org/3";
    protected final String TAG = getClass().getSimpleName();
    protected GetMovieDataApi service;
    final String SORT_PARAM = "sort_by";
    final String API_KEY = "api_key";
    SharedPreferences.OnSharedPreferenceChangeListener listener;
    Results res;
    private MovieAdapter mMovieAdapter;
    private String sortBy = "";
    private Uri mUri;
    private String mUriId;

    public PopularMovieListFragment() {

    }


    private void registerPreferenceListener() {
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                //String sortBy = sharedPrefs.getString(key,getString(R.string.pref_sortby_default));
                //Log.v(TAG,"Preference change for: ");
                updateMovieList();

            }
        };
        sharedPrefs.registerOnSharedPreferenceChangeListener(listener);

    }

    /*
        long addMovie(String backdropPath, String id, String originalTitle,
                      String posterPath, String overview, String releaseDate,
                      double voteAverage) {
            long movieLocationId;

            // First, check if the location with this city name exists in the db
            Cursor movieCursor = getActivity().getContentResolver().query(
                    MovieContract.MovieEntry.CONTENT_URI,
                    new String[]{MovieContract.MovieEntry._ID},
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[]{id},
                    null);

            if (movieCursor.moveToFirst()) {
                int movieIdIndex = movieCursor.getColumnIndex(MovieContract.MovieEntry._ID);
                movieLocationId = movieCursor.getLong(movieIdIndex);
            } else {
                // Now that the content provider is set up, inserting rows of data is pretty simple.
                // First create a ContentValues object to hold the data you want to insert.
                ContentValues movieValues = new ContentValues();

                // Then add the data, along with the corresponding name of the data type,
                // so the content provider knows what kind of value is being inserted.
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROPPATH, backdropPath);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, id);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, originalTitle);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTERPATH, posterPath);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, overview);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASEDATE, releaseDate);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_AVERAGEVOTE, voteAverage);


                // Finally, insert location data into the database.
                Uri insertedUri =  getActivity().getContentResolver().insert(
                        MovieContract.MovieEntry.CONTENT_URI,
                        movieValues
                );

                // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
                movieLocationId = ContentUris.parseId(insertedUri);
            }

            movieCursor.close();
            return movieLocationId;
        }
        */
    private void updateMovieList() {
        //Log.v(TAG, "updateMovieList() called");

        String api_key = "2fc475941d44b7da433d1f18e24e2551";
        //String api_key = ; /*Please use your own api_key for moviedb*/


        service.getMovieDataFromApi(sortBy, api_key, new Callback<Results>() {
            @Override
            public void success(Results results, Response response) {
                //res = results;
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

                        cVVector.add(movieValues);
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

                        cVVector.add(movieValues);
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
               /* imageAdapter = new ImageAdapter(getActivity(), results);
                imageAdapter.notifyDataSetChanged();
                gridView.setAdapter(imageAdapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        MovieInfo obj = (MovieInfo) imageAdapter.getItem(position);
                        Log.d(TAG,"id: " +obj.getBackdropPath());
                        Intent intent = new Intent(getActivity(), MovieDetail.class);
                        intent.putExtra("MovieInfo", obj);
                        startActivity(intent);
                    }
                });*/
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "failure: " + error);
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated");


        Cursor c =
                getActivity().getContentResolver().query(mUri,
                        new String[]{mUriId},
                        null,
                        null,
                        null);


        if (c.getCount() == 0) {
            updateMovieList();
        }

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
        gridView.setAdapter(mMovieAdapter);

        if (savedInstanceState != null) {
            res = savedInstanceState.getParcelable("KEY_RESULTS_LIST");
//            imageAdapter = new ImageAdapter(getActivity(), res);
//            gridView.setAdapter(imageAdapter);  // divya
        }

        // Log.v(TAG,"onCreateView()");

        //Obtain the gridView ID . where rootView inflates the fragment_main.xml

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sortBy = sharedPrefs.getString(getString(R.string.pref_sortby_key), getString(R.string.pref_sortby_default));
        if (sortBy.equals(getString(R.string.pref_sortby_default))) {
            mUri = MoviePopularityEntry.CONTENT_URI;
            mUriId = MoviePopularityEntry._ID;
        } else {
            mUri = MovieVoteAverageEntry.CONTENT_URI;
            mUriId = MovieVoteAverageEntry._ID;
        }
        service = ApiClient.MovieDataApiInterface();
        updateMovieList();
        gridView.setAdapter(mMovieAdapter);
        registerPreferenceListener();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // increment the position to match Database Ids indexed starting at 1
                int uriId = position + 1;
                Uri uri;

                // append Id to uri
                uri = ContentUris.withAppendedId(mUri,
                        uriId);


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

    @Override
    public void onDestroy() {
        //Log.v(TAG,"onDestroy() is called");
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(listener);
        super.onDestroy();
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

        mMovieAdapter.swapCursor(data);


    }

    // reset CursorAdapter on Loader Reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
    }


    /*
    public class ImageAdapter extends BaseAdapter {
        private final String LOG_TAG = ImageAdapter.class.getSimpleName();
        private Context mContext;
        private Results mResults;

        //Constructor which takes context as inputs
        public ImageAdapter(Context context, Results results) {
            mContext = context;
            mResults = results;
        }

        @Override
        //return the no. of Views to be displayed
        public int getCount() {
            return mResults.getResults().size();
        }

        @Override
        public Object getItem(int position) {
            return mResults.getResults().get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView view = (ImageView) convertView;
            if (view == null) {
                view = new ImageView(mContext);
                view.setLayoutParams(new GridView.LayoutParams(300, 300));
            }

            //Picasso easily load album art thumbnails into your views ...
            //Picasso will handle loading the images on a background thread, image decompression and caching the images.
            //Fetches Images and load them into Views
            Picasso.with(mContext).load(mResults.getResults().get(position).getPosterPath()).into(view);
            return view;
        }
    }*/
}
