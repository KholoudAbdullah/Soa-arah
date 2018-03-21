package com.soa_arah;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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


        button.setOnClickListener(this);
        log.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);


        onBackPressed();
    }
    @Override
    public void onBackPressed()
    {

        // super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
    }

    @SuppressLint("ResourceType")

    @Override
    public void onClick(View view) {
        if(view == button ){
            progressDialog.setMessage(" الرجاء الانتظار حتى يتم التسجيل");
            progressDialog.show();
            //calling register method on click
            intent = new Intent( Registration.this, ActivityPhoneAuth.class );
            intent.putExtra( "username", name.getText().toString().trim().toString() );
            intent.putExtra( "password", password.getText().toString().trim().toString() );
            intent.putExtra( "Phone", phone.getText().toString().trim().toString() );
            startActivity( intent );


        }
        if(view == log ) {

            startActivity(new Intent(this, LoginPage.class) ); //profile=login

        }

    }
}

