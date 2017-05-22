package com.app.vv_voronov.weatherforecast;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.vv_voronov.weatherforecast.data.WeatherContract;
import com.app.vv_voronov.weatherforecast.data.WeatherPreference;
import com.app.vv_voronov.weatherforecast.utilities.SyncUtils;


public class MainActivity extends AppCompatActivity implements ForecastAdapter.ForecastAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int FORECAST_LOADER_ID = 0;
    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;
    private RecyclerView mRecyclerView;
    private ForecastAdapter mForecastAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private int mPosition = RecyclerView.NO_POSITION;

    public static final String[] MAIN_FORECAST_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
    };

    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_MAX_TEMP = 1;
    public static final int INDEX_WEATHER_MIN_TEMP = 2;
    public static final int INDEX_WEATHER_CONDITION_ID = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_forecast);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        int recyclerViewOrientation = LinearLayoutManager.VERTICAL;

        boolean shouldReverseLayout = false;
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, recyclerViewOrientation, shouldReverseLayout);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mForecastAdapter = new ForecastAdapter(this,this);

        mRecyclerView.setAdapter(mForecastAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        int loaderId = FORECAST_LOADER_ID;

                Bundle bundleForLoader = null;

        getSupportLoaderManager().initLoader(loaderId, null, this);

        SyncUtils.initialize(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {

        switch (id) {

            case FORECAST_LOADER_ID:
                Uri forecastQueryUri = WeatherContract.WeatherEntry.CONTENT_URI;
                String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
                String selection = WeatherContract.WeatherEntry.getSqlSelectForTodayOnwards();

                return new CursorLoader(this,
                        forecastQueryUri,
                        MAIN_FORECAST_PROJECTION,
                        selection,
                        null,
                        sortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mForecastAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
        if (data.getCount() != 0) showWeatherDataView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mForecastAdapter.swapCursor(null);
    }


    private void openLocationInMap() {
        String addressString = WeatherPreference.getPreferredWeatherLocation(this);
        Uri geoLocation = Uri.parse("geo:0,0?q=" + addressString);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if (intent.resolveActivity(getPackageManager()) != null)
            startActivity(intent);
    }

    @Override
    public void onClick(long date) {
            Intent weatherDetailIntent = new Intent(MainActivity.this, DetailActivity.class);
            Uri uriForDateClicked = WeatherContract.WeatherEntry.buildWeatherUriWithDate(date);
            weatherDetailIntent.setData(uriForDateClicked);
            startActivity(weatherDetailIntent);
    }

    private void showWeatherDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (PREFERENCES_HAVE_BEEN_UPDATED) {
            getSupportLoaderManager().restartLoader(FORECAST_LOADER_ID, null, this);
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.forecast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_map) {
            openLocationInMap();
            return true;
        }

        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}