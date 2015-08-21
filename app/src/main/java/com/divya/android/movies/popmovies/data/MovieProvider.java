package com.divya.android.movies.popmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by DivyaM on 8/16/2015.
 */
public class MovieProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();


    private MovieDbHelper mOpenHelper;
    static final int POPULAR = 100;
    static final int VOTE = 200;
    private static final int POPULAR_WITH_ID = 300;
    private static final int VOTE_WITH_ID = 400;

    /* private static final SQLiteQueryBuilder smovieQueryBuilder;

     static{
         smovieQueryBuilder = new SQLiteQueryBuilder();

         //This is an inner join which looks like
         //weather INNER JOIN location ON weather.location_id = location._id
         smovieQueryBuilder.setTables(
                 MovieContract.MovieEntry.TABLE_NAME + " INNER JOIN " +
                         MovieContract.LocationEntry.TABLE_NAME +
                         " ON " + MovieContract.WeatherEntry.TABLE_NAME +
                         "." + MovieContract.WeatherEntry.COLUMN_LOC_KEY +
                         " = " + MovieContract.LocationEntry.TABLE_NAME +
                         "." + MovieContract.LocationEntry._ID);
     }*/
    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, MovieContract.PATH_POPULARITY, POPULAR);
        matcher.addURI(authority, MovieContract.PATH_POPULARITY + "/#", POPULAR_WITH_ID);
        matcher.addURI(authority, MovieContract.PATH_VOTE, VOTE);
        matcher.addURI(authority, MovieContract.PATH_VOTE + "/#", POPULAR_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case POPULAR: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MoviePopularityEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case VOTE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieVoteAverageEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case POPULAR_WITH_ID: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MoviePopularityEntry.TABLE_NAME,
                        projection,
                        MovieContract.MoviePopularityEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case VOTE_WITH_ID
                    : {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieVoteAverageEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieVoteAverageEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            /*case WEATHER_WITH_LOCATION_AND_DATE:
                return WeatherContract.WeatherEntry.CONTENT_ITEM_TYPE;
            case WEATHER_WITH_LOCATION:
                return WeatherContract.WeatherEntry.CONTENT_TYPE;*/
            case POPULAR:
                return MovieContract.MoviePopularityEntry.CONTENT_TYPE;
            case VOTE:
                return MovieContract.MovieVoteAverageEntry.CONTENT_TYPE;

            case POPULAR_WITH_ID:
                return MovieContract.MoviePopularityEntry.CONTENT_ITEM_TYPE;
            case VOTE_WITH_ID:
                return MovieContract.MovieVoteAverageEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case POPULAR: {
                // normalizeDate(values);
                long _id = db.insert(MovieContract.MoviePopularityEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.MoviePopularityEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case VOTE: {
                // normalizeDate(values);
                long _id = db.insert(MovieContract.MovieVoteAverageEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.MovieVoteAverageEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case POPULAR:
                rowsDeleted = db.delete(
                        MovieContract.MoviePopularityEntry.TABLE_NAME, selection, selectionArgs);
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MovieContract.MoviePopularityEntry.TABLE_NAME + "'");
                break;
            case VOTE:
                rowsDeleted = db.delete(
                        MovieContract.MovieVoteAverageEntry.TABLE_NAME, selection, selectionArgs);
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MovieContract.MovieVoteAverageEntry.TABLE_NAME + "'");
                break;
            case POPULAR_WITH_ID:
                rowsDeleted = db.delete(
                        MovieContract.MoviePopularityEntry.TABLE_NAME,
                        MovieContract.MoviePopularityEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MovieContract.MoviePopularityEntry.TABLE_NAME + "'");
                break;
            case VOTE_WITH_ID:
                rowsDeleted = db.delete(
                        MovieContract.MovieVoteAverageEntry.TABLE_NAME,
                        MovieContract.MovieVoteAverageEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MovieContract.MovieVoteAverageEntry.TABLE_NAME + "'");
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case POPULAR:
                //normalizeDate(values);
                rowsUpdated = db.update(MovieContract.MoviePopularityEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case VOTE:
                //normalizeDate(values);
                rowsUpdated = db.update(MovieContract.MovieVoteAverageEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case POPULAR_WITH_ID:
                //normalizeDate(values);
                rowsUpdated = db.update(MovieContract.MoviePopularityEntry.TABLE_NAME,
                        values,
                        MovieContract.MoviePopularityEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
                break;
            case VOTE_WITH_ID:
                //normalizeDate(values);
                rowsUpdated = db.update(MovieContract.MovieVoteAverageEntry.TABLE_NAME,
                        values,
                        MovieContract.MovieVoteAverageEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case POPULAR: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        //normalizeDate(value);
                        long _id = db.insert(MovieContract.MoviePopularityEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case VOTE: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        //normalizeDate(value);
                        long _id = db.insert(MovieContract.MovieVoteAverageEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
