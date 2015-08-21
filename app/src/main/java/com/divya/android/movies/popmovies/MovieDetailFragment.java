package com.divya.android.movies.popmovies;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.divya.android.movies.popmovies.api.ApiClient;
import com.divya.android.movies.popmovies.api.GetMovieDataApi;
import com.divya.android.movies.popmovies.model.ResultReviews;
import com.divya.android.movies.popmovies.model.ResultVideos;
import com.divya.android.movies.popmovies.model.UriData;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>{

    static final String MOVIES_BASE_URL = "http://api.themoviedb.org/3";
    protected final String TAG = getClass().getSimpleName();
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    CoordinatorLayout rootLayout;
    FloatingActionButton fabBtn;
    private int mUriId;
    private Uri mUri;
    private Cursor mDetailCursor;
    private ImageView backdropView;
    private ImageView posterView;
    private TextView releaseDate;
    private TextView userRating;
    private TextView overview;
    RecyclerView recList;
    private static final int DETAIL_LOADER = 0;


    private String mPosterPath;
    TextView reviews;

    String api_key = "2fc475941d44b7da433d1f18e24e2551";
    //String api_key = ; /*Please use your own api_key for moviedb*/



    public MovieDetailFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
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
                RVAdapter adapter = new RVAdapter(resultVideos);
                recList.setAdapter(adapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "failure: " + error);
            }
        });
    }

    public void GetReviews(final String id) {
        GetMovieDataApi reviewService;
        int videoId = Integer.parseInt(id);

        reviewService = ApiClient.ReviewDataApiInterface();
        reviewService.getMovieReviewsFromApi(videoId, api_key, new Callback<ResultReviews>() {
            @Override
            public void success(ResultReviews resultReviews, Response response) {
                String review = "";
                for (int i = 0; i < resultReviews.getResults().size(); i++) {

                    Log.d(TAG, "author:" + resultReviews.getResults().get(i).getAuthor());

                    review += resultReviews.getResults().get(i).getAuthor() + "\n\n" +
                            resultReviews.getResults().get(i).getContent() + "\n\n";
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
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        //initToolbar();
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        Intent intent = getActivity().getIntent();

        Bundle data = intent.getExtras();
        UriData uriObj = data.getParcelable("MovieInfo");
        mUriId = uriObj.getUriId();
        mUri = uriObj.getUri();

        Log.d(TAG, "content Uri : " + mUri);
        //getLoaderManager().initLoader(MOVIE_LOADER, null, MovieDetailFragment.this);


        backdropView = (ImageView)rootView.findViewById(R.id.backdrop_view);
        posterView = (ImageView) rootView.findViewById(R.id.posterPath);
        releaseDate = (TextView) rootView.findViewById(R.id.releaseDate);
        userRating = (TextView) rootView.findViewById(R.id.userRating);
        overview = (TextView) rootView.findViewById(R.id.overview);
        recList = (RecyclerView) rootView.findViewById(R.id.trailersList);
        reviews = (TextView) rootView.findViewById(R.id.reviews);

        rootLayout = (CoordinatorLayout) rootView.findViewById(R.id.rootLayout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsingToolbarLayout);

        fabBtn = (FloatingActionButton) rootView.findViewById(R.id.fabBtn);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(rootLayout, "Hello. I am Snackbar!", Snackbar.LENGTH_SHORT)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();
            }
        });
        /*
        ImageView backdropView = (ImageView)rootView.findViewById(R.id.backdrop_view);
        Picasso.with(getActivity()).load(obj.getBackdropPath()).into(backdropView);

        ImageView view = (ImageView) rootView.findViewById(R.id.posterPath);
        Picasso.with(getActivity()).load(obj.getPosterPath()).into(view);


        TextView releaseDate = (TextView) rootView.findViewById(R.id.releaseDate);
        String str = obj.getReleaseDate().substring(0, 4);
        releaseDate.setText(str);

        // TextView duration = (TextView)rootView.findViewById(R.id.duration);
        // duration.setText(obj.get());

        TextView userRating = (TextView) rootView.findViewById(R.id.userRating);
        userRating.setText(Double.toString(obj.getVoteAverage()) + "/10");

        TextView overview = (TextView) rootView.findViewById(R.id.overview);
        overview.setText(obj.getOverview());

        //Videos List
        recList = (RecyclerView) rootView.findViewById(R.id.trailersList);
        recList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recList.setLayoutManager(llm);

        GetTrailers(obj.getId());

        reviews = (TextView) rootView.findViewById(R.id.reviews);

        GetReviews(obj.getId());

*/

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        String selection = null;
        String [] selectionArgs = null;

        Log.d(TAG , "mUri in DetailFragment :" + mUri);
        return new CursorLoader(getActivity(),
                mUri,
                null,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }

    // Set the cursor in our CursorAdapter once the Cursor is loaded
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor mDetailCursor) {

        mDetailCursor.moveToFirst();
        DatabaseUtils.dumpCursor(mDetailCursor);

        int colIdx = mDetailCursor.getColumnIndex("original_title");
        collapsingToolbarLayout.setTitle(mDetailCursor.getString(colIdx));

        //Backdrop Image
        colIdx = mDetailCursor.getColumnIndex("backdropPath");
        Picasso.with(getActivity()).load(mDetailCursor.getString(colIdx)).into(backdropView);

        //PosterImage
        colIdx = mDetailCursor.getColumnIndex("poster_path");
        Picasso.with(getActivity()).load(mDetailCursor.getString(colIdx)).into(posterView);


        //Release Date
        colIdx = mDetailCursor.getColumnIndex( "release_date");
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

        colIdx = mDetailCursor.getColumnIndex("movieId");
        GetTrailers(mDetailCursor.getString(colIdx));


        GetReviews(mDetailCursor.getString(colIdx));

    }

    // reset CursorAdapter on Loader Reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        mDetailCursor = null;
    }


   /* private void initToolbar() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
    }*/
}
