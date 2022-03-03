package com.example.deskaidfixed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //After our splash we create our login page

    //First step we declare all of our elements that we used in our activity.xml
    private String emailteste, passwordteste;
    private EditText password;
    private CheckBox showpassword;
    private Button loginBtn;
    private EditText emailLog;
    private TextView forgotPassword;
    private CheckBox rememberMe;
    public static String emailFinal, passwordFinal;

    //Variables that i used to save the user login info if the user wished so
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private boolean saveLogin;

    //Just the FireBase Authentication variable
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private DocumentReference documentReference;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().hide();

        //Here we just connect our variables that we created upwards to the actual elements
        password = findViewById(R.id.passwordLogin);
        showpassword= findViewById(R.id.showPassword);
        loginBtn = findViewById(R.id.loginButton);
        loginBtn.setOnClickListener(this);
        emailLog = findViewById(R.id.emailLogin);
        forgotPassword = findViewById(R.id.fgPasswordtxt);
        rememberMe = findViewById(R.id.remember_me);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        //Here is the code to program our remember me feature
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if(saveLogin == true){
            emailLog.setText(loginPreferences.getString("email", ""));
            password.setText(loginPreferences.getString("password", ""));
            rememberMe.setChecked(true);
        }



        //Feature to reset our password if needed
        forgotPassword.setOnClickListener(view -> {
            EditText resetMail = new EditText(view.getContext());
            //Just creating an alert dialog pop pup to show questioning if we want to reset your password or not
            AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
            passwordResetDialog.setTitle("Reset Password");
            passwordResetDialog.setMessage("Enter your email to receive the password reset link");
            passwordResetDialog.setView(resetMail);
            passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //extrair o email e enviar link de reset
                    String mail = resetMail.getText().toString().trim();
                    mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, "Password reset link sent successfully", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Password reset link failed to be sent" + e.getMessage(), Toast.LENGTH_LONG).show());
                }
            });

            passwordResetDialog.setNegativeButton("No", (dialogInterface, i) -> {
                //fechar o dialog
            });
            passwordResetDialog.create().show();
        });

        //If we wanna see what we're writing in the password field this method allows us to check a checkbox and see what's in the field
        showpassword.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }else{
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });


    }


    //Now the connection to the firebase part. When we click the login button, here's what will happen.
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton:
                //This will hide the soft keyboard once the login button is pressed
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(emailLog.getWindowToken(), 0);

                //This will just get what's written in the email and password fields
                emailteste = emailLog.getText().toString().trim();
                passwordteste = password.getText().toString().trim();

                //If the box *remember me* is checked it will save the user details and input them automatically once he opens the app again
                if (rememberMe.isChecked()){
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("email", emailteste);
                    loginPrefsEditor.putString("password", passwordteste);
                    loginPrefsEditor.commit();
                }
                else{
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }

                //after all of this i created a method for the user login and invoked it here
                userLogin();
                break;
        }

    }

    private void userLogin() {

        String emailLogin = emailteste;
        String passLogin = passwordteste;

        documentReference = fStore.collection("Users").document("oaziabHS95OlfkS2XJQCXKOBczX2");



        //Saving the details in a global variable just in case
        emailFinal = emailLogin;
        passwordFinal = passLogin;

            //This will allow us to use the FireBase Authentication with our login mail and password
            mAuth.signInWithEmailAndPassword(emailLogin, passLogin).addOnCompleteListener(task -> {

                if (task.isSuccessful()) {
                    //If everything goes right we get the user that is currently logged in and then we enter another set of ifs to control our priorities
                    FirebaseUser user = mAuth.getCurrentUser();
                    String userID = mAuth.getCurrentUser().getUid();

                    //We decided that this app had to have an admin that could for example register clients or other members, the other regular users couldn't
                    if(userID.equals(documentReference.getId()))
                    {
                        //So if the current user logging in had the same uid as the admin account UID it would in this case redirect the admin to his special page
                        Intent intent = new Intent(MainActivity.this, AdminView.class);
                        startActivity(intent);
                        finish();
                    }
                    //If the user has it's email verified and is logging with the admin account it will send him aswell to the admin activity
                    else if (user.isEmailVerified() && userID.equals(documentReference.getId())){
                        startActivity(new Intent(MainActivity.this, AdminView.class));
                    }
                    //if the user is verified but it's just a normal user it will send him to the regular user activity
                    else if (user.isEmailVerified()) {
                        startActivity(new Intent(MainActivity.this, user_interface.class));
                    } else {
                        //if not it will tell him to check his email to confirm his account and will not let him proceed
                        Toast.makeText(MainActivity.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    //if the user doesn't exist or the password or email is wrong it will give out a warning aswell
                    Toast.makeText(MainActivity.this, "Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
                }




            });
        }

    }
