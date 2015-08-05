package com.divya.android.movies.popmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.divya.android.movies.popmovies.api.ApiClient;
import com.divya.android.movies.popmovies.api.GetMovieDataApi;
import com.divya.android.movies.popmovies.model.MovieInfo;
import com.divya.android.movies.popmovies.model.Results;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class PopularMovieListFragment extends Fragment {

    //gridView holds the id defined in fragment_main.xml
    private GridView gridView;
    ImageAdapter imageAdapter;
    private SharedPreferences sharedPrefs;
    static final String MOVIES_BASE_URL ="http://api.themoviedb.org/3";
    protected final String TAG = getClass().getSimpleName();
    protected GetMovieDataApi service;
    final String SORT_PARAM = "sort_by";
    final String API_KEY = "api_key";
    SharedPreferences.OnSharedPreferenceChangeListener listener;
    Results res;

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

    private void updateMovieList(){
        //Log.v(TAG, "updateMovieList() called");
        String sortBy = sharedPrefs.getString(getString(R.string.pref_sortby_key), getString(R.string.pref_sortby_default));
        String api_key = "2fc475941d44b7da433d1f18e24e2551";
        //String api_key = ; /*Please use your own api_key for moviedb*/


        service.getMovieDataFromApi(sortBy, api_key, new Callback<Results>() {
            @Override
            public void success(Results results, Response response) {
                res = results;
                imageAdapter = new ImageAdapter(getActivity(), results);
                imageAdapter.notifyDataSetChanged();
                gridView.setAdapter(imageAdapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        MovieInfo obj = (MovieInfo) imageAdapter.getItem(position);
                        Log.d(TAG,"id: " +obj.getId());
                        Intent intent = new Intent(getActivity(), MovieDetail.class);
                        intent.putExtra("MovieInfo", obj);
                        startActivity(intent);
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
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView)rootView.findViewById(R.id.gridview);
        if(savedInstanceState != null){
            res = savedInstanceState.getParcelable("KEY_RESULTS_LIST");
            imageAdapter = new ImageAdapter(getActivity(),res);
            gridView.setAdapter(imageAdapter);
        }

       // Log.v(TAG,"onCreateView()");

        //Obtain the gridView ID . where rootView inflates the fragment_main.xml

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        service = ApiClient.MovieDataApiInterface();
        updateMovieList();
        registerPreferenceListener();

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

    public class ImageAdapter extends BaseAdapter{
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
    }
}
