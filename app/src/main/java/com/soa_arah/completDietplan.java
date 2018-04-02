package com.soa_arah;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;


public class completDietplan  extends AppCompatActivity  {



    private EditText hight,waist,hip,wight;
    private String year1,day,month;
    private  ProgressDialog progressDialog;
    private Spinner gen;
    private TextView mDisplayDate;
    String[] array;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    String date;
    String record="";
    String gender[]={"أنثى","ذكر"};
    ArrayAdapter<String> adapter;
    //firebase auth object
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private Button cont;
    private Button cancel;
    private double bmi,BMR;
    private String age;
    Intent intent;
    private static final String TAG = "MainActivity";
    android.app.AlertDialog.Builder alert;
    int year;
    Calendar today1 = Calendar.getInstance();
    boolean datV= true,flag=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_complet_dietplan );
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );
        cont=(Button)findViewById(R.id.con);
        cancel=(Button) findViewById(R.id.cancel);

        hight=(EditText)findViewById(R.id.hight);
        waist=(EditText)findViewById(R.id.waist);
        hip=(EditText)findViewById(R.id.hip);
        wight=(EditText)findViewById(R.id.wight);



        //getting firebase auth object

        firebaseAuth = FirebaseAuth.getInstance();

        String User_ID = firebaseAuth.getCurrentUser().getEmail();
        String username =User_ID.substring(0,User_ID.lastIndexOf("@"));
        mDatabase= FirebaseDatabase.getInstance().getReference().child("RegisteredUser").child(username);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                RegisteredUser user = dataSnapshot.getValue(RegisteredUser.class);

                if(user.getWight().equals("لم يتم إدخال بيانات")) {
                    wight.setHint("ادخل الوزن");
                    wight.setOnFocusChangeListener(new View.OnFocusChangeListener(){

                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (wight.getText().toString().trim().length()<2){

                                wight.setError("الرجاء إدخال الوزن");
                            }
                        }
                    });
                }else
                    wight.setText(user.getWight());

                if(user.getHight().equals("لم يتم إدخال بيانات")) {
                    hight.setHint("ادخل الطول");
                    hight.setOnFocusChangeListener(new View.OnFocusChangeListener(){

                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hight.getText().toString().trim().length()<2){

                                hight.setError("الرجاء إدخال الطول");
                            }
                        }
                    });
                }
                else
                    hight.setText(user.getHight());

                if(user.gethip().equals("لم يتم إدخال بيانات")) {
                    hip.setHint("ادخل محيط الفخذ");
                    hip.setOnFocusChangeListener(new View.OnFocusChangeListener(){

                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hip.getText().toString().trim().length()<2){

                                hip.setError("الرجاء إدخال محيط الفخذ");
                            }
                        }
                    });
                }else
                    hip.setText(user.gethip());

                if(user.getWaist().equals("لم يتم إدخال بيانات")) {
                    waist.setHint("ادخل محيط الخصر");
                    waist.setOnFocusChangeListener(new View.OnFocusChangeListener(){

                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (waist.getText().toString().trim().length()<2){

                                waist.setError("الرجاء إدخال محيط الخصر");
                            }
                        }
                    });
                }else
                    waist.setText(user.getWaist());


             mDisplayDate.setText(user.getDateOfBarth());
             String bdate=user.getDateOfBarth();

                array=bdate.split("/");


       day=array[0];
       month=array[1];
       year1=array[2];

                age= getAge(Integer.parseInt(year1),Integer.parseInt( month ),Integer.parseInt(day));
                flag=false;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });





        gen=(Spinner)findViewById(R.id.gen);
        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,gender);
        gen.setAdapter(adapter);

        if (flag){
        hight.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hight.getText().toString().trim().length()<2){

                    hight.setError("الرجاء إدخال الطول");
                }
            }
        });
        wight.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (wight.getText().toString().trim().length()<2){

                    wight.setError("الرجاء إدخال الوزن");
                }
            }
        });
        waist.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (waist.getText().toString().trim().length()<2){

                    waist.setError("الرجاء إدخال محيط الخصر");
                }
            }
        });
        hip.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hip.getText().toString().trim().length()<2){

                    hip.setError("الرجاء إدخال محيط الفخذ");
                }
            }
        });}

        gen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        record = "أنثى";
                        break;
                    case 1:
                        record = "ذكر";
                        break;


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        progressDialog = new ProgressDialog(this);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        completDietplan .this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                Calendar today = Calendar.getInstance();
                if (year>(today.get(Calendar.YEAR)-3)){
                    alert= new android.app.AlertDialog.Builder(completDietplan.this);
                    alert.setMessage("الرجاء إدخال تاريخ صحيح");
                    alert.setCancelable(true);
                    alert.setPositiveButton(
                            "موافق",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.cancel();

                                }
                            });
                    android.app.AlertDialog alert11 = alert.create();
                    alert11.show();
                    datV=false;
                }else
                    datV=true;

                date = day + "/" + month + "/" + year;
                mDisplayDate.setText(date);
//calculate age
               age= getAge(year,month,day);
            }
        };

        cont.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if(hight.getText().toString().trim().length()<1){
                    alert= new android.app.AlertDialog.Builder(completDietplan.this);
                    alert.setMessage("الرجاء إدخال الطول");
                    alert.setCancelable(true);
                    alert.setPositiveButton(
                            "موافق",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.cancel();

                                }
                            });
                    android.app.AlertDialog alert11 = alert.create();
                    alert11.show();

                }
                else if (wight.getText().toString().trim().length()<1){
                    alert= new android.app.AlertDialog.Builder(completDietplan.this);
                    alert.setMessage("الرجاء إدخال الوزن");
                    alert.setCancelable(true);
                    alert.setPositiveButton(
                            "موافق",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.cancel();

                                }
                            });
                    android.app.AlertDialog alert11 = alert.create();
                    alert11.show();
                }
                else if (waist.getText().toString().trim().length()<1){
                    alert= new android.app.AlertDialog.Builder(completDietplan.this);
                    alert.setMessage("الرجاء إدخال محيط الخصر");
                    alert.setCancelable(true);
                    alert.setPositiveButton(
                            "موافق",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.cancel();

                                }
                            });
                    android.app.AlertDialog alert11 = alert.create();
                    alert11.show();
                }
                else if (hip.getText().toString().trim().length()<1){
                    alert= new android.app.AlertDialog.Builder(completDietplan.this);
                    alert.setMessage("الرجاء إدخال محيط الفخذ");
                    alert.setCancelable(true);
                    alert.setPositiveButton(
                            "موافق",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.cancel();

                                }
                            });
                    android.app.AlertDialog alert11 = alert.create();
                    alert11.show();
                }else if (!datV){
                    alert= new android.app.AlertDialog.Builder(completDietplan.this);
                    alert.setMessage("الرجاء إدخال تاريخ صحيح");
                    alert.setCancelable(true);
                    alert.setPositiveButton(
                            "موافق",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.cancel();

                                }
                            });
                    android.app.AlertDialog alert11 = alert.create();
                    alert11.show();
                }

              else {

                  String Wight=wight.getText().toString().trim();
                 String Hight=hight.getText().toString().trim();
                 String Hip=hip.getText().toString().trim();
                String Waist = waist.getText().toString().trim();
                String date=mDisplayDate.getText().toString();

                String gen=record;



                  if(record.equals("أنثى")){
                      BMR = (10.0*Double.parseDouble(Wight)) + (6.25*Double.parseDouble(Hight))- (5.0* Double.parseDouble(age)) - (161.0);
                 }
                  else if(record.equals("ذكر")) {
                      BMR = (10.0*Double.parseDouble(Wight))+ (6.25*Double.parseDouble(Hight)) - (5.0*Double.parseDouble(age)) + (5.0);
                       }
                 intent = new Intent( completDietplan.this, maxminDietplan.class );
                  double pow=(Double.parseDouble(Hight))*(Double.parseDouble(Hight));
                  bmi=(Double.parseDouble(Wight)/pow)*10000;
                 intent.putExtra( "BMR", Double.toString(BMR) );
                 intent.putExtra( "BMI",Double.toString(bmi) );
                 intent.putExtra( "Wight",Wight );
                  intent.putExtra( "Hight",Hight );
                  intent.putExtra( "Hip",Hip );
                  intent.putExtra( "Waist",Waist );
                  intent.putExtra( "date",date );
                  intent.putExtra( "gen",gen );
                  startActivity( intent );



              }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        completDietplan.this);
                alert.setMessage("هل أنت متأكد من الإلغاء؟");
                alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TOD O Auto-generated method stub
                        startActivity(new Intent(getApplicationContext(), home_page_register.class));

                    }


                });
                alert.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();

                    }
                });

                alert.show();
            }
        });
        onBackPressed();
    }
    @Override
    public void onBackPressed()
    {

        // super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
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
    //calculate age
    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

}
