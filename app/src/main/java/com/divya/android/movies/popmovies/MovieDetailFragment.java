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
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.divya.android.movies.popmovies.api.ApiClient;
import com.divya.android.movies.popmovies.api.GetMovieDataApi;
import com.divya.android.movies.popmovies.data.MovieContract;
import com.divya.android.movies.popmovies.model.ResultReviews;
import com.divya.android.movies.popmovies.model.ResultVideos;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    static final String MOVIES_BASE_URL = "http://api.themoviedb.org/3";
    protected final String TAG = getClass().getSimpleName();
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    CoordinatorLayout rootLayout;
    FloatingActionButton fabBtn;
    private Uri mUri;
    private ImageView backdropView;
    private ImageView posterView;
    private TextView releaseDate;
    private TextView userRating;
    private TextView overview;
    RecyclerView recList;
    TextView emptyView;
    TextView headReviews;
    private int colIdx;
    View div1;
    View div2;
    private int favValue;
    private static final int DETAIL_LOADER = 0;
    Intent intent;
    String sortBy;
    Cursor movieCursor;
    private ShareActionProvider mShareActionProvider;
    private String mMovieTitle;
    private static final String FORECAST_SHARE_HASHTAG = " #PopularMoveApp";
    private String review = "";


    private String mPosterPath;
    TextView reviews;

    String api_key = "2fc475941d44b7da433d1f18e24e2551";
    //String api_key = ; /*Please use your own api_key for moviedb*/
    private Cursor movieDetailCursor;
    private Cursor mDetailCursor;
    static final String DETAIL_URI = "URI";
    private SharedPreferences sharedPrefs;
    private ResultVideos videoTrailers;
    View rootView;
    private TextView headTrailers;

    public MovieDetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detailfragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (mMovieTitle != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mMovieTitle + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }


    @Override
    public void onResume() {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onResume();
    }


    public class RVAdapter extends RecyclerView.Adapter<RVAdapter.TrailerViewHolder> {
        ResultVideos trailers;

        public RVAdapter(ResultVideos trailers) {
            this.trailers = trailers;
        }


        public class TrailerViewHolder extends RecyclerView.ViewHolder {
            ImageView trailerKey;
            TextView trailerName;


            public TrailerViewHolder(View itemView) {
                super(itemView);

                trailerKey = (ImageView) itemView.findViewById(R.id.trailer_key);
                trailerName = (TextView) itemView.findViewById(R.id.info_text);
            }
        }

        @Override
        public TrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trailerview_layout, viewGroup, false);
            TrailerViewHolder pvh = new TrailerViewHolder(v);
            return pvh;
        }


        @Override
        public void onBindViewHolder(TrailerViewHolder trailerViewHolder, int i) {
            final String key = trailers.getResults().get(i).getKey();
            String path = "http://img.youtube.com/vi/" + key + "/mqdefault.jpg";
            Picasso.with(getActivity()).load(path).into(trailerViewHolder.trailerKey);

            trailerViewHolder.trailerName.setText(trailers.getResults().get(i).getName());
            trailerViewHolder.trailerKey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + key)));
                }
            });
        }

        @Override
        public int getItemCount() {
            return trailers.getResults().size();
        }

        @Override

        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
    }

    public void GetTrailers(String id) {
        GetMovieDataApi videoService;
        int videoId = Integer.parseInt(id);

        videoService = ApiClient.TrailerDataApiInterface();
        videoService.getMovieTrailersFromApi(videoId, api_key, new Callback<ResultVideos>() {
            @Override
            public void success(final ResultVideos resultVideos, Response response) {
                Log.d(TAG, "No. of Trailers : " + resultVideos.getResults().size());
                if (resultVideos.getResults().size() == 0) {
                    recList.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    recList.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
                videoTrailers = resultVideos;
                RVAdapter adapter = new RVAdapter(resultVideos);
                recList.setAdapter(adapter);
                Log.d(TAG, "recyclerview adapter is called");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "failure: " + error);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("Trailers", videoTrailers);
        outState.putString("Reviews", review);
        super.onSaveInstanceState(outState);
    }

    public void GetReviews(final String id) {
        GetMovieDataApi reviewService;
        int videoId = Integer.parseInt(id);

        reviewService = ApiClient.ReviewDataApiInterface();
        reviewService.getMovieReviewsFromApi(videoId, api_key, new Callback<ResultReviews>() {
            @Override
            public void success(ResultReviews resultReviews, Response response) {
                review = "";
                for (int i = 0; i < resultReviews.getResults().size(); i++) {

                    Log.d(TAG, "author:" + resultReviews.getResults().get(i).getAuthor());
                    Log.d(TAG, "Review:" + resultReviews.getResults().get(i).getContent());

                    review += resultReviews.getResults().get(i).getAuthor() + "\n\n" +
                            resultReviews.getResults().get(i).getContent() + "\n\n\n\n";
                }

                if (review != "")
                    reviews.setText(review);
                else
                    reviews.setText("No Reviews");
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

        rootView = inflater.inflate(R.layout.fragment_details, container, false);

        Bundle arguments = getArguments();

            toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);



        if (arguments != null) {

            mUri = arguments.getParcelable(MovieDetailFragment.DETAIL_URI);
        }
            sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());


            div1 = (View) rootView.findViewById(R.id.divider);
            div2 = (View) rootView.findViewById(R.id.divider2);
            backdropView = (ImageView) rootView.findViewById(R.id.backdrop_view);
            posterView = (ImageView) rootView.findViewById(R.id.posterPath);
            releaseDate = (TextView) rootView.findViewById(R.id.releaseDate);
            userRating = (TextView) rootView.findViewById(R.id.userRating);
            overview = (TextView) rootView.findViewById(R.id.overview);
            recList = (RecyclerView) rootView.findViewById(R.id.trailersList);
            emptyView = (TextView) rootView.findViewById(R.id.empty_view);
            headTrailers =(TextView) rootView.findViewById(R.id.text_trailers);
            headReviews = (TextView) rootView.findViewById(R.id.text_reviews);
            reviews = (TextView) rootView.findViewById(R.id.reviews);

            rootLayout = (CoordinatorLayout) rootView.findViewById(R.id.rootLayout);
            collapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsingToolbarLayout);

            fabBtn = (FloatingActionButton) rootView.findViewById(R.id.fabBtn);
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            sortBy = sharedPrefs.getString(getString(R.string.pref_sortby_key), getString(R.string.pref_sortby_default));

            if (savedInstanceState != null) {
                if (savedInstanceState.containsKey("Trailers")) {

                    videoTrailers = savedInstanceState.getParcelable("Trailers");

                    if (videoTrailers.getResults().size() == 0) {
                        recList.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        recList.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);
                    }
                    RVAdapter adapter = new RVAdapter(videoTrailers);
                    recList.setAdapter(adapter);
                    Log.d(TAG, "In SavedInstance State recyclerview adapter is called");
                }
                if (savedInstanceState.containsKey("Reviews")) {
                    review = savedInstanceState.getString("Reviews");
                    if (review != "")
                        reviews.setText(review);
                }
            }

            if (sortBy.equals("favorite")) {
                fabBtn.setVisibility(View.VISIBLE);
            } else {


                fabBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (favValue == 0) {
                            fabBtn.setImageResource(R.drawable.ic_star_border_white);
                            if (removeMovieFav() != 0) {
                                Toast.makeText(getActivity(), "Removed from Favorites", Toast.LENGTH_SHORT).show();
                                favValue = 1;
                            }
                        } else {
                            fabBtn.setImageResource(R.drawable.ic_star_white);
                            if (addMovieFav() != 0) {
                                Toast.makeText(getActivity(), "Added to Favorites", Toast.LENGTH_SHORT).show();
                                favValue = 0;
                            }

                        }

                    }
                });
            }

        return rootView;

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = null;
        String[] selectionArgs = null;


        Log.d(TAG, "mUri in DetailFragment :" + mUri);
        if (null != mUri) {
            return new CursorLoader(getActivity(),
                    mUri,
                    null,
                    selection,
                    selectionArgs,
                    null);
        }
        return null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    long addMovieFav() {
        long movieLocationId = 0;


        // Now that the content provider is set up, inserting rows of data is pretty simple.
        // First create a ContentValues object to hold the data you want to insert.
        ContentValues movieValues = new ContentValues();

        // Then add the data, along with the corresponding name of the data type,
        // so the content provider knows what kind of value is being inserted.
        movieValues.put(MovieContract.FavMovieEntry.COLUMN_MOVIE_BACKDROPPATH, movieDetailCursor.getString(movieDetailCursor.getColumnIndex("backdropPath")));
        movieValues.put(MovieContract.FavMovieEntry.COLUMN_MOVIE_ID, movieDetailCursor.getString(movieDetailCursor.getColumnIndex("movieId")));
        movieValues.put(MovieContract.FavMovieEntry.COLUMN_MOVIE_TITLE, movieDetailCursor.getString(movieDetailCursor.getColumnIndex("original_title")));
        movieValues.put(MovieContract.FavMovieEntry.COLUMN_MOVIE_POSTERPATH, movieDetailCursor.getString(movieDetailCursor.getColumnIndex("poster_path")));
        movieValues.put(MovieContract.FavMovieEntry.COLUMN_MOVIE_OVERVIEW, movieDetailCursor.getString(movieDetailCursor.getColumnIndex("overview")));
        movieValues.put(MovieContract.FavMovieEntry.COLUMN_MOVIE_RELEASEDATE, movieDetailCursor.getString(movieDetailCursor.getColumnIndex("release_date")));
        movieValues.put(MovieContract.FavMovieEntry.COLUMN_MOVIE_AVERAGEVOTE, movieDetailCursor.getDouble(movieDetailCursor.getColumnIndex("vote_average")));

        Uri insertedUri = getActivity().getContentResolver().insert(
                MovieContract.FavMovieEntry.CONTENT_URI,
                movieValues
        );

        // The resulting URI contains the ID for the row.
        movieLocationId = ContentUris.parseId(insertedUri);


        return movieLocationId;
    }


    int removeMovieFav() {


        int rowsDeleted = (getActivity().getContentResolver().delete(
                MovieContract.FavMovieEntry.CONTENT_URI,
                MovieContract.FavMovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{movieDetailCursor.getString(movieDetailCursor.getColumnIndex("movieId"))}));

        Log.d(TAG, "rowsDeleted = " + rowsDeleted);


        return rowsDeleted;
    }


    // Set the cursor in our CursorAdapter once the Cursor is loaded
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor mDetailCursor) {

        mDetailCursor.moveToFirst();
        DatabaseUtils.dumpCursor(mDetailCursor);
        movieDetailCursor = mDetailCursor;

        //initToolbar();

        final String id = mDetailCursor.getString(mDetailCursor.getColumnIndex("movieId"));
        movieCursor = getActivity().getContentResolver().query(
                MovieContract.FavMovieEntry.CONTENT_URI,
                new String[]{MovieContract.FavMovieEntry._ID},
                MovieContract.FavMovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{id},
                null);
        if (movieCursor.moveToFirst()) {
            fabBtn.setImageResource(R.drawable.ic_star_white);
            favValue = 0;
        } else {
            fabBtn.setImageResource(R.drawable.ic_star_border_white);
            favValue = 1;
        }

        colIdx = mDetailCursor.getColumnIndex("original_title");
        collapsingToolbarLayout.setTitle(mDetailCursor.getString(colIdx));

        //Backdrop Image
        colIdx = mDetailCursor.getColumnIndex("backdropPath");
        Picasso.with(getActivity()).load(mDetailCursor.getString(colIdx)).into(backdropView);

        //PosterImage
        colIdx = mDetailCursor.getColumnIndex("poster_path");
        Picasso.with(getActivity()).load(mDetailCursor.getString(colIdx)).into(posterView);


        //Release Date
        colIdx = mDetailCursor.getColumnIndex("release_date");
        String str = mDetailCursor.getString(colIdx).substring(0, 4);
        releaseDate.setText(str);

        //userRating
        colIdx = mDetailCursor.getColumnIndex("vote_average");
        userRating.setText(Double.toString(mDetailCursor.getDouble(colIdx)) + "/10");

        //overview
        colIdx = mDetailCursor.getColumnIndex("overview");
        overview.setText(mDetailCursor.getString(colIdx));

        //Videos List
        recList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recList.setLayoutManager(llm);

        div1.getLayoutParams().height = 3;

        colIdx = mDetailCursor.getColumnIndex("movieId");
        GetTrailers(mDetailCursor.getString(colIdx));

        headTrailers.setText("Trailers");
        div2.getLayoutParams().height = 3;
        headReviews.setText("Reviews");
        GetReviews(mDetailCursor.getString(colIdx));

//        if (review != "")
//            reviews.setText(review);

        // We still need this for the share intent
        mMovieTitle = String.format("%s \n\n\n\n\n %s", mDetailCursor.getString(mDetailCursor.getColumnIndex("original_title")),
                mDetailCursor.getString(mDetailCursor.getColumnIndex("overview")));

        // If onCreateOptionsMenu has already happened, we need to update the share intent now.
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    // reset CursorAdapter on Loader Reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mDetailCursor = null;
    }


    private void initToolbar() {
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }
}
