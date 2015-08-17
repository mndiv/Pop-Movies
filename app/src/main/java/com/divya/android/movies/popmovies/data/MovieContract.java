package com.divya.android.movies.popmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by KeerthanaS on 8/16/2015.
 */
public class MovieContract {
    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.divya.android.movies.popmovies";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.divya.android.movies.popmovies/movie/ is a valid path for
    // looking at weather data. content://com.divya.android.movies.popmovies/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_MOVIE = "movie";

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        // Table name
        public static final String TABLE_NAME = "movie";

        //backdrop Image of movie
        public static final String COLUMN_MOVIE_BACKDROPPATH = "backdropPath";

        //Id of movie
        public static final String COLUMN_MOVIE_ID = "movie_id";

        //Movietitle
        public static final String COLUMN_MOVIE_TITLE = "original_title";

        //Movie overview
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";

        //RELEASEDATE of movie
        public static final String COLUMN_MOVIE_RELEASEDATE = "release_date";

        //POSTER Image of movie
        public static final String COLUMN_MOVIE_POSTERPATH = "poster_path";

        //Average vote of movie
        public static final String COLUMN_MOVIE_AVERAGEVOTE = "vote_average";


        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }
}
