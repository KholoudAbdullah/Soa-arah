package com.soa_arah;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class view_account_register  extends AppCompatActivity {

    private TextView username,phone,wight,hight,date,gender,waist;
    private String User_ID;
    private TextView hip;
    private RegisteredUser user;

    //firebase auth object
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_account_register_activity);
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );
        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        username = (TextView) findViewById(R.id.username);
        phone = (TextView) findViewById(R.id.phone);
        wight = (TextView) findViewById(R.id.wight);
        hight = (TextView) findViewById(R.id.hight);
        date = (TextView) findViewById(R.id.date);
        gender = (TextView) findViewById(R.id.gender);
        waist = (TextView) findViewById(R.id.waist);
        hip = (TextView) findViewById(R.id.hip);
        User_ID = firebaseAuth.getCurrentUser().getEmail();
        String na =User_ID.substring(0,User_ID.lastIndexOf("@"));


        mDatabase1 = FirebaseDatabase.getInstance().getReference().child("RegisteredUser").child(na);


        mDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                user = dataSnapshot.getValue(RegisteredUser.class);
                username.setText(user.getName());
                phone.setText(user.getPhoneNum());
                wight.setText(user.getWight());
                hight.setText(user.getHight());
                date.setText(user.getDateOfBarth());
                gender.setText(user.getGender());
                hip.setText(user.gethip());
                waist.setText(user.getWaist());



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.viewaccount) {
            startActivity(new Intent(getApplicationContext(), view_account_register.class));
        } else if (item.getItemId() == R.id.editaccount) {
            startActivity(new Intent(getApplicationContext(), edit_account_register.class));
        } else if (item.getItemId() == R.id.Logout){
            firebaseAuth.signOut();
            //closing activity
            finish();
            startActivity(new Intent(getApplicationContext(), LoginPage.class));

        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
