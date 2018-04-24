package com.soa_arah;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class addCalories extends AppCompatActivity {
    EditText searchword;
    EditText calories,addCalText;
    private Button button,add;
    private Button reg;
    private EditText searchtext;
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
    String addCal="true";
    Double caloriy;
    private Date date;
    private String day1,day2,day3,day4,day5,day6,day7,calory,day;
    ArrayList<String>foods=new ArrayList<>();
    private ListView listView;
    ArrayAdapter<String> adapter;
    boolean flag2=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_calories);
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );

        isConnected();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        scan=(Button)findViewById( R.id.scan );
        searchtext=(EditText) findViewById(R.id.searchword);
        //calories=(EditText)findViewById(R.id.add);
        addCalText=(EditText)findViewById(R.id.add);
        add=(Button)findViewById(R.id.addB);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("يتم البحث، الرجاء الانتظار ...");
        listView=(ListView)findViewById(R.id.listview);
        searchKeyword();
        listView.setTextFilterEnabled(true);
        listView.setTextFilterEnabled(true);
        listView.bringToFront();
        searchtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!searchtext.getText().toString().equals("")){

                    listView.setVisibility(View.VISIBLE);
                    addCalories.this.adapter.getFilter().filter(s);
                    adapter.notifyDataSetChanged();

                }
                else {

                    listView.setVisibility(View.GONE);


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if(searchtext.getText().toString().length()<=0){

            listView.setVisibility(View.GONE);


        }
        scan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

               startActivity(new Intent(getApplicationContext(), BarcodeToAddCalories.class));

            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        User_ID = firebaseAuth.getCurrentUser().getEmail();
        username= User_ID.substring( 0, User_ID.lastIndexOf( "@" ) );

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addCalText.getText().toString().trim().length()<1){
                    alert= new android.app.AlertDialog.Builder(addCalories.this);
                    alert.setMessage("الرجاء ادخال عدد السعرات");
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

                }else
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
                    mDatabase.child(username).child("water1").setValue("0");
                    mDatabase.child(username).child("water2").setValue("0");
                    mDatabase.child(username).child("water3").setValue("0");
                    mDatabase.child(username).child("water4").setValue("0");
                    mDatabase.child(username).child("water5").setValue("0");
                    mDatabase.child(username).child("water6").setValue("0");
                    mDatabase.child(username).child("water7").setValue("0");
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
                android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(
                        addCalories.this);
                alert.setTitle("تم أضافة السعرات الحرارية بنجاح").setIcon(R.drawable.t1);
                android.support.v7.app.AlertDialog dialog = alert.create();

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
                startActivity(new Intent(getApplicationContext(), diet_plan.class));
                //close this activity
                finish();
                //opening login activity

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public boolean addCalories(float caloriy){
        float cal=1000;
       cal+=caloriy;
        return true;
    }
    public void scanCode (View view) {
        //startActivity(new Intent(getApplicationContext(), BarcodeToAddCalories.class));
    }
    public void searchKeyword(){

        fData = FirebaseDatabase.getInstance().getReference().child("Food");
        fData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    f = snapshot.child("name").getValue(String.class);
                    foods.add(f);

                }
                adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.keywordlayout,R.id.textView14,foods );
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view,final int position, long id) {
                        //textView=(TextView)view;
                        progressDialog.show();
                        fData = FirebaseDatabase.getInstance().getReference().child("Food");
                        final String selItem = (String) listView.getSelectedItem(); //
                        fData.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    f = snapshot.child("name").getValue(String.class);
                                    if (f.equals(listView.getItemAtPosition(position))) {

                                        cal = snapshot.child("calories").getValue(String.class);
                                        img = snapshot.child("image").getValue(String.class);
                                        stand = snapshot.child("standard").getValue(String.class);
                                        quantity = snapshot.child("quantity").getValue(String.class);
                                        if(addCal.equals("true")) {
                                            Intent intent = new Intent(getApplicationContext(), searchByNameToAddCalories.class);
                                            intent.putExtra("name", f);
                                            intent.putExtra("cal", cal);
                                            intent.putExtra("img", img);
                                            intent.putExtra("stand", stand);
                                            intent.putExtra("quantity", quantity);
                                            progressDialog.dismiss();
                                            startActivity(intent);
                                            break;
                                        }
                                        else{
                                            Intent intent = new Intent(getApplicationContext(), searchByName.class);
                                            intent.putExtra("name", f);
                                            intent.putExtra("cal", cal);
                                            intent.putExtra("img", img);
                                            intent.putExtra("stand", stand);
                                            intent.putExtra("quantity", quantity);
                                            progressDialog.dismiss();
                                            startActivity(intent);
                                            break;
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.home_1, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.Home) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


            if (user != null) {
                String id = user.getUid();
                //IT admin
                if(id.equals("kstgUKiRA7T3p1NNl3GuGBHgvcf2")){
                    startActivity(new Intent(getApplicationContext(),home_page_IT_admin.class));
                }
                // Nutrition addmin
                else if(id.equals("Pf7emnnQTEbmukAIDwWgkuv8JbC2")){
                    startActivity(new Intent(getApplicationContext(),home_page_Nutrition_admin.class));
                }
                else {
                    startActivity(new Intent(getApplicationContext(), home_page_register.class));

                }

            } else {
                startActivity( new Intent( getApplicationContext(), home_page_guest.class ) );

            }
        } else {
            return super.onOptionsItemSelected( item );
        }
        return true;

    }


}
