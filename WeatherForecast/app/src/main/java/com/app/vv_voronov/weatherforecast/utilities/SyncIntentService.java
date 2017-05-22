package com.app.vv_voronov.weatherforecast.utilities;

import android.app.IntentService;
import android.content.Intent;


public class SyncIntentService extends IntentService {
    public SyncIntentService() {
        super("SyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SyncTask.syncWeather(this);

    }
}
