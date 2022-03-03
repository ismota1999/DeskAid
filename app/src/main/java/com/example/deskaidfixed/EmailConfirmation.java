package com.example.deskaidfixed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EmailConfirmation extends AppCompatActivity {

    private TextView emailDisplay;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    private String userID;
    private Button btnRetLogin;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_confirmation);

        emailDisplay = findViewById(R.id.emailConf);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        btnRetLogin = findViewById(R.id.btnRetrnLogin);

        userID = mAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("Users").document(userID);

        documentReference.addSnapshotListener(this, (value, error) -> {

            emailDisplay.setText(value.getString("email"));
        });

    }

    public void btnRetrnLogin(View v){
        Intent intent = new Intent(EmailConfirmation.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}