package com.divya.android.movies.popmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import com.divya.android.movies.popmovies.model.MovieInfo;
import com.divya.android.movies.popmovies.model.ResultReviews;
import com.divya.android.movies.popmovies.model.ResultVideos;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {

    static final String MOVIES_BASE_URL = "http://api.themoviedb.org/3";
    protected final String TAG = getClass().getSimpleName();
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    CoordinatorLayout rootLayout;
    FloatingActionButton fabBtn;


    private String mPosterPath;
    TextView reviews;

    String api_key = "2fc475941d44b7da433d1f18e24e2551";
    //String api_key = ; /*Please use your own api_key for moviedb*/

    RecyclerView recList;

    public MovieDetailFragment() {
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
        MovieInfo obj = data.getParcelable("MovieInfo");

      /*  TextView movieTitle = (TextView) rootView.findViewById(R.id.movieTitle);
        movieTitle.setText(obj.getOriginalTitle());

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

        GetReviews(obj.getId());*/

        rootLayout = (CoordinatorLayout) rootView.findViewById(R.id.rootLayout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle("Design Library");

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

        return rootView;
    }

   /* private void initToolbar() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
    }*/
}
