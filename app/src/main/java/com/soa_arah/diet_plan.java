package com.soa_arah;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private DatabaseReference mDatabase1,mDatabase2,databaseReference,databaseReference1,mDatabase22;
    private ImageView imcal,imWater;
   private int bmi;
    private String na;
    private boolean flag,progressB=true;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
   private String id;
    double i;
    android.app.AlertDialog.Builder alert;

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
                                startActivity(new Intent(getApplicationContext(), diet_plan.class));

                                break;
                            case R.id.upload:
                                startActivity(new Intent(getApplicationContext(), Request_page.class));

                                break;
                        }
                        return false;
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
                waterSum();
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseReference=FirebaseDatabase.getInstance().getReference().child("Progress");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            String n=snapshot.getKey();

                            if(n.equals(na)){
                                progressB=false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                });

                alert= new android.app.AlertDialog.Builder(diet_plan.this);
                alert.setMessage("هل انت متأكد من حذف الخطة؟");
                alert.setCancelable(true);
                alert.setPositiveButton(
                        "نعم",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                FirebaseDatabase.getInstance().getReference().child("DietPlan").child(na).removeValue();
                                if (progressB=false)
                                    databaseReference.child(na).removeValue();

                                startActivity(new Intent(diet_plan.this, home_page_register.class));

                            }
                        });

                alert.setNegativeButton(
                        "لا",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.cancel();
                            }
                        });

                android.app.AlertDialog alert11 = alert.create();
                alert11.show();
            }
        });



        onBackPressed();
    }
    @Override
    public void onBackPressed()
    {

        // super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
    }
        public void dietplan(){

            mDatabase1 = FirebaseDatabase.getInstance().getReference().child("DietPlan").child(na);
            mDatabase22 = FirebaseDatabase.getInstance().getReference().child("DietPlan");
            mDatabase1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    plan = dataSnapshot.getValue(DietPlan.class);
                    if(plan!=null){
                    calGoal.setText(plan.getCalGoal());
                    Water.setText(plan.getWater());
                    BMI.setText(plan.getBMI());
                    NewCalFood.setText(plan.getDailyCal());}
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
        public void waterSum(){

            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.water_count, null);
            final EditText num = alertLayout.findViewById(R.id.waterNum);
            final ImageView plus = alertLayout.findViewById(R.id.increase);
            final ImageView minus = alertLayout.findViewById(R.id.decrease);


            String waterNum=Water.getText().toString();
            num.setText(waterNum);
            i = Double.parseDouble(num.getText().toString());
            plus.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {

                    String _stringVal;

                    Log.d("src", "يتم زيادة القيمة..");
                    i = i + 0.01;
                    _stringVal = String.valueOf(new DecimalFormat("##.##").format(i));
                    num.setText(_stringVal);

                }
            });

            minus.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {

                    String _stringVal;
                    Log.d("src", "يتم تنقيص القيمة..");
                    if (i > 0) {
                        i = i - 0.01;
                        _stringVal = String.valueOf(new DecimalFormat("##.##").format(i));
                        num.setText(_stringVal);
                    } else {
                        Log.d("src", "القيمه لايمكن ان تكون اقل من صفر");
                    };

                }
            });
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("عدد اللترات المكتسبة من الماء");
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // disallow cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(false);
            alert.setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            alert.setPositiveButton("حفظ", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (num.getText().toString().substring(0,1).equals("-")){
                        Water.setText(num.getText().toString().substring(1));

                    }else
                        Water.setText(num.getText());

                    mDatabase2 = FirebaseDatabase.getInstance().getReference().child("DietPlan").child(na).child("water");
                    mDatabase2.setValue(Water.getText().toString());

                }
            });
            AlertDialog dialog = alert.create();
            dialog.show();



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

