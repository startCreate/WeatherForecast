package com.app.vv_voronov.weatherforecast.utilities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.app.vv_voronov.weatherforecast.data.WeatherContract;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;


public class SyncUtils {
    private static boolean isInitialize;
    private static final int TIME_START = (int) TimeUnit.HOURS.toSeconds(2);
    private static final int TIME_INTERVAL = TIME_START/2;
    public static void startSync(Context context){
        Intent intent = new Intent(context, SyncIntentService.class);
        context.startService(intent);

    }
    public static void initialize(@NonNull final Context context){
        if (isInitialize)return;
        isInitialize = true;
        scheduleFirebaseJobDisp(context);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Uri forecastQuery = WeatherContract.WeatherEntry.CONTENT_URI;
                String[] projection = {WeatherContract.WeatherEntry._ID};
                Cursor cursor = context.getContentResolver().query(forecastQuery,projection,
                        WeatherContract.WeatherEntry.getSqlSelectForTodayOnwards(),null,null);
                if (null==cursor || cursor.getCount()==0)
                    startSync(context);
                return null;
            }
        }.execute();

    }
    private static void scheduleFirebaseJobDisp(Context context){
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Job job = dispatcher.newJobBuilder().setService(FirebaseJobService.class)
                .setTag("weather_forecast_job")
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(TIME_START, TIME_START+TIME_INTERVAL))
                .setReplaceCurrent(true)
        .build();
        dispatcher.schedule(job);
    }
}
