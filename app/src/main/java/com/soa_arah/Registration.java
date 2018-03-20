package com.soa_arah;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

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
    private void registerUser() {
        //getting email and password from edit texts

        final String Name = name.getText().toString().trim().toLowerCase();
        final String Phone = phone.getText().toString().trim();

        String email =stringToHex(Name)+"@soaarah.com";;
        String Password = password.getText().toString().trim();

        if (!(name.getText().toString().trim().isEmpty() && password.getText().toString().trim().isEmpty())) {

            //if the email and password and Location are not empty
            //displaying a progress dialog
            progressDialog.setMessage( " الرجاء الانتظار حتى يتم التسجيل" );
            progressDialog.show();

            //creating a new user


            firebaseAuth.createUserWithEmailAndPassword( email, Password )
                    .addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
                        @SuppressLint("ResourceType")
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                String User_ID = firebaseAuth.getCurrentUser().getUid();
                                final String uid = firebaseAuth.getInstance().getCurrentUser().getUid();
                                user1 = new RegisteredUser();

                                user1.setName( name.getText().toString().toLowerCase() );
                                user1.setPhoneNum( phone.getText().toString() );
                                String email = stringToHex( name.getText().toString().toLowerCase() ) + "@soaarah.com";
                                user1.setEmail( email );
                                user1.setID( User_ID );
                                user1.setWight( "لم يتم إدخال بيانات" );
                                user1.setHight( "لم يتم إدخال بيانات" );
                                user1.setWaist( "لم يتم إدخال بيانات" );
                                user1.setHip( "لم يتم إدخال بيانات" );
                                user1.setGender(  "لم يتم إدخال بيانات" );
                                user1.setDateOfBarth(  "لم يتم إدخال بيانات" );
                                mDatabase.child( stringToHex( name.getText().toString().toLowerCase() ) ).setValue( user1 );

                                //close this activity
                                finish();
                                //opening login activity
                                intent = new Intent( Registration.this, ActivityPhoneAuth.class );
                                intent.putExtra( "Phone", phone.getText().toString().trim().toString() );
                                startActivity( intent );

                            } else {
                                //display some message here
                                AlertDialog.Builder alert = new AlertDialog.Builder(
                                        Registration.this );
                                alert.setTitle( "عذراً يوجد مستخدم بهذا الاسم" ).setIcon( R.drawable.f1 );
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
                                //Toast.makeText(Registration.this, "هناك خلل..", Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        }
                    } );
        } else if (name.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(
                    Registration.this );

            alert.setTitle( "عذراً أحد الحقول الإلزامية فارغ" ).setIcon( R.drawable.f1 );
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
    }
    @Override
    public void onClick(View view) {
        if(view == button ){
            //calling register method on click
            registerUser();}
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

