package com.soa_arah;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

                            alert= new android.app.AlertDialog.Builder(AddReview.this);
                            alert.setMessage("تم إضافة التعليق");
                            alert.setCancelable(true);
                            alert.setPositiveButton(
                                    "موافق",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            Intent intent = new Intent(AddReview.this, ViewReviewRegisterUser.class);
                                            intent.putExtra("name", getIntent().getStringExtra("name"));
                                            startActivity(intent);




                                        }
                                    });
                            android.app.AlertDialog alert11 = alert.create();
                            alert11.show();


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
