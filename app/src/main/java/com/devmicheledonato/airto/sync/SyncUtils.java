package com.devmicheledonato.airto.sync;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by Michele on 28/05/2017.
 */

public class SyncUtils {

    private static final String TAG = SyncUtils.class.getSimpleName();

    private static boolean sInitialized;

    synchronized public static void initialize(@NonNull final Context context) {
        Log.d(TAG, "initialize");
        /*
         * Only perform initialization once per app lifetime. If initialization has already been
         * performed, we have nothing to do in this method.
         */
        if (sInitialized) return;

        sInitialized = true;

        startImmediateSync(context);
    }

    public static void startImmediateSync(@NonNull final Context context) {
        if (!sInitialized) return;
        Log.d(TAG, "startImmediateSync");
        context.startService(new Intent(context, WeatherSyncIntentService.class));
    }
}
