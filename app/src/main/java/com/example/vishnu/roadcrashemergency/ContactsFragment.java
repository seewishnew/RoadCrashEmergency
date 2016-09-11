package com.example.vishnu.roadcrashemergency;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by vishnu on 10/9/16.
 */
public class ContactsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener
{

    public static final String SEARCH_STRING = "SEARCH STRING";
    @SuppressLint("InlinedApi")
    private static final String[] FROM_COLUMNS = {
            Build.VERSION.SDK_INT
            >= Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                    ContactsContract.Contacts.DISPLAY_NAME
    };


    private final static int[] TO_IDS = {
            android.R.id.text1
    };

    public static final String[] PROJECTION = {
            ContactsContract.CommonDataKinds.Phone._ID,
            ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY,
            ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY,
            Build.VERSION.SDK_INT
            >=Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY :
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };

    public static final int CONTACT_ID_INDEX = 0;
    public static final int LOOKUP_KEY_INDEX = 1;
    public static final int HAS_PHONE_NUMBER = 2;
    public static final int DISPLAY_NAME = 3;
    public static final int NUMBER = 4;
    public static final String SELECTION =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?" :
                    ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?";
    public static final String LOG_TAG = "Contacts Fragment";
    public static final String EMERGENCY_CONTACTS = "EmergencyContacts";

    private String mSearchString;

    private String [] mSelectionArgs = {mSearchString};

    ListView listView2;

    Long mContactId;

    int hasPhoneNo;

    String mContactKey;

    String mContactName;

    String mContactNumber;

    Uri mContactUri;

    private SimpleCursorAdapter simpleCursorAdapter;

    public ContactsFragment(){

    }

    public static ContactsFragment getInstance(String string){

        ContactsFragment fragment = new ContactsFragment();

        Bundle args = new Bundle();
        args.putString(SEARCH_STRING, string);
        fragment.setArguments(args);

        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        if(bundle!=null){
            mSearchString = bundle.getString(SEARCH_STRING);
        }

        return inflater.inflate(R.layout.contacts_fragment,
                 container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Inside onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        listView2 = (ListView) getActivity().findViewById(R.id.listView2);

        simpleCursorAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.contacts_list_item,
                null,
                FROM_COLUMNS, TO_IDS,
                0
        );

        listView2.setAdapter(simpleCursorAdapter);

        listView2.setOnItemClickListener(this);

        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Log.d(LOG_TAG, "Inside onCreateLoader");

        mSelectionArgs[0] = "%" + mSearchString + "%";

        return new CursorLoader(
                getActivity(),
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PROJECTION,
                SELECTION,
                mSelectionArgs,
                null
        );


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        simpleCursorAdapter.swapCursor(data);

        listView2.setAdapter(simpleCursorAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        Log.d(LOG_TAG, "Inside onLoaderReset");
        simpleCursorAdapter.swapCursor(null);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Cursor cursor = simpleCursorAdapter.getCursor();

        cursor.moveToPosition(position);

        mContactId = cursor.getLong(CONTACT_ID_INDEX);
        mContactKey = cursor.getString(LOOKUP_KEY_INDEX);
        hasPhoneNo = cursor.getInt(HAS_PHONE_NUMBER);
        mContactName = cursor.getString(DISPLAY_NAME);
        mContactNumber = cursor.getString(NUMBER);

        SharedPreferences preferences = getActivity().getSharedPreferences(
                EMERGENCY_CONTACTS, Context.MODE_PRIVATE
        );

        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(new Long(mContactId).toString(), mContactNumber);

        editor.commit();

        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();

    }

    public void setmSearchString(String string){

        Log.d(LOG_TAG, "Inside setmSearchString");

        mSearchString = string;


    }
}
