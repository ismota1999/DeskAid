package com.example.deskaidfixed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Ticket extends AppCompatActivity {

    private TextView id;
    private FirebaseFirestore fstore;
    private TextView emailBD, tagBD, priorityBD, ticketDesc, tituloTick, tickAttend;
    private EditText tickAnswer;
    private FirebaseAuth mAuth;
    private String userID, stringId;
    private String firstName, lastName;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        fstore = FirebaseFirestore.getInstance();
        emailBD = findViewById(R.id.emailAnswer);
        tagBD = findViewById(R.id.subjectAnswer);
        priorityBD = findViewById(R.id.TagsAnswer);
        ticketDesc = findViewById(R.id.tickDescr);
        tituloTick = findViewById(R.id.tituloTicket);
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        tickAttend = findViewById(R.id.ticketAttendant);
        tickAnswer = findViewById(R.id.textView26);
        button = findViewById(R.id.button7);

        id = findViewById(R.id.ticketAttendant);
        String stringID;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                stringID = null;
            } else {
                stringID = extras.getString("IDS");
            }
        } else {
            stringID = (String) savedInstanceState.getSerializable("IDS");
        }

        System.out.println(stringID);

        stringId = stringID;

        DocumentReference userReference = fstore.collection("Users").document(userID);

        userReference.addSnapshotListener(this, (value, error) -> {

            firstName = value.getString("firstName");
            lastName = value.getString("lastName");
            tickAttend.setText(firstName + " " + lastName);

        });


        DocumentReference documentReference = fstore.collection("Tickets").document(stringID);

        documentReference.addSnapshotListener(this, (value, error) -> {

            emailBD.setText(value.getString("email"));
            tagBD.setText(value.getString("tag"));
            priorityBD.setText(value.getString("priority"));
            ticketDesc.setText(value.getString("ticketDesc"));
            tituloTick.setText(value.getString("subject"));
        });

        button.setOnClickListener(view -> {
            Intent intent = new Intent(Ticket.this, Email.class);

            String email = emailBD.getText().toString().trim();
            String subject = tituloTick.getText().toString().trim();
            String answerTick = tickAnswer.getText().toString().trim();

            System.out.println(email);
            System.out.println(subject);
            System.out.println(answerTick);


            intent.putExtra("email", email);
            intent.putExtra("subject", subject);
            intent.putExtra("answer", answerTick);
            intent.putExtra("ID", stringId);


            startActivity(intent);
            finish();

        });

    }
}



