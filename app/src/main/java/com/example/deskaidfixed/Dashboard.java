package com.example.deskaidfixed;

import static com.example.deskaidfixed.MainActivity.emailFinal;
import static com.example.deskaidfixed.MainActivity.passwordFinal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;
import androidx.fragment.app.FragmentContainerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {

    private FirebaseFirestore fStore;
    private TextView countUrgent, countUnsolved, countSolved;
    private ImageView returnAdmin;
    private FragmentContainerView solved, unsolved, urgent;
    private int delayTime = 5000;
    private View pieChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        countUnsolved = findViewById(R.id.countNew);
        countSolved = findViewById(R.id.count_solved);
        countUrgent = findViewById(R.id.countUrgente);
        pieChart = findViewById(R.id.PieChart);

        fStore = FirebaseFirestore.getInstance();

        Context other = this;


        solved = findViewById(R.id.btn_solved1);
        unsolved = findViewById(R.id.btn_new);
        urgent = findViewById(R.id.btn_urgent2);
        returnAdmin = findViewById(R.id.retrnAdmin3);

        getCount(States.SOLVED, count -> countSolved.setText(count));
        getCount(States.UNSOLVED, count -> countUnsolved.setText(count));
        getCountUrgent(count -> countUrgent.setText(count));


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mostraGrafico();
            }
        },delayTime);


        returnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (emailFinal.equals("jflorim79@gmail.com") && passwordFinal.equals("12345678")) {
                    Intent intent = new Intent(Dashboard.this, AdminView.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(Dashboard.this, user_interface.class);
                    startActivity(intent);
                    finish();

                }
            }
        });

        solved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String statusSolved = "Solved";
                Intent intent = new Intent(Dashboard.this, tickets2.class);
                intent.putExtra("Status", statusSolved);
                startActivity(intent);
                finish();
            }
        });

        unsolved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String statusUnsolved = "Unsolved";
                Intent intent = new Intent(Dashboard.this, tickets2.class);
                intent.putExtra("Status", statusUnsolved);
                startActivity(intent);
                finish();
            }
        });

        urgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urgent = "High";
                Intent intent = new Intent(Dashboard.this, tickets2.class);
                intent.putExtra("Status", urgent);
                startActivity(intent);
                finish();
            }
        });

    }

    private void getCount(States states, Consumer<String> consumer) {
        fStore.collection("Tickets")
                .whereEqualTo("state", states.toString())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        consumer.accept(Integer.toString(task.getResult().size()));
                    } else {
                        consumer.accept("N/A");
                    }
                });
    }

    private void getCountUrgent(Consumer<String> consumer) {
        fStore.collection("Tickets")
                .whereEqualTo("priority", "High")
                .whereEqualTo("state", States.UNSOLVED.toString())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        consumer.accept(Integer.toString((task.getResult().size())));
                    } else {
                        consumer.accept("N/A");
                    }

                });
    }






        public void mostraGrafico(){

            PieChart pieChart = findViewById(R.id.PieChart);

            int newSolved = Integer.parseInt(countSolved.getText().toString());
            int newUnsolv = Integer.parseInt(countUnsolved.getText().toString());
            int newUrg = Integer.parseInt(countUrgent.getText().toString());


            ArrayList<PieEntry> visitors = new ArrayList<>();
            visitors.add(new PieEntry(newSolved,"Solved"));
            visitors.add(new PieEntry(newUnsolv,"Unsolved"));
            visitors.add(new PieEntry(newUrg,"Ongoing"));

            PieDataSet pieDataSet = new PieDataSet(visitors, "");
            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            pieDataSet.setValueFormatter(new PercentFormatter(pieChart));
            //pieDataSet.setValueTextColor(Color.BLACK);
            pieDataSet.setValueTextSize(0f);
            pieDataSet.setDrawValues(false);

            PieData pieData = new PieData(pieDataSet);

            pieChart.setData(pieData);
            pieChart.setEntryLabelTextSize(0f);
            //pieChart.setEntryLabelColor(Color.BLACK);
            //pieChart.getDescription().setEnabled(false);
            pieChart.setCenterText("Ticket");
            pieChart.setCenterTextColor(Color.BLACK);
            pieChart.animate();

            pieChart.notifyDataSetChanged();
            pieChart.invalidate();

        }


}