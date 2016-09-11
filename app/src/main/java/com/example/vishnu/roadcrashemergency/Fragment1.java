package com.example.vishnu.roadcrashemergency;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by vishnu on 10/9/16.
 */
public class Fragment1 extends Fragment {

    public static final int CALL_REQUEST_CODE = 1004;
    public static final String LOG_TAG = "Fragment1";
    ImageButton call;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);

        call = (ImageButton) view.findViewById(R.id.callerImageButton);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MainActivity.isPermissionGiven){
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + Fragment2.contacts.get(0).getNumber()));
                    startActivityForResult(intent, CALL_REQUEST_CODE);
                }

                else{
                    Toast.makeText(getActivity(), "Enable Permissions to call", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

            case CALL_REQUEST_CODE:

                Log.d(LOG_TAG, "I think the call has ended");
                break;
        }
    }
}
