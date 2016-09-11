package com.example.vishnu.roadcrashemergency;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by vishnu on 11/9/16.
 */
public class PhoneStateBroadCastReceiver extends BroadcastReceiver {

    public static final String LOG_TAG = "BroadcastReceiver";
    Context mContext;
    String incoming_nr;
    private int prev_state;

    @Override
    public void onReceive(Context context, Intent intent) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        CustomPhoneStateListener customPhoneStateListener = new CustomPhoneStateListener();

        telephonyManager.listen(customPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        Bundle bundle = intent.getExtras();
        String phoneNr = bundle.getString("incoming_number");
        Log.d(LOG_TAG, "Phone no: " + phoneNr);

        mContext = context;


    }

    public class CustomPhoneStateListener extends PhoneStateListener{

        public static final String LOG_TAG = "PhoneStateListener";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);

            if(incomingNumber!=null && incomingNumber.length()>0)
                incoming_nr = incomingNumber;

            switch(state){
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d(LOG_TAG, "CALL_STATE_RINGING");
                    prev_state = state;
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d(LOG_TAG, "CALL_STATE_OFFHOOK");
                    prev_state = state;
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    if(prev_state ==TelephonyManager.CALL_STATE_OFFHOOK)
                        Log.d(LOG_TAG, "Call Disconnected");
                    else if(prev_state == TelephonyManager.CALL_STATE_RINGING)
                        Log.d(LOG_TAG, "Rejected");
                    break;
            }


        }
    }
}
