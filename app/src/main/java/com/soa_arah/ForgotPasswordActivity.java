package com.soa_arah;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

        name = (EditText) findViewById(R.id.name);
        code = (EditText) findViewById(R.id.code);



//        FirebaseDatabase database= FirebaseDatabase.getInstance();
//        DatabaseReference dbRef= database.getReference();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                }
            }
        };


//        dbRef.child("RegisteredUser").child(Name).child("phoneNum").
//        dbRef.child("RegisteredUser").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
//                    Toast.makeText(getApplicationContext(),"in for b",Toast.LENGTH_LONG).show();
////                    n=snapshot.getKey();
//                    n=snapshot.child("email").getValue(String.class);
//                    Toast.makeText(getApplicationContext(),"in for a",Toast.LENGTH_LONG).show();
//                   if(n.equals(name.getText().toString()+"@soaarah.com")){
//                       Toast.makeText(getApplicationContext(),"in if b",Toast.LENGTH_LONG).show();
//                       phoneNumber=snapshot.child("phoneNum").getValue(String.class);
//                       flag=true;
//                       Toast.makeText(getApplicationContext(),"in if a",Toast.LENGTH_LONG).show();
//                       break;
//                   }else flag=false;
//                    }
//
//                }
//
////                User user = dataSnapshot.getValue(User.class);
////                phoneNumber=user.getPhoneNum();
//
//
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }

    public void requestCode(View view){

//        String Name= name.getText().toString();
////        dbRef.child("RegisteredUser").child(Name).child("phoneNum").
//        dbRef.child("RegisteredUser").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//                String phoneNumber=user.getPhoneNum();

        dbRef=FirebaseDatabase.getInstance().getReference().child("RegisteredUser");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                  // Toast.makeText(getApplicationContext(),"in for b",Toast.LENGTH_LONG).show();
                    n=snapshot.getKey();
//                    n=snapshot.child("email").getValue(String.class);
//                   Toast.makeText(getApplicationContext(),"in for a",Toast.LENGTH_LONG).show();
                    if(n.equals(name.getText().toString())){
//                        Toast.makeText(getApplicationContext(),"in if b",Toast.LENGTH_LONG).show();
                        phoneNumber=snapshot.child("phoneNum").getValue(String.class);
                        Log.d("111111","222"+phoneNumber);
                        flag=true;
                        Toast.makeText( ForgotPasswordActivity.this, "الرجاء الانتظار حتى يتم الارسال", Toast.LENGTH_SHORT ).show();
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                phoneNumber, 30, TimeUnit.SECONDS, ForgotPasswordActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                        //Called if it is not needed to enter verification code
                                        signInWithCredential( phoneAuthCredential );
                                    }

                                    @Override
                                    public void onVerificationFailed(FirebaseException e) {
                                        //incorrect phone number, verification code, emulator, etc.
                                        Toast.makeText( ForgotPasswordActivity.this, "onVerificationFailed " + e.getMessage(), Toast.LENGTH_SHORT ).show();
                                    }

                                    @Override
                                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        //now the code has been sent, save the verificationId we may need it
                                        super.onCodeSent( verificationId, forceResendingToken );

                                        mVerificationId = verificationId;
                                    }

                                    @Override
                                    public void onCodeAutoRetrievalTimeOut(String verificationId) {
                                        //called after timeout if onVerificationCompleted has not been called
                                        super.onCodeAutoRetrievalTimeOut( verificationId );
                                        Toast.makeText( ForgotPasswordActivity.this, "onCodeAutoRetrievalTimeOut :" + verificationId, Toast.LENGTH_SHORT ).show();
                                    }
                                }
                        );
//                        Toast.makeText(getApplicationContext(),"in if a",Toast.LENGTH_LONG).show();
                        break;
                    }else flag=false;
                }

            }

//                User user = dataSnapshot.getValue(User.class);
//                phoneNumber=user.getPhoneNum();



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

                            alert.setTitle("تمت عملية التسجيل بنجاح").setIcon(R.drawable.t1);
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
                            Intent intent = new Intent(ForgotPasswordActivity.this, ResetPassword.class);
                            intent.putExtra("name",name.getText().toString().trim().toString());
                            startActivity(intent);
//                            startActivity(new Intent(getApplicationContext(), ResetPassword.class));

                        } else {
                            AlertDialog.Builder alert = new AlertDialog.Builder(
                                    ForgotPasswordActivity.this);

                            alert.setTitle("يوجد خطأ في الرمز").setIcon(R.drawable.f1);
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


}
