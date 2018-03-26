package com.soa_arah;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class ActivityPhoneAuth extends AppCompatActivity {


    private EditText etxtPhone;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText etxtPhoneCode;
    private String mVerificationId;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;
   private Intent intent;
    RegisteredUser user1;
    android.app.AlertDialog.Builder alert;

    private static final String TAG = "ActivityPhoneAuth";

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );

        //Note that this will not work on emulator, this requires a real device
        etxtPhone = (EditText) findViewById(R.id.etxtPhone);
        intent = getIntent();
        String p = intent.getExtras().getString("Phone", "");
        etxtPhone.setText(p);
        etxtPhoneCode = (EditText) findViewById(R.id.etxtPhoneCode);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                }
            }
        };

        etxtPhone.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (etxtPhone.getText().toString().trim().length()!=13){

                    etxtPhone.setError("الرجاء إدخال رقم الجوال مبتدئاً بمفتاح الدولة");
                }
            }
        });

        etxtPhoneCode.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (etxtPhoneCode.getText().toString().trim().length()<6){

                    etxtPhoneCode.setError("الرجاء إدخال رمز التحقق");
                }
            }
        });

        onBackPressed();
    }
    @Override
    public void onBackPressed()
    {

        // super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
    }

    public void requestCode(View view) {
        String phoneNumber = etxtPhone.getText().toString();

        if(etxtPhone.getText().toString().trim().toString().length()!=13 ){
            //&& phone.getText().toString().trim().toString().substring(0,1)=="+"
            alert= new android.app.AlertDialog.Builder(ActivityPhoneAuth.this);
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

        alert= new android.app.AlertDialog.Builder(ActivityPhoneAuth.this);

        alert.setTitle( "الرجاء الانتظار حتى يتم الارسال" ).setIcon( R.drawable.f1 );

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

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, 10, TimeUnit.SECONDS, ActivityPhoneAuth.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        //Called if it is not needed to enter verification code
                        signInWithCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        //incorrect phone number, verification code, emulator, etc.
                        alert = new android.app.AlertDialog.Builder(ActivityPhoneAuth.this);
                        alert.setMessage("هناك خلل..");
                        alert.setCancelable(true);
                        alert.setPositiveButton(
                                "موافق",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        startActivity(new Intent(getApplicationContext(), LoginPage.class));

                                    }
                                });
                        android.app.AlertDialog alert11 = alert.create();
                        alert11.show();                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        //now the code has been sent, save the verificationId we may need it
                        super.onCodeSent(verificationId, forceResendingToken);

                        mVerificationId = verificationId;
                    }

                }
        );
    }

    private void signInWithCredential(PhoneAuthCredential phoneAuthCredential) {

        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                                   registerUser();}
                        else {
                            android.app.AlertDialog.Builder alert= new android.app.AlertDialog.Builder(ActivityPhoneAuth.this);


                            alert.setTitle("يوجد حساب برقم الجوال أو يوجد خطأ في الرمز").setIcon(R.drawable.f1);
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

                        // ...
                    }
                });

    }

    public void signIn(View view) {
        String code = etxtPhoneCode.getText().toString();

        if (etxtPhoneCode.getText().toString().trim().length()<6){
            alert= new android.app.AlertDialog.Builder(ActivityPhoneAuth.this);
            alert.setMessage("الرجاء إدخال رمز التحقق");
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

        signInWithCredential(PhoneAuthProvider.getCredential(mVerificationId, code));

    }
    private void registerUser() {
        //getting email and password from edit texts
        final String Name = intent.getExtras().getString("username", "");

        String email =stringToHex(Name)+"@soaarah.com";;
        String Password = intent.getExtras().getString("password", "");

        if (!(Name.isEmpty() && Password.isEmpty())) {

            //if the email and password and Location are not empty
            //displaying a progress dialog

            //creating a new user

            AuthCredential credential = EmailAuthProvider.getCredential(email, Password);
            mAuth.getCurrentUser().linkWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                firebaseAuth = FirebaseAuth.getInstance();
                                String User_ID = firebaseAuth.getCurrentUser().getUid();
                                final String uid = firebaseAuth.getInstance().getCurrentUser().getUid();
                                user1 = new RegisteredUser();

                                user1.setName(  intent.getExtras().getString("username", "") );
                                user1.setPhoneNum( etxtPhone.getText().toString() );
                                String email = stringToHex(intent.getExtras().getString("username", "") ) + "@soaarah.com";
                                user1.setEmail( email );
                                user1.setID( User_ID );
                                user1.setWight( "لم يتم إدخال بيانات" );
                                user1.setHight( "لم يتم إدخال بيانات" );
                                user1.setWaist( "لم يتم إدخال بيانات" );
                                user1.setHip( "لم يتم إدخال بيانات" );
                                user1.setGender(  "لم يتم إدخال بيانات" );
                                user1.setDateOfBarth(  "لم يتم إدخال بيانات" );
                                mDatabase= FirebaseDatabase.getInstance().getReference().child("RegisteredUser");
                                mDatabase.child( stringToHex(intent.getExtras().getString("username", "") )).setValue( user1 );
                                AlertDialog.Builder alert = new AlertDialog.Builder(
                                        ActivityPhoneAuth.this);
                                alert.setTitle("تمت عملية التسجيل بنجاح").setIcon(R.drawable.t1);
                                AlertDialog dialog = alert.create();

                                // Finally, display the alert dialog
                                dialog.show();

                                // Get screen width and height in pixels
                                DisplayMetrics displayMetrics = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                                // The absolute width of the available display size in pixels.
                                int displayWidth = displayMetrics.widthPixels;
                                // The absolute height of the available display size in pixels.
                                int displayHeight = displayMetrics.heightPixels;

                                // Initialize a new window manager layout parameters
                                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

                                // Copy the alert dialog window attributes to new layout parameter instance
                                layoutParams.copyFrom(dialog.getWindow().getAttributes());

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
                                dialog.getWindow().setAttributes(layoutParams);
                                startActivity(new Intent(getApplicationContext(), home_page_register.class));
                                //close this activity
                                finish();
                                //opening login activity


                            } else {
                                //display some message here
                                AlertDialog.Builder alert = new AlertDialog.Builder(
                                        ActivityPhoneAuth.this );
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

                        }
                    } );
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