package com.example.deskaidfixed;

import static com.example.deskaidfixed.MainActivity.emailFinal;
import static com.example.deskaidfixed.MainActivity.passwordFinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class Email extends AppCompatActivity {
    private TextView mEditTextTo;
    private TextView mEditTextSubject;
    private TextView mEditTextMessage;
    private String email, subject, tickAnswer, ticketID;
    private FirebaseFirestore fStore;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        mEditTextTo=findViewById(R.id.edit_text_to);
        mEditTextSubject=findViewById(R.id.edit_text_subject);
        mEditTextMessage=findViewById(R.id.edit_text_message);
        btnBack = findViewById(R.id.button_back);
        fStore = FirebaseFirestore.getInstance();


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                email= null;
                subject = null;
                tickAnswer = null;
                ticketID = null;
            } else {
                email = extras.getString("email");
                subject = extras.getString("subject");
                tickAnswer = extras.getString("answer");
                ticketID = extras.getString("ID");
            }
        } else {
            email = (String) savedInstanceState.getSerializable("email");
            subject = (String) savedInstanceState.getSerializable("subject");
            tickAnswer = (String) savedInstanceState.getSerializable("answer");
            ticketID = (String) savedInstanceState.getSerializable("ID");
        }
        mEditTextTo.setText(email);
        mEditTextSubject.setText(subject);
        mEditTextMessage.setText(tickAnswer);


        Button buttonSend = findViewById(R.id.button_send);
        buttonSend.setOnClickListener(v -> {
            sendMail();
            update();
        });



        btnBack.setOnClickListener(v -> {
            if (emailFinal.equals("jflorim79@gmail.com") && passwordFinal.equals("12345678")) {
                Intent intent = new Intent(Email.this, AdminView.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(Email.this, user_interface.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void sendMail() {
        Intent intent= new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,tickAnswer);
        intent.putExtra(Intent.EXTRA_EMAIL, email);

        intent.setType("message/rxf822");
        startActivity(Intent.createChooser(intent,"Choose an email client"));


    }

    private void update(){
        DocumentReference documentReference = fStore.collection("Tickets").document(ticketID);

        String answer = mEditTextMessage.getText().toString();
        Map<String, Object> updateAnswer = new HashMap<>();
        updateAnswer.put("ticket_answer", answer);
        updateAnswer.put("state", States.SOLVED.toString());

        documentReference.set(updateAnswer, SetOptions.merge());
    }


}
