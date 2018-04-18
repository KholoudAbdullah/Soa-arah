package com.soa_arah;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
//import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
//import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
//import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProgressChart extends AppCompatActivity {
    private PieChart pieChart;
    private DatabaseReference databaseReference;
    private String[] yData=new String[7] ;
    private float[] Data=new float[7] ;
    private TextView textView;
    private String[] xData = {"اليوم الأول", "اليوم الثاني" , "اليوم الثالث" , "اليوم الرابع", "اليوم الخامس", "اليوم السادس", "اليوم السابع"};
    private FirebaseAuth firebaseAuth;
    String User_ID,username;
    private Legend legend;
    private PieDataSet pieDataSet;
    //private LegendEntry entry;
    private ArrayList<String> xEntrys;
    //private ArrayList<PieEntry> yEntrys;
   // private  ArrayList<LegendEntry> entries;
    private  ArrayList<Integer> colors;
   // private Description Des;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );
        setContentView(R.layout.activity_progress_chart);

        isConnected();

        pieChart=(PieChart)findViewById(R.id.chart);
        firebaseAuth = FirebaseAuth.getInstance();
        User_ID = firebaseAuth.getCurrentUser().getEmail();
        username= User_ID.substring( 0, User_ID.lastIndexOf( "@" ) );
       /* Des=new Description();
        Des.setText("السعرات الحرارية للأيام الاسبوع");
        pieChart.setDescription(Des);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("السعرات الحرارية للأيام الاسبوع");
        pieChart.setCenterTextSize(10);


        yEntrys = new ArrayList<>();
        xEntrys = new ArrayList<>();



        databaseReference= FirebaseDatabase.getInstance().getReference().child("Progress");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                yData[0]=dataSnapshot.child(username).child("day1").getValue(String.class);
                yData[1]=dataSnapshot.child(username).child("day2").getValue(String.class);
                yData[2]=dataSnapshot.child(username).child("day3").getValue(String.class);
                yData[3]=dataSnapshot.child(username).child("day4").getValue(String.class);
                yData[4]=dataSnapshot.child(username).child("day5").getValue(String.class);
                yData[5]=dataSnapshot.child(username).child("day6").getValue(String.class);
                yData[6]=dataSnapshot.child(username).child("day7").getValue(String.class);

                for (int j=0;j<7;j++){
                    Data[j]=Float.parseFloat(yData[j]);
                }
                for (int i = 0; i < yData.length; i++) {
                    if(!yData[i].equals("0"))
                    yEntrys.add(new PieEntry(Data[i], i));
                }

                for (int i = 0; i < xData.length; i++) {
                    if(!yData[i].equals("0"))
                        xEntrys.add(xData[i]);
                }

                colors = new ArrayList<>();
                colors.add(getResources().getColor(R.color.a));
                colors.add(getResources().getColor(R.color.b));
                colors.add(getResources().getColor(R.color.c));
                colors.add(getResources().getColor(R.color.d));
                colors.add(getResources().getColor(R.color.e));
                colors.add(getResources().getColor(R.color.f));
                colors.add(getResources().getColor(R.color.g));

                pieDataSet = new PieDataSet(yEntrys, "Employee Sales");
                pieDataSet.setSliceSpace(2);
                pieDataSet.setValueTextSize(12);
                pieDataSet.setColors(colors);
                legend = pieChart.getLegend();
                legend.setEnabled(true);
                legend.setForm(Legend.LegendForm.CIRCLE);
                legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
                entries = new ArrayList<>();
                for (int i = 0; i < xEntrys.size(); i++) {
                    entry = new LegendEntry();
                    entry.formColor = colors.get(i);
                    entry.label = xEntrys.get(i);
                    entries.add(entry);
                }

                legend.setCustom(entries);        //create pie data object
                PieData pieData = new PieData(pieDataSet);
                pieChart.setData(pieData);
                pieChart.invalidate();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        //addDataSet();

    }
    private void addDataSet() {



    }

    public boolean isConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiInfo != null && wifiInfo.isConnected()) || (mobileInfo != null && mobileInfo.isConnected())) {
            return true;
        } else {
            showDialog();
            return false;
        }
    }




    private void showDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(" عذراً انت غير متصل بالانترنت هل تريد الاتصال بالانترنت او الاغلاق؟")
                .setCancelable(false)
                .setPositiveButton("الاتصال", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("إغلاق", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
