package com.example.vishnu.roadcrashemergency;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;

public class SirenActivity extends AppCompatActivity {

    RelativeLayout relativeLayout;
    private static boolean black = true;

    MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siren);

        relativeLayout = (RelativeLayout) findViewById(R.id.sirenBackground);

        FlashScreen flashScreen = new FlashScreen();

        flashScreen.execute();

        mediaPlayer = MediaPlayer.create(this, R.raw.siren);

        mediaPlayer.setVolume(1F, 1F);

        mediaPlayer.start();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }

    public void changeToBlack(){
        relativeLayout.setBackgroundColor(Color.BLACK);
    }

    public void changeToWhite(){
        relativeLayout.setBackgroundColor(Color.WHITE);
    }

    class FlashScreen extends AsyncTask<Void, Void, Void>{

        private boolean flash3 = true;
        private int count = 0;

        private long sleepTime(){
            if(count == 4){
                count = 0;
                flash3 = !flash3;
            }

            if(flash3)
                return 1000L;
            else
                return 200L;
        }

        @Override
        protected Void doInBackground(Void... params) {

           while(count<5){

               ++count;

               runOnUiThread(
                       new Runnable() {
                           @Override
                           public void run() {
                               if(count%2==0)
                               changeToWhite();

                               else
                                   changeToBlack();
                           }
                       }
               );

               try {
                   Thread.sleep(
                           sleepTime()
                   );
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }

            return null;

        }
    }
}
