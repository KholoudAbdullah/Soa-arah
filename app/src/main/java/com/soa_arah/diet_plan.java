package com.soa_arah;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class diet_plan extends AppCompatActivity {
    private String username,hip,wight,hight,gender,waist,DailyCal,str;
    private TextView calGoal,NewCalFood,Water,BMI;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private String User_ID;
    private DietPlan plan;
    private RegisteredUser user;
    private Button delete;
    private  DatabaseReference Database;
    private DatabaseReference mDatabase1;
    private ImageView imcal,imWater,prgress;
   private int bmi;
    private String na;
    private boolean flag;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private String id;
    private Date date1,date2,date3,date4,date5,date6,date7,date;
    private String day1,day2,day3,day4,day5,day6,day7,calory,day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_diet_plan );
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );
        firebaseAuth =FirebaseAuth.getInstance();
        calGoal=(TextView)findViewById( R.id.CalGoal ) ;
        imcal=(ImageView)findViewById( R.id.imageView13 );
        imWater=(ImageView)findViewById( R.id.imageView11 );
        NewCalFood=(TextView)findViewById( R.id.NewCalFood );
        Water=(TextView)findViewById( R.id.Water);
        delete=(Button)findViewById(R.id.delete);
        prgress=(ImageView)findViewById( R.id.imageView3);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        User_ID = firebaseAuth.getCurrentUser().getEmail();
         na = User_ID.substring( 0, User_ID.lastIndexOf( "@" ) );
        progressBar.setVisibility(View.VISIBLE);
            mDatabase = FirebaseDatabase.getInstance().getReference().child("DietPlan");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    id=snapshot.getKey();
                    if(id.equals(na)){
                        flag=true;
                    }else
                        flag=false;
                }
                if(flag){
                    progressBar.setVisibility(View.INVISIBLE);
                    dietplan();

                }else {
                    progressBar.setVisibility(View.INVISIBLE);
                    creatdietplan();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.Navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.search:
                                startActivity(new Intent(getApplicationContext(), home_page_register.class));
                                break;
                            case R.id.diet_plan:
                                startActivity(new Intent(getApplicationContext(), diet_plan.class));

                                break;
                            case R.id.upload:
                                startActivity(new Intent(getApplicationContext(), Request_page.class));

                                break;
                        }
                        return false;
                    }
                });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imcal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), addCalories.class));
            }
        });
        imWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(), RequestByName.class));
            }
        });
        prgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProgressChart.class));

            }
        });
        onBackPressed();
    }
    @Override
    public void onBackPressed()
    {

        // super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
    }
        public void dietplan() {

            mDatabase1 = FirebaseDatabase.getInstance().getReference().child("DietPlan").child(na);
            mDatabase1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    plan = dataSnapshot.getValue(DietPlan.class);
                    calGoal.setText(plan.getCalGoal());
                    Water.setText(plan.getWater());
                    NewCalFood.setText(plan.getDailyCal());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

            });
            Database = FirebaseDatabase.getInstance().getReference().child("Progress");
            Database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DailyCal = dataSnapshot.child(na).child("startDate").getValue(String.class);

                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar c = Calendar.getInstance();
                    try {
                        date = Calendar.getInstance().getTime();
                        day = df.format(date);
                        c.setTime(date);
                        c.setTime(df.parse(DailyCal));
                        day1 = df.format(c.getTime());  // dt is now the new date

                        c.setTime(df.parse(DailyCal));
                        c.add(Calendar.DATE, 1);  // number of days to add
                        day2 = df.format(c.getTime());

                        c.setTime(df.parse(DailyCal));
                        c.add(Calendar.DATE, 2);  // number of days to add
                        day3 = df.format(c.getTime());

                        c.setTime(df.parse(DailyCal));
                        c.add(Calendar.DATE, 3);  // number of days to add
                        day4 = df.format(c.getTime());

                        c.setTime(df.parse(DailyCal));
                        c.add(Calendar.DATE, 4);  // number of days to add
                        day5 = df.format(c.getTime());

                        c.setTime(df.parse(DailyCal));
                        c.add(Calendar.DATE, 5);  // number of days to add
                        day6 = df.format(c.getTime());

                        c.setTime(df.parse(DailyCal));
                        c.add(Calendar.DATE, 6);  // number of days to add
                        day7 = df.format(c.getTime());
                    } catch (Exception e) {
                    }
                    if (day.equals(day1)) {
                        str = dataSnapshot.child(na).child("day1").getValue(String.class);
                    } else if (day.equals(day2)) {
                        str = dataSnapshot.child(na).child("day2").getValue(String.class);
                    } else if (day.equals(day3)) {
                        str = dataSnapshot.child(na).child("day3").getValue(String.class);
                    } else if (day.equals(day4)) {
                        str = dataSnapshot.child(na).child("day4").getValue(String.class);
                    } else if (day.equals(day5)) {
                        str = dataSnapshot.child(na).child("day5").getValue(String.class);
                    } else if (day.equals(day6)) {
                        str = dataSnapshot.child(na).child("day6").getValue(String.class);
                    } else if (day.equals(day7)) {
                        str = dataSnapshot.child(na).child("day7").getValue(String.class);
                    } else {
                        str = "0";
                    }
                    DecimalFormat precision = new DecimalFormat("0.00");
                    double cal = Double.parseDouble(str);
                    NewCalFood.setText(precision.format(cal));

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        public void creatdietplan(){
            AlertDialog.Builder alert = new AlertDialog.Builder(
                    diet_plan.this);
            alert.setMessage("هل تريد إنشاء خطة غذائية؟");
            alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TOD O Auto-generated method stub
                    startActivity(new Intent(getApplicationContext(), completDietplan.class));

                }

            });
            alert.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                    startActivity(new Intent(getApplicationContext(), home_page_register.class));

                }
            });

            alert.show();
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.viewaccount) {
            startActivity(new Intent(getApplicationContext(), view_account_register.class));
        } else if (item.getItemId() == R.id.editaccount) {
            startActivity(new Intent(getApplicationContext(), edit_account_register.class));
        } else if (item.getItemId() == R.id.aboutUs) {
            startActivity(new Intent(getApplicationContext(), aboutUs.class));
        } else if (item.getItemId() == R.id.Logout){
            firebaseAuth.signOut();
            //closing activity
            finish();
            startActivity(new Intent(getApplicationContext(), home_page_guest.class));

        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    }

