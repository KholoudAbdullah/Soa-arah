package com.soa_arah;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class addCalories extends AppCompatActivity {
    EditText searchword;
    EditText calories,addCalText;
    private Button button,add;
    private Button reg;
    private EditText searchtext;
    private Button searchBtn;
    private ProgressDialog progressDialog;
    String f;
    String cal;
    String img;
    boolean flag = false;
    String stand;
    String quantity;
    private Button scan;
    AlertDialog.Builder alert;
    String key;
    String []keyword;
    int size;
    ArrayList<String> list=new ArrayList<>();
    private DatabaseReference fData;
    private FirebaseAuth firebaseAuth;
    double cal2;
    String username;
    private android.widget.Toast Toast;
    private DatabaseReference mDatabase;
    private String User_ID;
    String DailyCal,id,str;
    String addCal;
    Double caloriy;
    private Date date1,date2,date3,date4,date5,date6,date7,date;
    private String day1,day2,day3,day4,day5,day6,day7,calory,day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_calories);
        searchword=(EditText) findViewById(R.id.searchword);
        calories=(EditText)findViewById(R.id.add);
        searchBtn=(Button)findViewById(R.id.searchButton);
        addCalText=(EditText)findViewById(R.id.add);
        add=(Button)findViewById(R.id.addB);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("يتم البحث، الرجاء الانتظار ...");

        searchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchKeyword();


            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        User_ID = firebaseAuth.getCurrentUser().getEmail();
        username= User_ID.substring( 0, User_ID.lastIndexOf( "@" ) );

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCal();
            }
        });
    }
    public void addCal(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Progress");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DailyCal=dataSnapshot.child(username).child("startDate").getValue(String.class);
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                Calendar c=Calendar.getInstance();
                try{
                    date=Calendar.getInstance().getTime();
                    day = df.format(date);
                    c.setTime(date);
                    c.setTime(df.parse(DailyCal));
                    day1 = df.format(c.getTime());  // dt is now the new date

                    c.setTime(df.parse(DailyCal));
                    c.add(Calendar.DATE, 1);  // number of days to add
                    day2 = df.format(c.getTime());

                    c.setTime(df.parse(DailyCal));
                    c.add(Calendar.DATE, 2);  // number of days to add
                    day3 = df.format(c.getTime());

                    c.setTime(df.parse(DailyCal));
                    c.add(Calendar.DATE, 3);  // number of days to add
                    day4 = df.format(c.getTime());

                    c.setTime(df.parse(DailyCal));
                    c.add(Calendar.DATE, 4);  // number of days to add
                    day5 = df.format(c.getTime());

                    c.setTime(df.parse(DailyCal));
                    c.add(Calendar.DATE, 5);  // number of days to add
                    day6 = df.format(c.getTime());

                    c.setTime(df.parse(DailyCal));
                    c.add(Calendar.DATE, 6);  // number of days to add
                    day7 = df.format(c.getTime());
                }
                catch (Exception e){}
                if(day.equals(day1)){
                    str="j";
                    calory=dataSnapshot.child(username).child("day1").getValue(String.class);
                    cal2=Double.parseDouble(calory);
                    cal2+=Double.parseDouble(addCalText.getText().toString());
                    str=Double.toString(cal2);
                    mDatabase.child(username).child("day1").setValue(str);

                }
                else if(day.equals(day2)){
                    str="j";
                    calory=dataSnapshot.child(username).child("day2").getValue(String.class);
                    cal2=Double.parseDouble(calory);
                    cal2+=Double.parseDouble(addCalText.getText().toString());
                    str=Double.toString(cal2);
                    mDatabase.child(username).child("day2").setValue(str);


                }else if(day.equals(day3)){
                    str="j";
                    calory=dataSnapshot.child(username).child("day3").getValue(String.class);
                    cal2=Double.parseDouble(calory);
                    cal2+=Double.parseDouble(addCalText.getText().toString());
                    str=Double.toString(cal2);
                    mDatabase.child(username).child("day3").setValue(str);


                }else if(day.equals(day4)){
                    str="j";
                    calory=dataSnapshot.child(username).child("day4").getValue(String.class);
                    cal2=Double.parseDouble(calory);
                    cal2+=Double.parseDouble(addCalText.getText().toString());
                    str=Double.toString(cal2);
                    mDatabase.child(username).child("day4").setValue(str);


                }
                else if(day.equals(day5)){
                    str="j";
                    calory=dataSnapshot.child(username).child("day5").getValue(String.class);
                    cal2=Double.parseDouble(calory);
                    cal2+=Double.parseDouble(addCalText.getText().toString());
                    str=Double.toString(cal2);
                    mDatabase.child(username).child("day5").setValue(str);


                }
                else if(day.equals(day6)){
                    str="j";
                    calory=dataSnapshot.child(username).child("day6").getValue(String.class);
                    cal2=Double.parseDouble(calory);
                    cal2+=Double.parseDouble(addCalText.getText().toString());
                    str=Double.toString(cal2);
                    mDatabase.child(username).child("day6").setValue(str);


                }
                else if(day.equals(day7)){
                    str="j";
                    calory=dataSnapshot.child(username).child("day7").getValue(String.class);
                    cal2=Double.parseDouble(calory);
                    cal2+=Double.parseDouble(addCalText.getText().toString());
                    str=Double.toString(cal2);
                    mDatabase.child(username).child("day7").setValue(str);


                }else
                {
                    mDatabase.child(username).child("day1").setValue("0");
                    mDatabase.child(username).child("day2").setValue("0");
                    mDatabase.child(username).child("day3").setValue("0");
                    mDatabase.child(username).child("day4").setValue("0");
                    mDatabase.child(username).child("day5").setValue("0");
                    mDatabase.child(username).child("day6").setValue("0");
                    mDatabase.child(username).child("day7").setValue("0");
                    mDatabase.child(username).child("startDate").setValue(day);
                    str="j";
                    cal2=Double.parseDouble(addCalText.getText().toString());
                    str=Double.toString(cal2);
                    mDatabase.child(username).child("day1").setValue(str);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void scanCode (View view) {
        //startActivity(new Intent(getApplicationContext(), BarcodeToAddCalories.class));
    }

    public void searchKeyword() {
        list.clear();
        progressDialog.show();
        fData = FirebaseDatabase.getInstance().getReference().child("Food");
        fData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    key = snapshot.child("keyword").getValue(String.class);
                    f = snapshot.child("name").getValue(String.class);
                    id = snapshot.getKey();
                    keyword = key.split(",");
                    int j = 0;
                    for (int i = 0; i < keyword.length; i++) {
                        if (keyword[i].equals(searchword.getText().toString().trim())) {
                            list.add(f);

                        }

                    }
                }
                progressDialog.dismiss();
                if (list.isEmpty()) {
                    alert = new AlertDialog.Builder(addCalories.this);
                    alert.setMessage("عذراً لايوجد هذا الصنف سجل دخولك لإضافة");
                    alert.setCancelable(true);
                    alert.setPositiveButton(
                            "سجل الدخول",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    startActivity(new Intent(getApplicationContext(), LoginPage.class));

                                }
                            });

                    alert.setNegativeButton(
                            "إلغاء",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alert11 = alert.create();
                    alert11.show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), searchByKeyword.class);
                    intent.putExtra("list", list);
                    addCal="true";
                    intent.putExtra("addCal",addCal);
                    progressDialog.dismiss();
                    startActivity(intent);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
