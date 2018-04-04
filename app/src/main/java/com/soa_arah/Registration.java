package com.soa_arah;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Registration extends AppCompatActivity implements View.OnClickListener{

    private EditText name;
    private EditText password,PasswordCom;
    private TextView phone;

    private Button log;
    private ProgressDialog progressDialog;
    private Button button;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    Intent intent;
    RegisteredUser user1;
    private static final String TAG = "MainActivity";

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    android.app.AlertDialog.Builder alert;
    boolean flag;
    String hexName;
    String m;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );
        firebaseAuth = FirebaseAuth.getInstance();

        mDatabase= FirebaseDatabase.getInstance().getReference().child("RegisteredUser");
        name=(EditText)findViewById(R.id.name);
        password=(EditText)findViewById(R.id.Password);
        phone=(TextView)findViewById(R.id.phone);
        PasswordCom=(EditText)findViewById(R.id.PasswordCom);

        button=(Button)findViewById(R.id.button);
        log=(Button) findViewById(R.id.log);



        //valedation
        name.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (name.getText().toString().trim().length()<1){

                    name.setError("االرجاء إدخال إسم المستخدم");
                }
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (password.getText().toString().trim().length()<6){

                    password.setError("يجب ان تتكون كلمة المرور من ٦ خانات او اكثر");
                }
            }
        });
        PasswordCom.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!password.getText().toString().trim().equals(PasswordCom.getText().toString().trim()) || PasswordCom.getText().toString().trim().length()<6){

                    PasswordCom.setError("كلمة المرور ليست متطابقة");
                }
            }
        });
        phone.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (phone.getText().toString().trim().length()!=13){

                    phone.setError("الرجاء إدخال رقم الجوال مبتدئاً بمفتاح الدولة");
                }
            }
        });



        button.setOnClickListener(this);
        log.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);



    }


    @SuppressLint("ResourceType")

    @Override
    public void onClick(View view) {
        if(view == button ){
            if (name.getText().toString().trim().length()<1){
                alert= new android.app.AlertDialog.Builder(Registration.this);
                alert.setMessage("الرجاء إدخال إسم المستخدم");
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
                return;
            }
            else if (password.getText().toString().trim().length()<6){
                alert= new android.app.AlertDialog.Builder(Registration.this);
                alert.setMessage("يجب ان تتكون كلمة المرور من ٦ خانات او اكثر");
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
                return;
            }
            else if (!password.getText().toString().trim().equals(PasswordCom.getText().toString().trim()) || PasswordCom.getText().toString().trim().length()<6){
                alert= new android.app.AlertDialog.Builder(Registration.this);
                alert.setMessage("كلمة المرور ليست متطابقة");
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
                return;
            }
            else if(phone.getText().toString().trim().toString().length()!=13 ){
                //&& phone.getText().toString().trim().toString().substring(0,1)=="+"
                alert= new android.app.AlertDialog.Builder(Registration.this);
                alert.setMessage("الرجاء إدخال رقم الجوال مبتدئاً بمفتاح الدولة");
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
                return;
            }
            else{
            progressDialog.setMessage(" الرجاء الانتظار...");
            progressDialog.show();

                mDatabase = FirebaseDatabase.getInstance().getReference().child("RegisteredUser");

                mDatabase.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            String n=stringToHex(name.getText().toString().trim().toLowerCase());
                             m=snapshot.getKey();


                            if (m.equals(n)){
                                progressDialog.dismiss();
                                alert= new android.app.AlertDialog.Builder(Registration.this);
                                alert.setMessage("عذراً يوجد مستخدم بهذا الاسم");
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
                                flag=true;
                                break;

                            }
                            else flag=false;
                        }

                        if (flag==false){
                            //calling register method on click
                            intent = new Intent( Registration.this, ActivityPhoneAuth.class );
                            intent.putExtra( "username", name.getText().toString().trim().toString() );
                            intent.putExtra( "password", password.getText().toString().trim().toString() );
                            intent.putExtra( "Phone", phone.getText().toString().trim().toString() );
                            startActivity( intent );}

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




        }
        }
        if(view == log ) {

            startActivity(new Intent(this, LoginPage.class) ); //profile=login

        }

    }

    public static String stringToHex(String base)
    {
        StringBuffer buffer = new StringBuffer();
        int intValue;
        for(int x = 0; x < base.length(); x++)
        {
            int cursor = 0;
            intValue = base.charAt(x);
            String binaryChar = new String(Integer.toBinaryString(base.charAt(x)));
            for(int i = 0; i < binaryChar.length(); i++)
            {
                if(binaryChar.charAt(i) == '1')
                {
                    cursor += 1;
                }
            }
            if((cursor % 2) > 0)
            {
                intValue += 128;
            }
            buffer.append(Integer.toHexString(intValue));
        }
        return buffer.toString();
    }
}

