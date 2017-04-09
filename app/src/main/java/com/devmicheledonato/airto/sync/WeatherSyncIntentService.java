package com.devmicheledonato.airto.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class WeatherSyncIntentService extends IntentService {

    public WeatherSyncIntentService() {
        super("WeatherSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SyncTask.syncWeather(this);
    }
}
