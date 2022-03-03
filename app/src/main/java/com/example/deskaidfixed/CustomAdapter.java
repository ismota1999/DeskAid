package com.example.deskaidfixed;

import static com.example.deskaidfixed.Registo.TAG;
import static com.example.deskaidfixed.tickets2.documentID;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<Tickets>{

    public CustomAdapter(Context context, List<Tickets> object){
        super(context,0, object);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView =  ((Activity)getContext()).getLayoutInflater().inflate(R.layout.ticket_item,parent,false);
        }

        TextView subjecttext = convertView.findViewById(R.id.subject);
        TextView statetext = convertView.findViewById(R.id.state);
        TextView firstName = convertView.findViewById(R.id.fname);
        TextView lastname = convertView.findViewById(R.id.lname);
        TextView priority = convertView.findViewById(R.id.priority);
        TextView email = convertView.findViewById(R.id.email);
        TextView ticketDesc = convertView.findViewById(R.id.tickDesc);
        TextView ticketID = convertView.findViewById(R.id.ticketID);

        Tickets tickets = getItem(position);

        subjecttext.setText(tickets.getSubject());
        statetext.setText(tickets.getState());
        firstName.setText(tickets.getFirstname());
        lastname.setText(tickets.getLastname());
        priority.setText(tickets.getPriority());
        email.setText(tickets.getEmail());
        ticketDesc.setText(tickets.getTicketDesc());
        ticketID.setText(tickets.ids.get(0));



        return convertView;
    }

}