package com.example.deskaidfixed;

import static com.example.deskaidfixed.MainActivity.emailFinal;
import static com.example.deskaidfixed.MainActivity.passwordFinal;
import static com.example.deskaidfixed.Registo.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deskaidfixed.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class tickets2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static String documentID;
    FirebaseFirestore fStore;
    private Spinner spinner_State;
    private Button showTicks;
    private ListView listView;
    private CustomAdapter ticketAdapter;
    private ArrayList<Tickets> mTicketsList;
    private ImageView closeBtn;
    private Context other;
    private TextView STATE;
    private String statusTicks;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets2);

        fStore = FirebaseFirestore.getInstance();
        showTicks = findViewById(R.id.btn_ShowTickets);
        listView = findViewById(R.id.listview);
        closeBtn = findViewById(R.id.btn_close);
        STATE = findViewById(R.id.state);


        spinner_State = findViewById(R.id.spinner_states);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.States, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_State.setAdapter(adapter);
        spinner_State.setOnItemSelectedListener(this);

        mTicketsList = new ArrayList<Tickets>();
        ticketAdapter = new CustomAdapter(this, mTicketsList);
        listView.setAdapter(ticketAdapter);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                statusTicks= null;
            } else {
                statusTicks = extras.getString("Status");
            }
        } else {
            statusTicks = (String) savedInstanceState.getSerializable("Status");
        }


        other = this;

        fazTabela(statusTicks);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textview = (TextView) view.findViewById(R.id.state);
                String text1 = textview.getText().toString().trim();

                if(text1.equals(States.SOLVED.toString())) {

                }
                else {
                    TextView textView = (TextView) view.findViewById(R.id.ticketID);
                    String text = textView.getText().toString().trim();
                    Intent intent = new Intent(tickets2.this, Ticket.class);
                    String docsIDS = text;
                    intent.putExtra("IDS", docsIDS);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        String text = parent.getItemAtPosition(i).toString();


        showTicks.setOnClickListener(view1 -> fStore.collection("Tickets")
                .whereEqualTo("state", text)
                .get()
                .addOnCompleteListener(task -> {
                    List<Tickets> mTicketsList = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + "=>" + document.getData());
                            Tickets tickets = document.toObject(Tickets.class);
                            mTicketsList.add(tickets);
                            tickets.ids.add(document.getId());
                        }

                        ListView mTicketsListView = findViewById(R.id.listview);
                        CustomAdapter mTicketAdapter = new CustomAdapter(other, mTicketsList);
                        mTicketsListView.setAdapter(mTicketAdapter);

                        Toast.makeText(getApplicationContext(), "Loaded Documents", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "Error getting documents", task.getException());
                    }
                }));
        closeBtn.setOnClickListener(v -> {


            if (emailFinal.equals("jflorim79@gmail.com") && passwordFinal.equals("12345678")) {
                Intent intent = new Intent(tickets2.this, HomeFragment.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(tickets2.this, user_interface.class);
                startActivity(intent);
                finish();

            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void fazTabela (String status) {
        if (statusTicks.equals("High")) {
            fStore.collection("Tickets")
                    .whereEqualTo("priority", status)
                    .whereEqualTo("state", States.UNSOLVED.toString())
                    .get()
                    .addOnCompleteListener(task -> {
                        List<Tickets> mTicketsList = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + "=>" + document.getData());
                                Tickets tickets = document.toObject(Tickets.class);
                                mTicketsList.add(tickets);
                                tickets.ids.add(document.getId());
                            }

                            ListView mTicketsListView = findViewById(R.id.listview);
                            CustomAdapter mTicketAdapter = new CustomAdapter(other, mTicketsList);
                            mTicketsListView.setAdapter(mTicketAdapter);

                            Toast.makeText(getApplicationContext(), "Loaded Documents", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "Error getting documents", task.getException());
                        }
                    });
        } else {
            fStore.collection("Tickets")
                    .whereEqualTo("state", status)
                    .get()
                    .addOnCompleteListener(task -> {
                        List<Tickets> mTicketsList = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + "=>" + document.getData());
                                Tickets tickets = document.toObject(Tickets.class);
                                mTicketsList.add(tickets);
                                tickets.ids.add(document.getId());
                            }

                            ListView mTicketsListView = findViewById(R.id.listview);
                            CustomAdapter mTicketAdapter = new CustomAdapter(other, mTicketsList);
                            mTicketsListView.setAdapter(mTicketAdapter);

                            Toast.makeText(getApplicationContext(), "Loaded Documents", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "Error getting documents", task.getException());
                        }
                    });
        }
    }
        }












