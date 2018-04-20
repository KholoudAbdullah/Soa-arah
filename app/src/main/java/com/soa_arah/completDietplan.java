package com.soa_arah;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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



    private EditText hight,wight;
    private String year1,day,month;
    private  ProgressDialog progressDialog;
    private Spinner gen,activity;
    private TextView mDisplayDate;
    String[] array;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    String date;
    String record="";
    private double activityFactor;
    String gender[]={"أنثى","ذكر"};
    String act[]={"ممارسة قليلة أو معدومة","نشاط خفيف (ممارسة خفيفة ، ممارسة الرياضة من 1-3 أيام في الأسبوع)","معتدل نشط (الرياضة / ممارسة 3-5 أيام في الأسبوع)","نشط للغاية (الرياضات المكثفة 6 أو 7 أيام في الأسبوع)","نشاط إضافي (الرياضة والتمارين الرياضية ، أو التدريب البدني أو التدريب الشاق)"};
    ArrayAdapter<String> adapter,adapter2;
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
    boolean datV= true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_complet_dietplan );
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );

        isConnected();

        cont=(Button)findViewById(R.id.con);
        cancel=(Button) findViewById(R.id.cancel);
        activity=(Spinner) findViewById(R.id.spinner2);
        hight=(EditText)findViewById(R.id.hight);
        wight=(EditText)findViewById(R.id.wight);
        progressDialog = new ProgressDialog(completDietplan.this);

        // Setting progressDialog Title.
        progressDialog.setTitle("الرجاء الإنتظار ...");
        progressDialog.show();


        hight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (hight.getText().toString().trim().length()<2){

                    hight.setError("الرجاء إدخال الطول");
                }
            }
        });
        wight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (wight.getText().toString().trim().length()<2){

                    wight.setError("الرجاء إدخال الوزن");
                }
            }
        });


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
                if(user.getDateOfBarth().equals( "لم يتم إدخال بيانات" ))
                    mDisplayDate.setText("اختر التاريخ");
                else{
                    mDisplayDate.setText(user.getDateOfBarth());
                    String bdate=user.getDateOfBarth();
                    array=bdate.split("/");
                    day=array[0];
                    month=array[1];
                    year1=array[2];
                    age= getAge(Integer.parseInt(year1),Integer.parseInt( month ),Integer.parseInt(day));
                }


                // Hiding the progressDialog after done uploading.
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Hiding the progressDialog after done uploading.
                progressDialog.dismiss();

            }

        });
        adapter2=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,act);
        activity.setAdapter(adapter2);



        gen=(Spinner)findViewById(R.id.gen);
        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,gender);
        gen.setAdapter(adapter);
        activity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        activityFactor=1.2;
                        break;
                    case 1:
                        activityFactor=1.375;
                        break;
                    case 2:
                        activityFactor=1.55;
                        break;
                    case 3:
                        activityFactor=1.725;
                        break;
                    case 4:
                        activityFactor=1.9;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                else if (!datV){
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
                String date=mDisplayDate.getText().toString();

                String gen=record;



                  if(record.equals("أنثى")){
                      BMR = (10.0*Double.parseDouble(Wight)) + (6.25*Double.parseDouble(Hight))- (5.0* Double.parseDouble(age)) - (161.0);
                      BMR*=activityFactor;
                 }
                  else if(record.equals("ذكر")) {
                      BMR = (10.0*Double.parseDouble(Wight))+ (6.25*Double.parseDouble(Hight)) - (5.0*Double.parseDouble(age)) + (5.0);
                      BMR*=activityFactor;
                  }
                 intent = new Intent( completDietplan.this, maxminDietplan.class );
                  double pow=(Double.parseDouble(Hight))*(Double.parseDouble(Hight));
                  bmi=(Double.parseDouble(Wight)/pow)*10000;
                 intent.putExtra( "BMR", Double.toString(BMR) );
                 intent.putExtra( "BMI",Double.toString(bmi) );
                 intent.putExtra( "Wight",Wight );
                  intent.putExtra( "Hight",Hight );
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
