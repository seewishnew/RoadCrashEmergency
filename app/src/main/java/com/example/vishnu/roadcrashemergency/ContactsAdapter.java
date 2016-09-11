package com.example.vishnu.roadcrashemergency;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by vishnu on 10/9/16.
 */
public class ContactsAdapter extends ArrayAdapter {

    Context context;
    List<Contact> contacts;

    public ContactsAdapter(Context context, int resource, List<Contact> contacts) {
        super(context, resource);

        this.context = context;
        this.contacts = contacts;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.contacts_list_item, null);

        TextView text1 = (TextView) view.findViewById(android.R.id.text1);

        text1.setText(contacts.get(position).getName());

        return view;
    }
}
