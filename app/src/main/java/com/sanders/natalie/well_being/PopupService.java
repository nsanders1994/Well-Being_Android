package com.sanders.natalie.well_being;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

/**
 * Created by Natalie on 2/4/2015.
 */
public class PopupService extends IntentService {
    private static final String TAG = PopupService.class.getSimpleName();
    private AlertDatabaseHandler dbHandler;

    public PopupService() {
        super("Service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // If the user is not making a phone call, create a pop-up alert dialog
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if(audioManager.getMode() != AudioManager.MODE_IN_CALL) {
            Intent i = new Intent(PopupService.this, Dialog.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }
}
