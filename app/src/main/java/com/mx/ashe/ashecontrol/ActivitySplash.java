package com.mx.ashe.ashecontrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ActivitySplash extends AppCompatActivity {

    private static String TAG = ActivitySplash.class.getName();
    private static long SLEEP_TIME = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_splash);

        IntentLauncher launcher = new IntentLauncher();
        launcher.start();
    }
    private class IntentLauncher extends Thread {
        @Override
        /**
         * Sleep for some time and than start new activity.
         */
        public void run() {
            try {
                Thread.sleep(SLEEP_TIME * 500);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            // Lanza el layout del Loggin
            Intent intent = new Intent(ActivitySplash.this, ActivityLogin.class);
            ActivitySplash.this.startActivity(intent);
            ActivitySplash.this.finish();
        }
    }
}
