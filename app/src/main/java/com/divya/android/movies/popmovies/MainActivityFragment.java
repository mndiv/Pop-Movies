package com.divya.android.movies.popmovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private  ImageAdapter mImageAdapter;
    private GridView gridView;

    public MainActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMoviesList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        updateMoviesList();
        gridView = (GridView)rootView.findViewById(R.id.gridview);

        return rootView;
    }

    private void updateMoviesList() {
        FetchMovieTask movieTask = new FetchMovieTask();
        movieTask.execute();
    }

    public class ImageAdapter extends BaseAdapter{
        private final String LOG_TAG = ImageAdapter.class.getSimpleName();
        private Context mContext;
        private String[] resultStrs;

    public ImageAdapter(Context context, String[] strings) {
        mContext = context;
        resultStrs = strings;
    }

    @Override
    public int getCount() {
        return resultStrs.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
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
            }

            String url = "http://image.tmdb.org/t/p/w185" + resultStrs[position];
            Picasso.with(mContext).load(url).into(view);
            return view;
        }
}

    public class FetchMovieTask extends AsyncTask<Void, Void, String[]> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();



        private String[] getMovieDataFromJson(String movieJSONString)
                throws JSONException{

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_RESULTS = "results";
            final String OWM_IMAGEPATH= "poster_path";


            JSONObject movieJson = new JSONObject(movieJSONString);
            JSONArray resultsArray = movieJson.getJSONArray(OWM_RESULTS);

            int numMovies = resultsArray.length();
            String[] resultImageStrs = new String[numMovies];

            for(int i=0;i<numMovies;i++){
                JSONObject res = resultsArray.getJSONObject(i);

                resultImageStrs[i] = res.getString(OWM_IMAGEPATH);
                //Log.v(LOG_TAG,"ImageStrs : " + resultImageStrs[i]);
            }
            return resultImageStrs;
        }
        @Override
        protected String[] doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJSONString = null;

            try{

                final String MOVIES_BASE_URL ="http://api.themoviedb.org/3/discover/movie?";
                final String SORT_PARAM = "sort_by";
                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                                .appendQueryParameter(SORT_PARAM,"popularity.desc")
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
                        Log.e("MainActivityFragment", "Error Closing Stream", e);
                    }
                }
            }

            try {
                return getMovieDataFromJson(movieJSONString);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            gridView.setAdapter(new ImageAdapter(getActivity(), strings));

        }
    }

}
