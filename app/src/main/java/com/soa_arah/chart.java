package com.soa_arah;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class chart extends AppCompatActivity {
    private BarChart barChart;
    private DatabaseReference databaseReference,databaseReference2;
    private String[] yData = new String[7];
    private String[] calory = new String[7];
    private float[] Data = new float[7];
    private TextView textView,textView2;
    private String[] xData = {"اليوم الأول", "اليوم الثاني", "اليوم الثالث", "اليوم الرابع", "اليوم الخامس", "اليوم السادس", "اليوم السابع"};
    private FirebaseAuth firebaseAuth;
    String User_ID, username;
    private ArrayList<String> xEntrys;
    private ArrayList<BarEntry> yEntrys;
    private String day,d;
    private String day1,day2,day3,day4,day5,day6,day7,day8;
    private Date date;
    private double cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        barChart = (BarChart) findViewById(R.id.chart);
        textView=(TextView)findViewById(R.id.textView20);
        textView2=(TextView)findViewById(R.id.textView29);
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
                BarDataSet barDataSet = new BarDataSet(yEntrys, "السعرات الحرارية");
                barDataSet.setColor(getResources().getColor(R.color.colorPrimary));
                xEntrys = new ArrayList<>();
                for (int i = 0; i < xData.length; i++) {
                    //if (!yData[i].equals("0"))
                        xEntrys.add(xData[i]);}
                BarData barData = new BarData(xEntrys,barDataSet);
                barChart.setData(barData);
                barChart.setDescription("السعرات الحراريه خلال ايام الأسبوع");
                barChart.setDescription("السعرات الحراريه خلال ايام الأسبوع");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        caloriesEndOfTheWeek();
    }
    private void caloriesEndOfTheWeek(){
        firebaseAuth = FirebaseAuth.getInstance();
        User_ID = firebaseAuth.getCurrentUser().getEmail();
        username = User_ID.substring(0, User_ID.lastIndexOf("@"));
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                d=dataSnapshot.child("Progress").child(username).child("startDate").getValue(String.class);
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                Calendar c=Calendar.getInstance();
                try{
                    date=Calendar.getInstance().getTime();
                    day = df.format(date);
                    c.setTime(date);
                    c.setTime(df.parse(d));
                    day1 = df.format(c.getTime());  // dt is now the new date

                    c.setTime(df.parse(d));
                    c.add(Calendar.DATE, 1);  // number of days to add
                    day2 = df.format(c.getTime());

                    c.setTime(df.parse(d));
                    c.add(Calendar.DATE, 2);  // number of days to add
                    day3 = df.format(c.getTime());

                    c.setTime(df.parse(d));
                    c.add(Calendar.DATE, 3);  // number of days to add
                    day4 = df.format(c.getTime());

                    c.setTime(df.parse(d));
                    c.add(Calendar.DATE, 4);  // number of days to add
                    day5 = df.format(c.getTime());

                    c.setTime(df.parse(d));
                    c.add(Calendar.DATE, 5);  // number of days to add
                    day6 = df.format(c.getTime());

                    c.setTime(df.parse(d));
                    c.add(Calendar.DATE, 6);  // number of days to add
                    day7 = df.format(c.getTime());

                    c.setTime(df.parse(d));
                    c.add(Calendar.DATE, 8);  // number of days to add
                    day8 = df.format(c.getTime());
                }
                catch (Exception e){}
                if(day.equals(day1)){
                    textView.setText("اليوم هو اليوم الأول من الخطه الغذائية");
                }
                else if(day.equals(day2)){
                    textView.setText("اليوم هو اليوم الثاني من الخطه الغذائية");
                }else if(day.equals(day3)){
                    textView.setText("اليوم هو اليوم الثالث من الخطه الغذائية");
                }else if(day.equals(day4)){
                    textView.setText("اليوم هو اليوم الرابع من الخطه الغذائية");
                }
                else if(day.equals(day5)){
                    textView.setText("اليوم هو اليوم الخامس من الخطه الغذائية");
                }
                else if(day.equals(day6)){
                    textView.setText("اليوم هو اليوم السادس من الخطه الغذائية");
                }
                else if(day.equals(day7)) {
                    textView.setText("اليوم هو اليوم السابع من الخطه الغذائية");
                    calory[0] = dataSnapshot.child("Progress").child(username).child("day1").getValue(String.class);
                    calory[1] = dataSnapshot.child("Progress").child(username).child("day2").getValue(String.class);
                    calory[2] = dataSnapshot.child("Progress").child(username).child("day3").getValue(String.class);
                    calory[3] = dataSnapshot.child("Progress").child(username).child("day4").getValue(String.class);
                    calory[4] = dataSnapshot.child("Progress").child(username).child("day5").getValue(String.class);
                    calory[5] = dataSnapshot.child("Progress").child(username).child("day6").getValue(String.class);
                    calory[6] = dataSnapshot.child("Progress").child(username).child("day7").getValue(String.class);
                    double max=Double.parseDouble(dataSnapshot.child("DietPlan").child(username).child("max").getValue(String.class));
                    cal=Double.parseDouble(calory[0])+Double.parseDouble(calory[1])+Double.parseDouble(calory[2])+Double.parseDouble(calory[3])+Double.parseDouble(calory[4])+Double.parseDouble(calory[5])+Double.parseDouble(calory[6]);
                    max=max*7.0;
                    if(cal<max){
                        double calF=max-cal;
                        double kg=calF/7700;
                        DecimalFormat precision = new DecimalFormat("0.00");
                        textView2.setText("في نهاية هاذا الأسبوع من المفترض ان يقل وزنك بمقدار "+precision.format(kg)+" كيلو جرام");
                    }else if(cal==max){
                        textView2.setText("خلال هاذا الأسبوع من المفترض ان وزنك لا يتغير");
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.home_1, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.Home) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


            if (user != null) {
                String id = user.getUid();
                //IT admin
                if(id.equals("kstgUKiRA7T3p1NNl3GuGBHgvcf2")){
                    startActivity(new Intent(getApplicationContext(),home_page_IT_admin.class));
                }
                // Nutrition addmin
                else if(id.equals("Pf7emnnQTEbmukAIDwWgkuv8JbC2")){
                    startActivity(new Intent(getApplicationContext(),home_page_Nutrition_admin.class));
                }
                else {
                    startActivity(new Intent(getApplicationContext(), home_page_register.class));

                }

            } else {
                startActivity( new Intent( getApplicationContext(), home_page_guest.class ) );

            }
        } else {
            return super.onOptionsItemSelected( item );
        }
        return true;

    }
}
