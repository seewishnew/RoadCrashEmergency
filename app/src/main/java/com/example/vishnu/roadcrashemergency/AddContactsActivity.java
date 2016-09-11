package com.example.vishnu.roadcrashemergency;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import java.util.zip.Inflater;

public class AddContactsActivity extends AppCompatActivity {

    public static final String LOG_TAG = "AddContactsActivity";
    MenuItem item;
    private boolean isSearchOpen = false;
    AutoCompleteTextView search;
    ContactsFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragment = new ContactsFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.relativeLayoutContainer, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);

        item = menu.findItem(R.id.search);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.search:

                ActionBar actionBar = getSupportActionBar();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                if(isSearchOpen){

                    actionBar.setDisplayShowCustomEnabled(false);
                    actionBar.setDisplayShowTitleEnabled(true);

                    imm.hideSoftInputFromWindow(search.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);

                    item.setIcon(R.drawable.ic_search);

                    isSearchOpen = false;
                }

                else{

                    actionBar.setDisplayShowTitleEnabled(false);
                    actionBar.setDisplayShowCustomEnabled(true);
                    actionBar.setCustomView(R.layout.searchbar);

                    search = (AutoCompleteTextView) actionBar.getCustomView().findViewById(R.id.autoCompleteTextView);

                    search.requestFocus();

                    imm.showSoftInput(search, InputMethodManager.SHOW_IMPLICIT);

                    item.setIcon(R.drawable.ic_close);

                    isSearchOpen = true;

                    search.addTextChangedListener(
                            new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    Log.d(LOG_TAG, "text changed!");

                                    fragment = ContactsFragment.getInstance(s.toString());

                                    getSupportFragmentManager().beginTransaction()
                                            .add(R.id.relativeLayoutContainer, fragment)
                                            .commit();



                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            }
                    );
                }

                break;

        }



        return super.onOptionsItemSelected(item);
    }
}
