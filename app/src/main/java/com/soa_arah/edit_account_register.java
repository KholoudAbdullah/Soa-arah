package com.soa_arah;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class edit_account_register extends AppCompatActivity implements View.OnClickListener {

    private ImageButton edit_wight;
    private ImageButton edit_hight;
    private ImageButton edit_date;
    private ImageButton edit_gender;
    private ImageButton edit_waist;
    private ImageButton edit_hip;
    private EditText hight,waist,hip,wight;

    //firebase auth object
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_account_register_activity);

        edit_wight=(ImageButton)findViewById(R.id.edit_wight);
        edit_hight=(ImageButton)findViewById(R.id.edit_hight);
        edit_date=(ImageButton)findViewById(R.id.edit_date);
        edit_gender=(ImageButton)findViewById(R.id.edit_gender);
        edit_waist=(ImageButton)findViewById(R.id.edit_waist);
        edit_hip=(ImageButton)findViewById(R.id.edit_hip);

        hight=(EditText)findViewById(R.id.hight);
        waist=(EditText)findViewById(R.id.waist);
        hip=(EditText)findViewById(R.id.hip);
        wight=(EditText)findViewById(R.id.wight);

        edit_wight.setOnClickListener(this);
        edit_hight.setOnClickListener(this);
        edit_date.setOnClickListener(this);
        edit_gender.setOnClickListener(this);
        edit_waist.setOnClickListener(this);
        edit_hip.setOnClickListener(this);

        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        String User_ID = firebaseAuth.getCurrentUser().getEmail();
        String username =User_ID.substring(0,User_ID.lastIndexOf("@"));

        mDatabase= FirebaseDatabase.getInstance().getReference().child("RegisteredUser").child(username);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                RegisteredUser user = dataSnapshot.getValue(RegisteredUser.class);

                wight.setText(user.getWight());
                hight.setText(user.getHight());
                hip.setText(user.gethip());
                waist.setText(user.getWaist());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {

            if (view == edit_wight) {
                mDatabase.child("wight").setValue(wight.getText().toString());
                Toast.makeText(getApplicationContext(),"تم تغيير الوزن بنجاح",Toast.LENGTH_LONG).show();
            }
            if (view == edit_hight) {
                mDatabase.child("hight").setValue(hight.getText().toString());
                Toast.makeText(getApplicationContext(),"تم تغيير الطول بنجاح",Toast.LENGTH_LONG).show();
            }
            if (view == edit_date) {

            }
            if (view == edit_gender) {

            }
            if (view == edit_waist) {
                mDatabase.child("waist").setValue(waist.getText().toString());
                Toast.makeText(getApplicationContext(),"تم تغيير محيط الخصر بنجاح",Toast.LENGTH_LONG).show();
            }
            if (view == edit_hip) {
                mDatabase.child("hip").setValue(hip.getText().toString());
                Toast.makeText(getApplicationContext(),"تم تغيير محيط الفخذ بنجاح",Toast.LENGTH_LONG).show();
            }


    }


}
