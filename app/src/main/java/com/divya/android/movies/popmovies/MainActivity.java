package com.divya.android.movies.popmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.stetho.Stetho;


public class MainActivity extends AppCompatActivity {

    private String sortInfo;
    protected final String TAG = getClass().getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sortInfo = Utility.getSortOption(this);
        setContentView(R.layout.activity_main);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String sortBy = Utility.getSortOption(this);
        Log.d(TAG,"sortIn Main : "+sortBy);
        if(!sortBy.equals(sortInfo)){
            PopularMovieListFragment pmf = (PopularMovieListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
            if(null != pmf)
                pmf.onSettingsChanged();


            sortInfo = sortBy;
        }

    }
}
