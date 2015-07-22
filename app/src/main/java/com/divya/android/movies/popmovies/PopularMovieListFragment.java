package com.divya.android.movies.popmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    List<MovieInfo> movieDetailsObj;
    private RetainedAppData mRetainedAppData; //abcd
    protected final String TAG = getClass().getSimpleName(); //abcd

    public PopularMovieListFragment() {

    }

    @Override
    public void onStart() {
        super.onStart();
        movieDetailsObj = new ArrayList<MovieInfo>();
        updateMoviesList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mRetainedAppData = new RetainedAppData(); //abcd
        Log.d(TAG, "onCreate(): Creating new  data set");

        //movieDetailsObj = new ArrayList<MovieInfo>();

        //Obtain the gridView ID . where rootView inflates the fragment_main.xml
        gridView = (GridView)rootView.findViewById(R.id.gridview);
        imageAdapter = new ImageAdapter(getActivity());

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieInfo obj = (MovieInfo) imageAdapter.getItem(position);
               Intent intent = new Intent(getActivity(), MovieDetail.class);
                intent.putExtra("MovieInfo", obj);
               startActivity(intent);
            }
        });
        return rootView;
    }

    private void updateMoviesList() {
        //This creates an AsyncTask FetchMovieTask() which runs in other thread than main thread.
        FetchMovieTask movieTask = new FetchMovieTask();
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        movieTask.execute();
}

    public class ImageAdapter extends BaseAdapter{
        private final String LOG_TAG = ImageAdapter.class.getSimpleName();
        private Context mContext;
        //Constructor which takes context as inputs
    public ImageAdapter(Context context) {
        mContext = context;
    }

    @Override
    //return the no. of Views to be displayed
    public int getCount() {
        return movieDetailsObj.size();
    }

    @Override
    public Object getItem(int position) {
        return movieDetailsObj.get(position);
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
            view.setLayoutParams(new GridView.LayoutParams(220, 220));
        }

        //Picasso easily load album art thumbnails into your views ...
        //Picasso will handle loading the images on a background thread, image decompression and caching the images.
        //Fetches Images and load them into Views
        Picasso.with(mContext).load(movieDetailsObj.get(position).getPoster_path()).into(view);
        return view;
    }

    }

    //abcd
    private class RetainedAppData {
        private Results mData;
        private GetMovieDataRestAdapter mGetMovieDataRestAdapter; //RE ST Adapter
        private Callback<Results> mMovieInfoCallback = new Callback<Results>() {

            public void success(Results data, Response response) {


                for (int i = 0; i < data.getMovieInfo().size(); i++) {
                    Log.d(TAG, "Async Success: MovieData: title:" + data.getMovieInfo().get(i).getOriginal_title() +
                                    ", poster_path:" + data.getMovieInfo().get(i).getPoster_path() +
                                    ", overview:" + data.getMovieInfo().get(i).getOverview() +
                                    ", release_data:" + data.getMovieInfo().get(i).getRelease_date() +
                                    ",vote_average:" + data.getMovieInfo().get(i).getVote_average()
                    );

                    mData.mMovieInfo.add(data.mMovieInfo.get(i));
                }
            }
            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "failure: " + error);
            }
        };

        public void runRetrofitTest(final String sortBy, final String api_key){
            if(mGetMovieDataRestAdapter == null){
                mGetMovieDataRestAdapter = new GetMovieDataRestAdapter();
            }
            mGetMovieDataRestAdapter.testMovieDataApi(sortBy, api_key, mMovieInfoCallback);
        }
    }
    public class FetchMovieTask extends AsyncTask<Void, Void, String[]> {


        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();



        private String[] getMovieDataFromJson(String movieJSONString)
                throws JSONException{

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_RESULTS = "results";
            final String OWM_TITLE = "original_title";
            final String OWM_IMAGEPATH= "poster_path";
            final String OWM_OVERVIEW = "overview";
            final String OWM_RELEASEDATE = "release_date";
            final String OWM_USERRATING = "vote_average";


            JSONObject movieJson = new JSONObject(movieJSONString);
            JSONArray resultsArray = movieJson.getJSONArray(OWM_RESULTS);

            int numMovies = resultsArray.length();
            String[] resultImageStrs = new String[numMovies];

            for(int i=0;i<numMovies;i++){

                JSONObject res = resultsArray.getJSONObject(i);

                resultImageStrs[i] = res.getString(OWM_IMAGEPATH);
                movieDetailsObj.add(new MovieInfo((String)res.getString(OWM_TITLE),
                        "http://image.tmdb.org/t/p/w185" + res.getString(OWM_IMAGEPATH),
                                            res.getString(OWM_OVERVIEW),
                                            res.getString(OWM_RELEASEDATE),
                                            res.getDouble(OWM_USERRATING)));
            }

            return resultImageStrs;
        }
        @Override
        protected String[] doInBackground(Void... params) {

            //abcd
           /* HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJSONString = null;

            try{

                final String MOVIES_BASE_URL ="http://api.themoviedb.org/3/discover/movie?";
                final String SORT_PARAM = "sort_by";
                final String API_KEY = "api_key";

                String sortBy = sharedPrefs.getString(getString(R.string.pref_sortby_key), getString(R.string.pref_sortby_default));


                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                                .appendQueryParameter(SORT_PARAM,sortBy)
                                .appendQueryParameter(API_KEY,"2fc475941d44b7da433d1f18e24e2551").build();

                String myUri = builtUri.toString();

               // Log.v(LOG_TAG,"URL :" + myUri);
                URL url = new URL(myUri);

                //Create the request to tmbdp and open the connection
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");;
                urlConnection.connect();

                //Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if(inputStream == null){
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line=reader.readLine())!= null){
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + '\n');
                }
                if(buffer.length() == 0){
                    return null;
                }

                movieJSONString = buffer.toString();
                


            }catch (IOException e){
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                Log.e(LOG_TAG, "Error", e);
                return null;
            }finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error Closing Stream", e);
                    }
                }
            }

            try {
                return getMovieDataFromJson(movieJSONString);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }*/ //abcd

            //abcd

            final String SORT_PARAM = "sort_by";
            final String API_KEY = "api_key";

            String sortBy = sharedPrefs.getString(getString(R.string.pref_sortby_key), getString(R.string.pref_sortby_default));
            String api_key = "2fc475941d44b7da433d1f18e24e2551";

            mRetainedAppData.runRetrofitTest(sortBy,api_key);


            return null;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            imageAdapter.notifyDataSetChanged();
            gridView.setAdapter(imageAdapter);

        }
    }

}
