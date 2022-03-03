package com.example.deskaidfixed;

import static com.example.deskaidfixed.MainActivity.emailFinal;
import static com.example.deskaidfixed.MainActivity.passwordFinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Ticket_form extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ImageView returnAdmin;
    private Spinner spinnerTag;
    private Spinner spinnerPriority;
    public String text, text1;
    private EditText tickName, tickLastName, tickEmail, tickText, tickSubject;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    DocumentReference adminReference;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_form);

        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        returnAdmin = findViewById(R.id.retrnAdmin);
        tickName = findViewById(R.id.ticketName);
        tickLastName = findViewById(R.id.ticketLastName);
        tickEmail = findViewById(R.id.ticketEmail);
        tickText = findViewById(R.id.ticketText);
        tickSubject = findViewById(R.id.subjectForm);

        //Creating the tags spinner
        spinnerTag = findViewById(R.id.tag_spn);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Tag, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTag.setAdapter(adapter);
        spinnerTag.setOnItemSelectedListener(this);

        //Creating the priority spinner
        spinnerPriority = findViewById(R.id.priority_spn);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.Priority, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(adapter2);
        spinnerPriority.setOnItemSelectedListener(this);

        //This is our return button and i wanted to make this page dynamic and i didnt want to create 2 activities, one for the admin and one for the user
        //so if the user clicking the return button in this page was the admin it would return him to the admin activity again and if it was a regular user
        //it would send him to the regular user activity
        returnAdmin.setOnClickListener(v -> {
            adminReference = fStore.collection("Users").document("oaziabHS95OlfkS2XJQCXKOBczX2");
            user = mAuth.getCurrentUser();

            if (user.getUid().equals(adminReference.getId())) {
                Intent intent = new Intent(Ticket_form.this, AdminView.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(Ticket_form.this, user_interface.class);
                startActivity(intent);
                finish();

            }
        });
    }

    //Getting the chosen item in the spinner into a string so we can use it in the ticket form cause it's a part of its info and data
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        text = spinnerTag.getSelectedItem().toString().trim();
        text1 = spinnerPriority.getSelectedItem().toString().trim();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }

    //When we click the submit button it calls the submit ticket method
    public void onClick(View v) {
        submitTicket();
    }


    private void submitTicket(){
        String ticketName = tickName.getText().toString().trim();
        String ticketLast = tickLastName.getText().toString().trim();
        String ticketMail = tickEmail.getText().toString().trim();
        String ticketText = tickText.getText().toString().trim();
        String tag = text;
        String priority = text1;
        States state = States.UNSOLVED;
        String ticketSubject = tickSubject.getText().toString().trim();


        //Validations
        if(ticketName.isEmpty()){
            tickName.setError("Please enter a name");
            tickName.requestFocus();
            return;
        }

        if(ticketLast.isEmpty()){
            tickLastName.setError("Please enter a last name");
            tickLastName.requestFocus();
            return;
        }

        if(ticketMail.isEmpty()){
            tickEmail.setError("Please enter an email");
            tickEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(ticketMail).matches()) {
            tickEmail.setError("Please enter a valid email");
            tickEmail.requestFocus();
            return;
        }

        if(ticketText.isEmpty()){
            tickText.setError("Please enter a text describing the ticket");
            tickText.requestFocus();
            return;
        }


        //Now after we fetched all of the information we're going to send it to the FireStore
        CollectionReference fstoreTickets = fStore.collection("Tickets");

        Map<String, Object> ticket = new HashMap<>();
        ticket.put("firstname", ticketName);
        ticket.put("lastname", ticketLast);
        ticket.put("email", ticketMail);
        ticket.put("tag", tag);
        ticket.put("subject", ticketSubject);
        ticket.put("priority", priority);
        ticket.put("ticketDesc", ticketText);
        ticket.put("state", state.toString());

        fstoreTickets.add(ticket).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(getApplicationContext(), "Ticket Added", Toast.LENGTH_SHORT).show();
                //If the task is successful we clean all of our fields so the person that's submitting tickets can fill out all of the form again quickly
                tickName.setText("");
                tickLastName.setText("");
                tickEmail.setText("");
                tickText.setText("");
                tickSubject.setText("");
            }
            else{
                Toast.makeText(getApplicationContext(), "Error adding ticket", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onClickCancel(View view){
        tickName.setText("");
        tickLastName.setText("");
        tickEmail.setText("");
        tickText.setText("");
        tickSubject.setText("");
    }

}
