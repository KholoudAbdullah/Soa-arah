package com.soa_arah;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;


public class edit_account_register extends AppCompatActivity implements View.OnClickListener {

    private ImageButton edit_wight;
    private ImageButton edit_hight;
    private ImageButton edit_date;
    private ImageButton edit_gender;
    private ImageButton edit_waist;
    private ImageButton edit_hip;
    private EditText hight,waist,hip,wight;
    private  ProgressDialog progressDialog;
    private Spinner gen;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    String date;
    String record="";
    String gender[]={"أنثى","ذكر"};
    ArrayAdapter<String> adapter;
    //firebase auth object
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_account_register_activity);
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );
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

                if(user.getWight().equals("لم يتم إدخال بيانات"))
                    wight.setHint("لم يتم الإدخال");
                else
                    wight.setText(user.getWight());

                if(user.getHight().equals("لم يتم إدخال بيانات"))
                    hight.setHint("لم يتم الإدخال");
                else
                    hight.setText(user.getHight());

                if(user.gethip().equals("لم يتم إدخال بيانات"))
                    hip.setHint("لم يتم الإدخال");
                else
                    hip.setText(user.gethip());

                if(user.getWaist().equals("لم يتم إدخال بيانات"))
                    waist.setHint("لم يتم الإدخال");
                else
                    waist.setText(user.getWaist());


                mDisplayDate.setText(user.getDateOfBarth());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        gen=(Spinner)findViewById(R.id.gen);
        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,gender);
        gen.setAdapter(adapter);
        gen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        record = "أنثى";
                        break;
                    case 1:
                        record = "ذكر";
                        break;


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        progressDialog = new ProgressDialog(this);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        edit_account_register.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                date = day + "/" + month + "/" + year;
                mDisplayDate.setText(date);
            }
        };


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.Navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.search:
                                startActivity(new Intent(getApplicationContext(), home_page_register.class));
                                break;
                            case R.id.diet_plan:
                                startActivity(new Intent(getApplicationContext(), diet_plan.class));

                                break;
                            case R.id.upload:
                                startActivity(new Intent(getApplicationContext(), Request_page.class));

                                break;
                        }
                        return false;
                    }
                });

    }

    @Override
    public void onClick(View view) {

            if (view == edit_wight) {
                if(wight.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(),"لم يتم إدخال الوزن",Toast.LENGTH_LONG).show();
                else{
                mDatabase.child("wight").setValue(wight.getText().toString());
                Toast.makeText(getApplicationContext(),"تم تغيير الوزن بنجاح",Toast.LENGTH_LONG).show();}
            }
            if (view == edit_hight) {
                if(hight.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(),"لم يتم إدخال الطول",Toast.LENGTH_LONG).show();
                else{
                mDatabase.child("hight").setValue(hight.getText().toString());
                Toast.makeText(getApplicationContext(),"تم تغيير الطول بنجاح",Toast.LENGTH_LONG).show();}
            }
            if (view == edit_date) {
                mDatabase.child("dateOfBarth").setValue(mDisplayDate.getText().toString());
                Toast.makeText(getApplicationContext(),"تم تغيير تاريخ الميلاد بنجاح",Toast.LENGTH_LONG).show();
            }
            if (view == edit_gender) {
                mDatabase.child("gender").setValue(record);
                Toast.makeText(getApplicationContext(),"تم تغيير الجنس بنجاح",Toast.LENGTH_LONG).show();

            }
            if (view == edit_waist) {
                if(waist.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(),"لم يتم إدخال محيط الخصر",Toast.LENGTH_LONG).show();
                else{
                mDatabase.child("waist").setValue(waist.getText().toString());
                Toast.makeText(getApplicationContext(),"تم تغيير محيط الخصر بنجاح",Toast.LENGTH_LONG).show();}
            }
            if (view == edit_hip) {
                if(hip.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(),"لم يتم إدخال محيط الفخذ",Toast.LENGTH_LONG).show();
                else{
                mDatabase.child("hip").setValue(hip.getText().toString());
                Toast.makeText(getApplicationContext(),"تم تغيير محيط الفخذ بنجاح",Toast.LENGTH_LONG).show();}
            }



    BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.Navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.search:
                    startActivity(new Intent(getApplicationContext(), home_page_register.class));
                    break;
                case R.id.diet_plan:
                    //startActivity(new Intent(getApplicationContext(), edit_account_register.class));

                    break;
                case R.id.upload:
                    startActivity(new Intent(getApplicationContext(), Request_page.class));

                    break;
            }
            return false;
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
            startActivity(new Intent(getApplicationContext(), home_page_guest.class));

        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }


}
