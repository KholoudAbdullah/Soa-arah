package com.soa_arah;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class chart extends AppCompatActivity {
    private BarChart barChart;
    private DatabaseReference databaseReference;
    private String[] yData = new String[7];
    private float[] Data = new float[7];
    private TextView textView;
    private String[] xData = {"اليوم الأول", "اليوم الثاني", "اليوم الثالث", "اليوم الرابع", "اليوم الخامس", "اليوم السادس", "اليوم السابع"};
    private FirebaseAuth firebaseAuth;
    String User_ID, username;
    private Legend legend;
    private PieDataSet pieDataSet;
    private ArrayList<String> xEntrys;
    private ArrayList<BarEntry> yEntrys;
    private ArrayList<Integer> colors;
     BarDataSet barDataSet;
    ArrayList<BarDataSet> barDataSets = null;

    ArrayList<String> dates;
    Random random;
    ArrayList<BarEntry> barEntries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        barChart = (BarChart) findViewById(R.id.chart);


        firebaseAuth = FirebaseAuth.getInstance();
        User_ID = firebaseAuth.getCurrentUser().getEmail();
        username = User_ID.substring(0, User_ID.lastIndexOf("@"));
        yEntrys = new ArrayList<>();
       databaseReference = FirebaseDatabase.getInstance().getReference().child("Progress");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                yData[0] = dataSnapshot.child(username).child("day1").getValue(String.class);
                yData[1] = dataSnapshot.child(username).child("day2").getValue(String.class);
                yData[2] = dataSnapshot.child(username).child("day3").getValue(String.class);
                yData[3] = dataSnapshot.child(username).child("day4").getValue(String.class);
                yData[4] = dataSnapshot.child(username).child("day5").getValue(String.class);
                yData[5] = dataSnapshot.child(username).child("day6").getValue(String.class);
                yData[6] = dataSnapshot.child(username).child("day7").getValue(String.class);

                for (int j = 0; j < 7; j++) {
                    //if (!yData[j].equals("0"))
                        Data[j] = Float.parseFloat(yData[j]);
                }
                for (int i = 0; i < yData.length; i++) {
                   // if (!yData[i].equals("0"))
                        yEntrys.add(new BarEntry(Data[i], i));

                }
                BarDataSet barDataSet = new BarDataSet(yEntrys, "calories");
                barDataSet.setColor(getResources().getColor(R.color.colorPrimary));
                xEntrys = new ArrayList<>();
                for (int i = 0; i < xData.length; i++) {
                    //if (!yData[i].equals("0"))
                        xEntrys.add(xData[i]);}
                BarData barData = new BarData(xEntrys,barDataSet);
                barChart.setData(barData);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private ArrayList<BarDataSet> getDataSet(){
        firebaseAuth = FirebaseAuth.getInstance();
        User_ID = firebaseAuth.getCurrentUser().getEmail();
        username = User_ID.substring(0, User_ID.lastIndexOf("@"));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Progress");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                yData[0] = dataSnapshot.child(username).child("day1").getValue(String.class);
                yData[1] = dataSnapshot.child(username).child("day2").getValue(String.class);
                yData[2] = dataSnapshot.child(username).child("day3").getValue(String.class);
                yData[3] = dataSnapshot.child(username).child("day4").getValue(String.class);
                yData[4] = dataSnapshot.child(username).child("day5").getValue(String.class);
                yData[5] = dataSnapshot.child(username).child("day6").getValue(String.class);
                yData[6] = dataSnapshot.child(username).child("day7").getValue(String.class);

                for (int j = 0; j < 7; j++) {
                    Data[j] = Float.parseFloat(yData[j]);
                }
                for (int i = 0; i < yData.length; i++) {
                    if (!yData[i].equals("0"))
                        yEntrys.add(new BarEntry(Data[i], i));

                }
                barDataSets = new ArrayList<>();
                BarDataSet barDataSet = new BarDataSet(yEntrys, "calories");
                barDataSets.add(barDataSet);
                ArrayList<String> xAxis = new ArrayList<>();
                ArrayList<String> xEntrys = new ArrayList<>();
                for (int i = 0; i < xData.length; i++) {
                    if (!yData[i].equals("0"))
                        xEntrys.add(xData[i]);}


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return barDataSets;
    }

}
