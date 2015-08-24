package com.divya.android.movies.popmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by DivyaM on 8/16/2015.
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
    public static final String PATH_POPULARITY = "popular";
    public static final String PATH_VOTE = "vote";
    public static final String PATH_FAV = "favorite";

    public static final class MoviePopularityEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_POPULARITY).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POPULARITY;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POPULARITY;

        // Table name
        public static final String TABLE_NAME = "popularity";

        //Movietitle
        public static final String COLUMN_MOVIE_TITLE = "original_title";

        //backdrop Image of movie
        public static final String COLUMN_MOVIE_BACKDROPPATH = "backdropPath";

        //POSTER Image of movie
        public static final String COLUMN_MOVIE_POSTERPATH = "poster_path";

        //RELEASEDATE of movie
        public static final String COLUMN_MOVIE_RELEASEDATE = "release_date";

        //Average vote of movie
        public static final String COLUMN_MOVIE_AVERAGEVOTE = "vote_average";


        //Movie overview
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";

        //Id of movie
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static String COLUMN_MOVIE_FAV = "favorite";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildFavMovieList() {
            return CONTENT_URI.buildUpon()
                    .appendPath(COLUMN_MOVIE_FAV).build();
        }

        public static int getFavValue(Uri uri) {
            String favString = uri.getQueryParameter(COLUMN_MOVIE_FAV);

            return Integer.parseInt(favString);
        }
    }


    public static final class MovieVoteAverageEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_VOTE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VOTE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VOTE;

        // Table name
        public static final String TABLE_NAME = "voteaverage";

        //Movietitle
        public static final String COLUMN_MOVIE_TITLE = "original_title";

        //backdrop Image of movie
        public static final String COLUMN_MOVIE_BACKDROPPATH = "backdropPath";

        //POSTER Image of movie
        public static final String COLUMN_MOVIE_POSTERPATH = "poster_path";

        //RELEASEDATE of movie
        public static final String COLUMN_MOVIE_RELEASEDATE = "release_date";

        //Average vote of movie
        public static final String COLUMN_MOVIE_AVERAGEVOTE = "vote_average";


        //Movie overview
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";

        //Id of movie
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static String COLUMN_MOVIE_FAV = "favorite";


        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildFavMovieList() {
            return CONTENT_URI.buildUpon()
                    .appendQueryParameter(COLUMN_MOVIE_FAV, "1").build();
        }


    }

    public static final class FavMovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAV;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAV;

        // Table name
        public static final String TABLE_NAME = "favorite";

        //Movietitle
        public static final String COLUMN_MOVIE_TITLE = "original_title";

        //backdrop Image of movie
        public static final String COLUMN_MOVIE_BACKDROPPATH = "backdropPath";

        //POSTER Image of movie
        public static final String COLUMN_MOVIE_POSTERPATH = "poster_path";

        //RELEASEDATE of movie
        public static final String COLUMN_MOVIE_RELEASEDATE = "release_date";

        //Average vote of movie
        public static final String COLUMN_MOVIE_AVERAGEVOTE = "vote_average";


        //Movie overview
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";

        //Id of movie
        public static final String COLUMN_MOVIE_ID = "movieId";


        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
