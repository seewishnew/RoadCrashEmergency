package com.example.vishnu.roadcrashemergency;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vishnu on 10/9/16.
 */
public class Fragment2 extends Fragment {

    public static final int REQUEST_CODE = 1003;
    public static final String LOG_TAG = "Fragment2";
    ListView listView;
    Button addContactsButton;
    ImageButton travelImageButton;
    public static List<Contact> contacts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);

        addContactsButton = (Button) view.findViewById(R.id.addContactsButton);
        travelImageButton = (ImageButton) view.findViewById(R.id.travelImageButton);

        travelImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TravelModeActivity.class);
                startActivity(intent);
            }
        });

        listView = (ListView) view.findViewById(R.id.listView);

        contacts = new ArrayList<>();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        contacts.clear();

        SharedPreferences preferences = getActivity().getSharedPreferences(
                ContactsFragment.EMERGENCY_CONTACTS,
                Context.MODE_PRIVATE
        );

        Map allContacts = preferences.getAll();

        Iterator iterator = allContacts.entrySet().iterator();

        while(iterator.hasNext()){
            Map.Entry pair = (Map.Entry) iterator.next();

            Cursor cursor;
            cursor = getActivity().getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    ContactsFragment.PROJECTION,
                    ContactsContract.CommonDataKinds.Phone._ID + " = " + pair.getKey().toString(),
                    null, null
            );

            if(cursor!=null){
                Log.d(LOG_TAG, "cursor not null");

                cursor.moveToFirst();

                Contact contact = new Contact(
                        cursor.getLong(ContactsFragment.CONTACT_ID_INDEX),
                        cursor.getString(ContactsFragment.DISPLAY_NAME),
                        cursor.getString(ContactsFragment.NUMBER)
                );

                Log.d(LOG_TAG, "ID: " + contact.getId() +
                        "\n NAME: " + contact.getName() +
                        "\n NUMBER" + contact.getNumber());

                contacts.add(contact);



            }

            else
                Log.d(LOG_TAG, "Cursor is null");
        }

        listView.setAdapter(new ContactsAdapter(getActivity(), 0, contacts));

        addContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), AddContactsActivity.class);
                startActivityForResult(intent, REQUEST_CODE);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CODE:
                //Todo handle stuff

                contacts.clear();

                SharedPreferences preferences = getActivity().getSharedPreferences(
                        ContactsFragment.EMERGENCY_CONTACTS,
                        Context.MODE_PRIVATE
                );

                Map allContacts = preferences.getAll();

                Iterator iterator = allContacts.entrySet().iterator();

                while(iterator.hasNext()){
                    Map.Entry pair = (Map.Entry) iterator.next();

                    Cursor cursor;
                    cursor = getActivity().getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            ContactsFragment.PROJECTION,
                            ContactsContract.CommonDataKinds.Phone._ID + " = " + pair.getKey().toString(),
                            null, null
                    );

                    if(cursor!=null){
                        Log.d(LOG_TAG, "cursor not null");

                        cursor.moveToFirst();

                        Contact contact = new Contact(
                                cursor.getLong(ContactsFragment.CONTACT_ID_INDEX),
                                cursor.getString(ContactsFragment.DISPLAY_NAME),
                                cursor.getString(ContactsFragment.NUMBER)
                        );

                        Log.d(LOG_TAG, "ID: " + contact.getId() +
                                "\n NAME: " + contact.getName() +
                        "\n NUMBER" + contact.getNumber());

                        contacts.add(contact);

                        listView.setAdapter(new ContactsAdapter(getActivity(), 0, contacts));


                    }

                    else
                        Log.d(LOG_TAG, "Cursor is null");
                }

                break;
        }
    }
}
