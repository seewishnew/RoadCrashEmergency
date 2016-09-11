package com.example.vishnu.roadcrashemergency;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TravelModeActivity extends AppCompatActivity implements SensorEventListener{
    public static final String LOG_TAG = "TravelModeActivity";
    public static final int NOTIFICATION_ID = 1005;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 4000;
    public static boolean isService = false;
    Button startserviceButton;
    private int messageTime = 0;
    private int callTime = 0;
    boolean canceled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_mode);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this,senAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);

        startserviceButton = (Button) findViewById(R.id.button1);
        startserviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isService){
                startService(new Intent(TravelModeActivity.this, TravelModeActivity.class));
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                    startserviceButton.setText("Stop");
                isService = true;
                }

                else{
                    stopService(new Intent(TravelModeActivity.this,
                            TravelModeActivity.class));
                    startserviceButton.setText("Start");
                    isService = false;
                }
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    //Todo: Notification/popup
                    Log.d(LOG_TAG, "Shake!");

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                    builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
                    builder.setContentTitle("Are you alright?");
                    builder.setContentText("Click to disable within 30 seconds");
                    builder.setAutoCancel(true);
                    builder.mNotification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    PendingIntent pendingIntent = PendingIntent.getActivity(
                            this, 0,
                            new Intent(this, DummyActivity.class),
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

                    builder.setContentIntent(pendingIntent);



                    long currentTime = System.currentTimeMillis();
// mId allows you to update the notification later on.
                    mNotificationManager.notify(NOTIFICATION_ID, builder.build());

                    StringBuilder builder1 = new StringBuilder();
                    if(MainActivity.mLastLocation!=null) {
                        builder1.append("Am in danger at Latitude: " + MainActivity.mLastLocation.getLatitude()
                                + "\n Longitude: " + MainActivity.mLastLocation.getLongitude());
                    }

                    String message = builder1.toString();

                    while(currentTime+12000 >= System.currentTimeMillis()){
                        try {
                            Log.d(LOG_TAG, "Going to sleep");
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    try{
                        if(messageTime==0 && !canceled)
                        if(MainActivity.isPermissionGiven) {
                            Log.d(LOG_TAG, "Sending Message");
                            SmsManager smsManager = SmsManager.getDefault();
                            messageTime++;
                            smsManager.sendTextMessage("9445217542", null, message, null, null);
                            Toast.makeText(getApplicationContext(), "SMS Sent!", Toast.LENGTH_LONG).show();
                        }
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(getApplicationContext(),
                                "SMS failed, please try again later!",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                    }


                    Log.d(LOG_TAG, "You're dead");
                    if(callTime==0 && !canceled){
                        ++callTime;
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + "7338951501"));
                        Log.d(LOG_TAG, "Calling no");
                        if(MainActivity.isPermissionGiven)
                            startActivity(intent);
                    }
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}
