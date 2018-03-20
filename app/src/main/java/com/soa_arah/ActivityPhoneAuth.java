package com.soa_arah;

import android.annotation.SuppressLint;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
        Intent intent = getIntent();
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
        firebaseAuth = FirebaseAuth.getInstance();

        String User_ID = firebaseAuth.getCurrentUser().getEmail();
        String username =User_ID.substring(0,User_ID.lastIndexOf("@"));

        mDatabase= FirebaseDatabase.getInstance().getReference().child("RegisteredUser").child(username);
        onBackPressed();
    }
    @Override
    public void onBackPressed()
    {

        // super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
    }

    public void requestCode(View view) {
        String phoneNumber = etxtPhone.getText().toString();

        if (TextUtils.isEmpty(phoneNumber))
            return;
        Toast.makeText(ActivityPhoneAuth.this, "الرجاء الانتظار حتى يتم الارسال" , Toast.LENGTH_SHORT).show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, 60, TimeUnit.SECONDS, ActivityPhoneAuth.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        //Called if it is not needed to enter verification code
                        signInWithCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        //incorrect phone number, verification code, emulator, etc.
                        Toast.makeText(ActivityPhoneAuth.this, "onVerificationFailed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

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

        mAuth.getCurrentUser().linkWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = task.getResult().getUser();
                            AlertDialog.Builder alert = new AlertDialog.Builder(
                                    ActivityPhoneAuth.this);

                            mDatabase.child("phoneNum").setValue(etxtPhone.getText().toString());
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

                        } else {
                            AlertDialog.Builder alert = new AlertDialog.Builder(
                                    ActivityPhoneAuth.this);

                            alert.setTitle("يوجد حساب برقم الجوال أو يوجد خطأ في الرمز").setIcon(R.drawable.f1);
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

                        }

                        // ...
                    }
                });

    }

    public void signIn(View view) {
        String code = etxtPhoneCode.getText().toString();
        if (TextUtils.isEmpty(code))
            return;

        signInWithCredential(PhoneAuthProvider.getCredential(mVerificationId, code));

    }
}