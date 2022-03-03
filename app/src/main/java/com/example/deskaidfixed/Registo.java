package com.example.deskaidfixed;

import static com.example.deskaidfixed.MainActivity.emailFinal;
import static com.example.deskaidfixed.MainActivity.passwordFinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class Registo extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "TAG";
    // defining our own password pattern
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{4,}" +                // at least 4 characters
                    "$");
    private EditText firstname, lastname, email, password, passwordConfirm;
    private Button regisBtn;
    private ImageView returnAdmin;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();;
    private DocumentReference adminReference = fStore.collection("Users").document("oaziabHS95OlfkS2XJQCXKOBczX2");
    private FirebaseUser user = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registo);

        returnAdmin = findViewById(R.id.retrnAdmin2);
        regisBtn = findViewById(R.id.btnRegister);
        regisBtn.setOnClickListener(this);
        firstname = findViewById(R.id.firstName);
        lastname = findViewById(R.id.lastName);
        email = findViewById(R.id.emailRegister);
        password = findViewById(R.id.passwordRegister);
        passwordConfirm = findViewById(R.id.passwordConfirm);

        //This is our return button and i wanted to make this page dynamic and i didnt want to create 2 activities, one for the admin and one for the user
        //so if the user clicking the return button in this page was the admin it would return him to the admin activity again and if it was a regular user
        //it would send him to the regular user activity
        returnAdmin.setOnClickListener(v -> {
            if (user.getUid().equals(adminReference.getId())) {
                Intent intent = new Intent(Registo.this, AdminView.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(Registo.this, user_interface.class);
                startActivity(intent);
                finish();

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                registeruser();
                break;
        }
    }

    //This is our method to register our user
    private void registeruser() {

        //First we fetch all of the data that was inputted into our fields
        String fname = firstname.getText().toString().trim();
        String lname = lastname.getText().toString().trim();
        String bdemail = email.getText().toString().trim();
        String bdpass = password.getText().toString().trim();
        String bdpassconfirm = passwordConfirm.getText().toString().trim();

        //Then we validate all of the fields that we want to validate

        if (fname.isEmpty()) {
            firstname.setError("A name is required");
            firstname.requestFocus();
            return;
        }

        //Here we validate all of our fields so we can have a clean and safe register
        if (lname.isEmpty()) {
            lastname.setError("A last name is required");
            lastname.requestFocus();
            return;
        }

        if (bdemail.isEmpty()) {
            email.setError("Email is Required");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(bdemail).matches()){
            email.setError("Please enter a valid email");
            email.requestFocus();
            return;
        }

        if (!bdpass.equals(bdpassconfirm)) {
            password.setError("Please type matching passwords");
            password.requestFocus();
            return;
        }

        if (bdpass.length() < 8) {
            password.setError("Password must contain 8 or more characters");
            password.requestFocus();
            return;
        }

        if(!PASSWORD_PATTERN.matcher(bdpass).matches()){
            password.setError("Password is too weak");
            password.requestFocus();
            return;
        }

        //Method to creat the user in our database using his email and password
        mAuth.createUserWithEmailAndPassword(bdemail, bdpass).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                Toast.makeText(Registo.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                String userID = mAuth.getCurrentUser().getUid();
                //This is how we are going to put the user information into our FireStore but we don't pass the password due to security
                DocumentReference documentReference = fStore.collection("Users").document(userID);
                Map<String, Object> user = new HashMap<>();
                user.put("firstName", fname);
                user.put("lastName", lname);
                user.put("email", bdemail);

                documentReference.set(user).addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "On Success; User profile is created for " + userID);
                }).addOnFailureListener(failure -> {
                    Log.d(TAG, "FAILURE");
                });

                //This will detect the user that was registered it will get his uid, his email info and it will send him a verification email and it will redirect him to an activity showing his email
                //and that an email has been sent to it
                FirebaseUser userVeri = FirebaseAuth.getInstance().getCurrentUser();
                userVeri.sendEmailVerification();
                Intent intent = new Intent(this, EmailConfirmation.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(Registo.this, "Failed to register, try again!", Toast.LENGTH_LONG).show();
            }
        });


    }


}
