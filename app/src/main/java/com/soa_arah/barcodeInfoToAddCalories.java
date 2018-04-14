package com.soa_arah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class barcodeInfoToAddCalories extends AppCompatActivity {
    String cal, img, table, q, name, bar, f, id,DailyCal,username,User_ID,str;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private Date date1,date2,date3,date4,date5,date6,date7,date;
    private String day1,day2,day3,day4,day5,day6,day7,calory,day;
    private Double cal2;
    private Button add;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_info_to_add_calories);
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );

        isConnected();

        progressDialog = new ProgressDialog(barcodeInfoToAddCalories.this);
        // Setting progressDialog Title.
        progressDialog.setMessage("الرجاء الانتظار ...");
        progressDialog.show();

        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAuth = FirebaseAuth.getInstance();
        User_ID = firebaseAuth.getCurrentUser().getEmail();
        username= User_ID.substring( 0, User_ID.lastIndexOf( "@" ) );
        add=(Button)findViewById( R.id.add );
        bar = getIntent().getStringExtra( "bar" );
        name = getIntent().getStringExtra( "Name" );
        cal = getIntent().getStringExtra( "cal" );
        img = getIntent().getStringExtra( "image" );
        table = getIntent().getStringExtra( "table" );
        q = getIntent().getStringExtra( "q" );
        TextView textn = (TextView) findViewById( R.id.textView9 );
        textn.setText( bar );
        TextView textC = (TextView) findViewById( R.id.textView12 );
        ImageView imageView = (ImageView) findViewById( R.id.imageView7 );
        Glide.with( getApplicationContext() ).load( img ).into( imageView );
        textn.setText( name );
        // Hiding the progressDialog after done uploading.
        progressDialog.dismiss();

        if (cal.equals( "لا يوجد" )) {
            textC.setText( "خالي من السعرات الحرارية" );
        } else
            textC.setText( cal.toString() + " سعرة حرارية" );
        onBackPressed();
        add.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCal();
            }
        } );
    }
    @Override
    public void onBackPressed()
    {

        // super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
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

                    date1=df.parse(day1);
                    date2=df.parse(day2);
                    date3=df.parse(day3);
                    date4=df.parse(day4);
                    date5=df.parse(day5);
                    date6=df.parse(day6);
                    date7=df.parse(day7);


                }
                catch (Exception e){}
                if(day.equals(day1)){
                    str="j";
                    calory=dataSnapshot.child(username).child("day1").getValue(String.class);
                    cal2=Double.parseDouble(calory);
                    cal2+=Double.parseDouble(cal);
                    str=Double.toString(cal2);
                    mDatabase.child(username).child("day1").setValue(str);

                }
                else if(day.equals(day2)){
                    str="j";
                    calory=dataSnapshot.child(username).child("day2").getValue(String.class);
                    cal2=Double.parseDouble(calory);
                    cal2+=Double.parseDouble(cal);
                    str=Double.toString(cal2);
                    mDatabase.child(username).child("day2").setValue(str);


                }else if(day.equals(day3)){
                    str="j";
                    calory=dataSnapshot.child(username).child("day3").getValue(String.class);
                    cal2=Double.parseDouble(calory);
                    cal2+=Double.parseDouble(cal);
                    str=Double.toString(cal2);
                    mDatabase.child(username).child("day3").setValue(str);


                }else if(day.equals(day4)){
                    str="j";
                    calory=dataSnapshot.child(username).child("day4").getValue(String.class);
                    cal2=Double.parseDouble(calory);
                    cal2+=Double.parseDouble(cal);
                    str=Double.toString(cal2);
                    mDatabase.child(username).child("day4").setValue(str);


                }
                else if(day.equals(day5)){
                    str="j";
                    calory=dataSnapshot.child(username).child("day5").getValue(String.class);
                    cal2=Double.parseDouble(calory);
                    cal2+=Double.parseDouble(cal);
                    str=Double.toString(cal2);
                    mDatabase.child(username).child("day5").setValue(str);


                }
                else if(day.equals(day6)){
                    str="j";
                    calory=dataSnapshot.child(username).child("day6").getValue(String.class);
                    cal2=Double.parseDouble(calory);
                    cal2+=Double.parseDouble(cal);
                    str=Double.toString(cal2);
                    mDatabase.child(username).child("day6").setValue(str);


                }
                else if(day.equals(day7)){
                    str="j";
                    calory=dataSnapshot.child(username).child("day7").getValue(String.class);
                    cal2=Double.parseDouble(calory);
                    cal2+=Double.parseDouble(cal);
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
                    cal2=Double.parseDouble(cal);
                    str=Double.toString(cal2);
                    mDatabase.child(username).child("day1").setValue(str);

                }
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        barcodeInfoToAddCalories.this);
                alert.setTitle("تم أضافة السعرات الحرارية بنجاح").setIcon(R.drawable.t1);
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
                startActivity(new Intent(getApplicationContext(), diet_plan.class));
                //close this activity
                finish();
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




}
