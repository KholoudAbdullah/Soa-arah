package com.soa_arah;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Registration extends AppCompatActivity implements View.OnClickListener{
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 555;
    private EditText name;
    private EditText password;
    private EditText phone;
    private EditText wedith;
    private EditText hight;
    private EditText Waist;
    private EditText thigh;
    private TextView textpasswordempty;
    private TextView textnameempty;
    private TextView textphoneempty;
    private Spinner gen;
   private TextView log;
    String date;
    private ProgressDialog progressDialog;
    private Button button;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    String Phone;
    String message;
    User user1;
    private static final String TAG = "MainActivity";

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    String record="";
    String gender[]={"أنثى","ذكر"};
    ArrayAdapter<String> adapter;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        firebaseAuth = FirebaseAuth.getInstance();

        mDatabase= FirebaseDatabase.getInstance().getReference().child("RegisteredUser");
name=(EditText)findViewById(R.id.name);

        password=(EditText)findViewById(R.id.password);
        phone=(EditText)findViewById(R.id.phone);
        wedith=(EditText)findViewById(R.id.wedith);
        hight=(EditText)findViewById(R.id.hight);
        Waist=(EditText)findViewById(R.id.Waist);
        thigh=(EditText)findViewById(R.id.thigh);
        textpasswordempty =(TextView)findViewById(R.id.textpasswordempty);
        textphoneempty = (TextView)findViewById(R.id.textphoneempty);
        textnameempty = (TextView)findViewById(R.id.textnameempty);
        button=(Button)findViewById(R.id.button);
        log=(TextView)findViewById(R.id.log);
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
        button.setOnClickListener(this);
        log.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Registration.this,
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

                 date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
            }
        };



    }


    @Override
    public void onClick(View view) {
        if(view == button ){
            //calling register method on click
            registerUser();}
        if(view == log ) {
            startActivity(new Intent(this, home_page_guest.class) ); //profile=login

        }

    }
    private void registerUser() {
        //getting email and password from edit texts

        final String Name = name.getText().toString().trim();
        final String Phone= phone.getText().toString().trim();
        final String Wedith =wedith.getText().toString().trim();
        final String Hight =hight.getText().toString().trim();
        final String waist =Waist.getText().toString().trim();
        final String Thigh =thigh.getText().toString().trim();


        String Password  = password.getText().toString().trim();
        String email =Password +"@"+ Name+".com" ;


        //checking if email and passwords are empty
        if(TextUtils.isEmpty(Name)) {
            textnameempty.setText("* حقل مطلوب ");
        }else{
            textnameempty.setText("");
        }

        if(TextUtils.isEmpty(Phone)) {
            textphoneempty.setText("* حقل مطلوب ");
        }else{
            textphoneempty.setText("");
        }

        if(TextUtils.isEmpty(Password)){
            textpasswordempty.setText("* حقل مطلوب");
        }else{
            textpasswordempty.setText("");
        }



        if(textpasswordempty.getText().toString().trim().isEmpty()&&textphoneempty.getText().toString().trim().isEmpty()&&textnameempty.getText().toString().trim().isEmpty()) {
            //if the email and password and Location are not empty
            //displaying a progress dialog
            progressDialog.setMessage("تسجيل، الرجاء الانتظار..");
            progressDialog.show();

            //creating a new user
            firebaseAuth.createUserWithEmailAndPassword(email, Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                String User_ID = firebaseAuth.getCurrentUser().getUid();
                                final String uid = firebaseAuth.getInstance().getCurrentUser().getUid();
                                user1=new User();

                                user1.setName(name.getText().toString());
                                user1.setPhoneNum(phone.getText().toString());
                                String email=password.getText().toString()+"@"+name.getText().toString()+".com";
                                user1.setEmail(email);
                                user1.setID(User_ID);
                                user1.setPassword(password.getText().toString());

                                    user1.setWight( wedith.getText().toString());

                                    user1.setHight( hight.getText().toString());


                                    user1.setWaist( Waist.getText().toString());


                                    user1.setHip( thigh.getText().toString());

                                user1.setGender(record);
                                user1.setDateOfBarth(date);
                                mDatabase.child(name.getText().toString()).setValue(user1);
                                // Intent intent=new Intent(RegisterOrgActivity.this, ProfileActivity.class);
                                // intent.putExtra("org", (Serializable) org1);
                                //display message to the user here
                                Toast.makeText(Registration.this, "تمت العملية بنجاح", Toast.LENGTH_LONG).show();
                                //close this activity
                                finish();
                                //opening login activity
                                startActivity(new Intent(getApplicationContext(), home_page_guest.class));
                            }
                            else {
                                //display some message here
                                Toast.makeText(Registration.this, "هناك خلل..", Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
        }
    }
    protected void sendSMSMessage() {
        final String Phone= phone.getText().toString().trim();
        String message = "hello";


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(Phone, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }}
    }

