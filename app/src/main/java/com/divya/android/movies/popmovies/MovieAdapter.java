package com.divya.android.movies.popmovies;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Movie;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by DivyaM on 8/19/2015.
 */
public class MovieAdapter extends CursorAdapter {

    private static final String LOG_TAG = Movie.class.getSimpleName();
    private Context mContext;
    private static int sLoaderID;

    public static class ViewHolder {
        public final ImageView imageView;
        // public final TextView textView;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.movie_image);
            //textView = (TextView) view.findViewById(R.id.flavor_text);
        }
    }

    public MovieAdapter(Context context, Cursor c, int flags, int loaderID) {
        super(context, c, flags);
        Log.d(LOG_TAG, "MovieAdapter");
        mContext = context;
        sLoaderID = loaderID;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int layoutId = R.layout.movie_item;
        Log.d(LOG_TAG, "In new View");
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        ViewHolder viewHolder = (ViewHolder) view.getTag();

        Log.d(LOG_TAG, "In bind View");
        String sortOption = Utility.getSortOption(context);

        int imageIndex = cursor.getColumnIndex("poster_path");
        final String image = cursor.getString(imageIndex);
        Picasso.with(mContext).load(image).into(viewHolder.imageView);

    }
}
