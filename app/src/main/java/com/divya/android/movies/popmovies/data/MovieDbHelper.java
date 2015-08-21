package com.divya.android.movies.popmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.divya.android.movies.popmovies.data.MovieContract.MoviePopularityEntry;
import com.divya.android.movies.popmovies.data.MovieContract.MovieVoteAverageEntry;

/**
 * Created by DivyaM on 8/16/2015.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_POPULARITY_TABLE= "CREATE TABLE " + MoviePopularityEntry.TABLE_NAME + " (" +
                MoviePopularityEntry._ID + " INTEGER PRIMARY KEY," +
                MoviePopularityEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                MoviePopularityEntry.COLUMN_MOVIE_BACKDROPPATH + " TEXT , " +
                MoviePopularityEntry.COLUMN_MOVIE_POSTERPATH + " TEXT , " +
                MoviePopularityEntry.COLUMN_MOVIE_RELEASEDATE + " TEXT , " +
                MoviePopularityEntry.COLUMN_MOVIE_AVERAGEVOTE + " REAL , " +
                MoviePopularityEntry.COLUMN_MOVIE_OVERVIEW + " TEXT , " +
                MoviePopularityEntry.COLUMN_MOVIE_ID + " TEXT UNIQUE NOT NULL " +
                " );";

        Log.d(DATABASE_NAME, "Create popularity table statement : " + SQL_CREATE_POPULARITY_TABLE);

        final String SQL_CREATE_VOTE_TABLE= "CREATE TABLE " + MovieVoteAverageEntry.TABLE_NAME + " (" +
                MovieVoteAverageEntry._ID + " INTEGER PRIMARY KEY," +
                MovieVoteAverageEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieVoteAverageEntry.COLUMN_MOVIE_BACKDROPPATH + " TEXT , " +
                MovieVoteAverageEntry.COLUMN_MOVIE_POSTERPATH + " TEXT , " +
                MovieVoteAverageEntry.COLUMN_MOVIE_RELEASEDATE + " TEXT , " +
                MovieVoteAverageEntry.COLUMN_MOVIE_AVERAGEVOTE + " REAL , " +
                MovieVoteAverageEntry.COLUMN_MOVIE_OVERVIEW + " TEXT , " +
                MovieVoteAverageEntry.COLUMN_MOVIE_ID + " TEXT UNIQUE NOT NULL " +
                " );";

        Log.d(DATABASE_NAME, "Create vote table statement : " + SQL_CREATE_VOTE_TABLE);


        sqLiteDatabase.execSQL(SQL_CREATE_POPULARITY_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_VOTE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviePopularityEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieVoteAverageEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
