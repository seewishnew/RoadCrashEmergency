package com.example.vishnu.roadcrashemergency;

import android.app.NotificationManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class DummyActivity extends AppCompatActivity {

    public static final String CANCELED = "Canceled";
    public static final String LOG_TAG = "DummyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);
        Log.d(LOG_TAG, "Inside dummy activity");
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(TravelModeActivity.NOTIFICATION_ID);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);

    }
}
