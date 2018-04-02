package com.soa_arah;

import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
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

    }

    public void addReview(View view){
        mAuth = FirebaseAuth.getInstance();


                if (mAuth.getCurrentUser() != null) {
                    R_key = mAuth.getCurrentUser().getEmail();
                    key =R_key.substring(0,R_key.lastIndexOf("@"));
//                    dbRef1=FirebaseDatabase.getInstance().getReference().child("RegisteredUser");
//                    name=dbRef1.child(key).child("name").
                    dbRef1 = FirebaseDatabase.getInstance().getReference().child("RegisteredUser").child(key);
                    dbRef1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            name = (String) dataSnapshot.child("name").getValue();


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }

                    });

                    dbRef= FirebaseDatabase.getInstance().getReference("Review").child(getIntent().getStringExtra("name")).push();
                    dbRef.child("comment").setValue(comment.getText().toString());
                    dbRef.child("RKey").setValue(key);
                    dbRef.child("numLike").setValue("0");
                    dbRef.child("numDisLike").setValue("0");
                    dbRef.child("writer").setValue(name);
                }
                else{}




    }
}
