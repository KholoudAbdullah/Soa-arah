package com.soa_arah;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;


public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = "ActivityPhoneAuth";

    private TextView enterNum;
    private TextView toResend;
    private EditText name;
    private EditText code;
    private Button send;
    private Button resend;
    private Button confirm;
    private ProgressBar progressBar;


    android.app.AlertDialog.Builder alert;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


     private DatabaseReference dbRef;


    String phoneNumber;
    String n;
    boolean flag;

    private String mVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationStateChangedCallBacks;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );

        isConnected();

        name = (EditText) findViewById(R.id.name);
        code = (EditText) findViewById(R.id.code);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        name.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (name.getText().toString().trim().length()<1){

                    name.setError("الرجاء إدخال إسم المستخدم");
                }
            }
        });
        code.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (code.getText().toString().trim().length()<1){

                    code.setError("الرجاء إدخال رمز التحقق");
                }
            }
        });



        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                }
            }
        };



    }
    public void requestCode(View view){

        if(name.getText().toString().trim().length()<1){
            alert= new android.app.AlertDialog.Builder(ForgotPasswordActivity.this);
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

        progressBar.setVisibility(View.VISIBLE);


        dbRef=FirebaseDatabase.getInstance().getReference().child("RegisteredUser");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    n=snapshot.getKey();

                    String na=stringToHex(name.getText().toString().trim());
                    if(n.equals(na)){
                        progressBar.setVisibility(View.INVISIBLE);

                        phoneNumber=snapshot.child("phoneNum").getValue(String.class);
                        Log.d("111111","222"+phoneNumber);
                        flag=true;

                        //Toast.makeText( ForgotPasswordActivity.this, "الرجاء الانتظار حتى يتم الارسال", Toast.LENGTH_SHORT ).show();


                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                phoneNumber, 10, TimeUnit.SECONDS, ForgotPasswordActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                        //Called if it is not needed to enter verification code
                                        signInWithCredential( phoneAuthCredential );
                                    }

                                    @Override
                                    public void onVerificationFailed(FirebaseException e) {
                                        //incorrect phone number, verification code, emulator, etc.
                                        //Toast.makeText( ForgotPasswordActivity.this, "onVerificationFailed " + e.getMessage(), Toast.LENGTH_SHORT ).show();

                                        AlertDialog.Builder alert = new AlertDialog.Builder(
                                                ForgotPasswordActivity.this );

                                        alert.setTitle( "عذراً تأكد من رمز التحقق" ).setIcon( R.drawable.f1 );
                                        alert.setNegativeButton("موافق", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                        AlertDialog dialog = alert.create();

                                        // Finally, display the alert dialog
                                        dialog.show();
                                    }

                                    @Override
                                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        //now the code has been sent, save the verificationId we may need it
                                        super.onCodeSent( verificationId, forceResendingToken );

                                        mVerificationId = verificationId;
                                    }


                                }
                        );
//                        Toast.makeText(getApplicationContext(),"in if a",Toast.LENGTH_LONG).show();
                        break;
                    }else flag=false;

                }
                if(flag==false){
                    progressBar.setVisibility(View.INVISIBLE);

                    AlertDialog.Builder alert = new AlertDialog.Builder(
                            ForgotPasswordActivity.this );

                    alert.setTitle( "عذراً تأكد من اسم المستخدم" ).setIcon( R.drawable.f1 );
                    alert.setNegativeButton("موافق", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
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



                    int dialogWindowWidth = (int) (displayWidth * 0.9f);
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
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if (TextUtils.isEmpty(phoneNumber)) {
        // Log.d("phonenumber","phonenumber"+phoneNumber.toString());
            return;
        }



        }


    private void signInWithCredential(PhoneAuthCredential phoneAuthCredential) {

        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("111","11eee1");
                        if (task.isSuccessful()) {

                            FirebaseUser user = task.getResult().getUser();
                            AlertDialog.Builder alert = new AlertDialog.Builder(
                                    ForgotPasswordActivity.this);

                            alert.setTitle("تمت عملية التأكيد بنجاح").setIcon(R.drawable.t1);
                            Log.d("111","111");
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
                            Intent intent = new Intent(ForgotPasswordActivity.this, ResetPassword.class);
                            intent.putExtra("name",name.getText().toString().trim().toString());
                            startActivity(intent);
//                            startActivity(new Intent(getApplicationContext(), ResetPassword.class));

                        } else {
                            AlertDialog.Builder alert = new AlertDialog.Builder(
                                    ForgotPasswordActivity.this);

                            alert.setTitle("الرجاء التأكد من رمز التحقق").setIcon(R.drawable.f1);
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

                        }

                        // ...
                    }
                });

    }


    public void confirm(View view) {
        String Code = code.getText().toString();
        if (TextUtils.isEmpty(Code))
            return;

        signInWithCredential(PhoneAuthProvider.getCredential(mVerificationId, Code));

        Intent intent = new Intent(ForgotPasswordActivity.this, ResetPassword.class);
        intent.putExtra("name",name.getText().toString().trim().toString());
        startActivity(intent);
//        startActivity(new Intent(this , ResetPassword.class));
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
