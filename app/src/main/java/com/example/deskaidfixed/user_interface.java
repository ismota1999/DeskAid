package com.example.deskaidfixed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.deskaidfixed.databinding.FragmentHomeBinding;
import com.example.deskaidfixed.ui.home.HomeViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class user_interface extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interface);
    }


    //Just inflating the menu with options for the user to go to
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Define the intents for each button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.signout_user){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(user_interface.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        if (id == R.id.create_ticket){
            Intent intent = new Intent(user_interface.this, Ticket_form.class);
            startActivity(intent);
            finish();
        }
        if(id == R.id.dashboard_user){
            Intent intent = new Intent(user_interface.this, Dashboard.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void NewTicket(View vi){
        Intent intent = new Intent(user_interface.this, Ticket_form.class);
        startActivity(intent);
        finish();

    }
}