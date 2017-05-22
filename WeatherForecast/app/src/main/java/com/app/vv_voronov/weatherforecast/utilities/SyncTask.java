package com.app.vv_voronov.weatherforecast.utilities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.app.vv_voronov.weatherforecast.data.WeatherContract;
import com.app.vv_voronov.weatherforecast.data.WeatherPreference;

import java.net.URL;


public class SyncTask {

    synchronized public static void syncWeather(Context context) {

        try {
                  String locationQuery = WeatherPreference.getPreferredWeatherLocation(context);

            URL weatherRequestUrl = NetworkUtils.buildUrl(locationQuery);

            String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);


            ContentValues[] weatherValues = OpenWeatherJsonUtils.getWeatherContentValuesFromJson(context, jsonWeatherResponse);

            if (weatherValues != null && weatherValues.length != 0) {
                   ContentResolver sunshineContentResolver = context.getContentResolver();

              sunshineContentResolver.delete(
                        WeatherContract.WeatherEntry.CONTENT_URI, null, null);
                sunshineContentResolver.bulkInsert(
                        WeatherContract.WeatherEntry.CONTENT_URI, weatherValues);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}
