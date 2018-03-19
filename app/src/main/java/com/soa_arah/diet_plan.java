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


public class diet_plan extends AppCompatActivity {
    private String username,hip,wight,hight,date,gender,waist;
    private TextView calGoal,NewCalFood,Water,BMI;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private String User_ID;
    private DietPlan plan;
    private RegisteredUser user;
    private Button delete;
    private  DatabaseReference Database;
    private DatabaseReference mDatabase1;
    private ImageView imcal,imWater;
   private int bmi;
    private String na;
    private boolean flag;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
   private String id;
    // private DietPlan plan;

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
        BMI=(TextView)findViewById( R.id.BMI);
        delete=(Button)findViewById(R.id.delete);
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
                                startActivity(new Intent(getApplicationContext(), edit_account_register.class));

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
            public void onClick(View view) {
            }
        });

        imcal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(), RequestByName.class));
            }
        });
        imWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(), RequestByName.class));
            }
        });

        }
        public void dietplan(){

            mDatabase1 = FirebaseDatabase.getInstance().getReference().child("DietPlan").child(na);
            mDatabase1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    plan = dataSnapshot.getValue(DietPlan.class);
                    calGoal.setText(plan.getCalGoal());
                    Water.setText(plan.getWater());
                    BMI.setText(plan.getBMI());
                    NewCalFood.setText(plan.getDailyCal());
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


    }

