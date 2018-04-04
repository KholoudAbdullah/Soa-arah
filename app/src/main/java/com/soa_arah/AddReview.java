package com.soa_arah;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddReview extends AppCompatActivity {

    private EditText comment;
    private Button send;

    private DatabaseReference dbRef;
    private DatabaseReference dbRef1;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String R_key;
    private String key;

    private RegisteredUser user;

    private String name;
    android.app.AlertDialog.Builder alert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );


        comment= (EditText) findViewById(R.id.comment);


//        mAuth = FirebaseAuth.getInstance();
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                if (firebaseAuth.getCurrentUser() != null) {
//                    R_key = mAuth.getCurrentUser().getEmail();
//                    key =R_key.substring(0,R_key.lastIndexOf("@"));
//                }
//                else{}
//            }
//        };


        comment.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (comment.getText().toString().trim().length()<1){

                    comment.setError("الرجاء كتابة التعليق");
                }
            }
        });

    }

    public void addReview(View view){

        if(comment.getText().toString().trim().length()<1){
            alert= new android.app.AlertDialog.Builder(AddReview.this);
            alert.setMessage("الرجاء كتابة التعليق");
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

        }else {
        mAuth = FirebaseAuth.getInstance();



                    R_key = mAuth.getCurrentUser().getEmail();
                    key =R_key.substring(0,R_key.lastIndexOf("@"));
//                    dbRef1=FirebaseDatabase.getInstance().getReference().child("RegisteredUser");
//                    name=dbRef1.child(key).child("name").
                    dbRef1 = FirebaseDatabase.getInstance().getReference().child("RegisteredUser").child(key);
                    dbRef1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            name =dataSnapshot.child("name").getValue().toString();
                            dbRef= FirebaseDatabase.getInstance().getReference("Review").child(getIntent().getStringExtra("name")).push();
                            dbRef.child("comment").setValue(comment.getText().toString());
                            dbRef.child("RKey").setValue(key);
                            dbRef.child("numLike").setValue("0");
                            dbRef.child("numDisLike").setValue("0");
                            dbRef.child("writer").setValue(name);

                            AlertDialog.Builder alert = new AlertDialog.Builder(
                                    AddReview.this);
                            alert.setTitle("تم إضافة التعليق").setIcon(R.drawable.t1);

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
                            Intent intent = new Intent(AddReview.this, ViewReviewRegisterUser.class);
                            intent.putExtra("name", getIntent().getStringExtra("name"));
                            startActivity(intent);
                            //close this activity
                            finish();
                            //opening login activity


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }

                    });

//                    dbRef= FirebaseDatabase.getInstance().getReference("Review").child(getIntent().getStringExtra("name")).push();
//                    dbRef.child("comment").setValue(comment.getText().toString());
//                    dbRef.child("RKey").setValue(key);
//                    dbRef.child("numLike").setValue("0");
//                    dbRef.child("numDisLike").setValue("0");
//                        dbRef.child("writer").setValue(name);









    }


}}
