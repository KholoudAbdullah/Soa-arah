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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;


public class completDietplan  extends AppCompatActivity  {

    private EditText hight,waist,hip,wight;
    private  ProgressDialog progressDialog;
    private Spinner gen;
    private TextView mDisplayDate;
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



    gen=(Spinner)findViewById(R.id.gen);
        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,gender);
        gen.setAdapter(adapter);
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
                int year = cal.get(Calendar.YEAR);
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
              if((wight.getText().toString().trim().isEmpty())||(hight.getText().toString().trim().isEmpty())||(hip.getText().toString().trim().isEmpty())||(waist.getText().toString().trim().isEmpty())||(mDisplayDate.getText().toString().trim().isEmpty())){
                  AlertDialog.Builder alert = new AlertDialog.Builder(
                          completDietplan.this );

                  alert.setTitle( "عذراً أحد الحقول فارغ" ).setIcon( R.drawable.f1 );

                  AlertDialog dialog = alert.create();

                  // Finally, display the alert dialog
                  dialog.show();

                  // Get screen width and height in pixels
                  DisplayMetrics displayMetrics = new DisplayMetrics();
                  getWindowManager().getDefaultDisplay().getMetrics( displayMetrics );
                  // The absolute width of the available display size in pixels.
                  int displayWidth = displayMetrics.widthPixels;
                  // The absolute height of the available display size in pixels.
                  int displayHeight = displayMetrics.heightPixels;

                  // Initialize a new window manager layout parameters
                  WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

                  // Copy the alert dialog window attributes to new layout parameter instance
                  layoutParams.copyFrom( dialog.getWindow().getAttributes() );

                  // Set the alert dialog window width and height
                  // Set alert dialog width equal to screen width 90%
                  // int dialogWindowWidth = (int) (displayWidth * 0.9f);
                  // Set alert dialog height equal to screen height 90%
                  // int dialogWindowHeight = (int) (displayHeight * 0.9f);

                  // Set alert dialog width equal to screen width 70%
                  int dialogWindowWidth = (int) (displayWidth * 0.9f);
                  // Set alert dialog height equal to screen height 70%
                  int dialogWindowHeight = (int) (displayHeight * 0.15f);

                  // Set the width and height for the layout parameters
                  // This will bet the width and height of alert dialog
                  layoutParams.width = dialogWindowWidth;
                  layoutParams.height = dialogWindowHeight;

                  // Apply the newly created layout parameters to the alert dialog window
                  dialog.getWindow().setAttributes( layoutParams );
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
