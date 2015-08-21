package com.divya.android.movies.popmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by DivyaM on 8/17/2015.
 */
public class Utility {
    public static String getSortOption(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        return prefs.getString(context.getString(R.string.pref_sortby_key),
                context.getString(R.string.pref_sortby_default));
    }
}
