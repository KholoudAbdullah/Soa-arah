package com.soa_arah;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class home_page_Nutrition_admin extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private Button button;
    private Button reg;
    AlertDialog.Builder alert;
    private String addCal="false";
    ArrayList<String>foods=new ArrayList<>();
    private ListView listView;
    TextView textView;
    ArrayAdapter<String> adapter;
    private String quantity;
    String key;
    String []keyword;
    String []searchR;
    private ArrayList<Food> re_name;
    private ArrayList<String> key1;
    int size;

    ArrayList<String> list=new ArrayList<>();
    private EditText searchtext;
    private DatabaseReference fData;
    private Button searchBtn;
    private ProgressDialog progressDialog;
    String id;
    String f;
    String cal;
    String img;
    boolean flag;
    String stand;
    String grams;
    private Button scan,searchbtn;
    private Integer sumreq=0;
    private DatabaseReference mDatabaseReference;
    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_nutrition_admin_activity);
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );
        searchtext = (EditText) findViewById(R.id.searchword);
        searchbtn=(Button)findViewById(R.id.searchbtn);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        scan=(Button)findViewById(R.id.scan);
        isConnected();
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Barcode.class));

            }
        });
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Requests").child("ByName");

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    sumreq=sumreq+1;
                }


                AlarmManager alarmManager = (AlarmManager) getSystemService( Context.ALARM_SERVICE);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user != null){
                    //ID موب صح
                    //IT admin
                    if(user.getUid().equals("aSK7RyMA8xfdaQNPF0xS6kAumam2")){
                        startActivity(new Intent(getApplicationContext(),home_page_IT_admin.class));
                    }
                    // Nutrition addmin
                    else if(user.getUid().equals("Pf7emnnQTEbmukAIDwWgkuv8JbC2")){
                        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
                        notificationIntent.putExtra( "SumRequest",sumreq );
                        notificationIntent.addCategory("android.intent.category.DEFAULT");
                        PendingIntent broadcast = PendingIntent.getBroadcast(home_page_Nutrition_admin.this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                        Calendar cal = Calendar.getInstance();
//                        cal.add(Calendar.SECOND, 15);
//                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
                        Calendar calendar = Calendar.getInstance();
                        Calendar calendarcurrent = Calendar.getInstance();
                        calendarcurrent.setTimeInMillis(System.currentTimeMillis());
                        calendar.set(Calendar.HOUR_OF_DAY,10);
                        calendar.set(Calendar.MINUTE,5);
                        calendar.set(Calendar.SECOND, 0);
                        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY*7, broadcast);
                        if(System.currentTimeMillis()>calendar.getTimeInMillis()) {
                            alarmManager.cancel( broadcast );
                        }
                    }
                    else {
                        startActivity(new Intent(getApplicationContext(), home_page_register.class));

                    }
                }





            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Requests").child("ByBarcode");

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    sumreq=sumreq+1;
                }

                AlarmManager alarmManager = (AlarmManager) getSystemService( Context.ALARM_SERVICE);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user != null){
                    //ID موب صح
                    //IT admin
                    if(user.getUid().equals("aSK7RyMA8xfdaQNPF0xS6kAumam2")){
                        startActivity(new Intent(getApplicationContext(),home_page_IT_admin.class));
                    }
                    // Nutrition addmin
                    else if(user.getUid().equals("Pf7emnnQTEbmukAIDwWgkuv8JbC2")){
                        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
                        notificationIntent.putExtra( "SumRequest",sumreq );
                        notificationIntent.addCategory("android.intent.category.DEFAULT");
                        PendingIntent broadcast = PendingIntent.getBroadcast(home_page_Nutrition_admin.this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                        Calendar cal = Calendar.getInstance();
//                        cal.add(Calendar.SECOND, 15);
//                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
                        Calendar calendar = Calendar.getInstance();
                        Calendar calendarcurrent = Calendar.getInstance();
                        calendarcurrent.setTimeInMillis(System.currentTimeMillis());
                        calendar.set(Calendar.HOUR_OF_DAY,10);
                        calendar.set(Calendar.MINUTE,5);
                        calendar.set(Calendar.SECOND, 0);

                        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY*7, broadcast);
                        if(System.currentTimeMillis()>calendar.getTimeInMillis()) {
                            alarmManager.cancel( broadcast );
                        }
                    }
                    else {
                        startActivity(new Intent(getApplicationContext(), home_page_register.class));

                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listView=(ListView)findViewById(R.id.listview);
        searchKeyword();
        listView.setTextFilterEnabled(true);
        listView.setTextFilterEnabled(true);

        searchtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!searchtext.getText().toString().equals("")){

                    listView.setVisibility(View.VISIBLE);
                    home_page_Nutrition_admin.this.adapter.getFilter().filter(s);
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




//
        firebaseAuth = FirebaseAuth.getInstance();

        searchtext=(EditText)findViewById(R.id.searchword);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("يتم البحث، الرجاء الانتظار ...");


        //menu bottom bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.Navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.search:
                                startActivity(new Intent(getApplicationContext(), home_page_Nutrition_admin.class));
                                break;
                            case R.id.request:
                                startActivity(new Intent(getApplicationContext(), view_request.class));

                                break;
                            case R.id.account:
                                startActivity(new Intent(getApplicationContext(), account_Nutrition_admin.class));

                                break;
                        }
                        return false;
                    }
                });




        onBackPressed();
    }
    @Override
    public void onBackPressed()
    {

        // super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
    }
    public void scanCode (View view) {
        startActivity(new Intent(getApplicationContext(), Barcode.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.adminloguot,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.aboutUs) {
            startActivity(new Intent(getApplicationContext(), aboutUs.class));
    }else if (item.getItemId() == R.id.Logout){
            firebaseAuth.signOut();
            //closing activity
            finish();
            startActivity(new Intent(getApplicationContext(), LoginPage.class));

        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
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
                listView.bringChildToFront(listView);
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
    public void search(){
        firebaseAuth = FirebaseAuth.getInstance();
        fData = FirebaseDatabase.getInstance().getReference().child("Food");
        fData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    f = snapshot.child("name").getValue(String.class);
                    id = snapshot.getKey();
                    if (f.equals(searchtext.getText().toString().trim())) {
                        cal = snapshot.child("calories").getValue(String.class);
                        img = snapshot.child("image").getValue(String.class);
                        stand = snapshot.child("standard").getValue(String.class);
                        quantity = snapshot.child("quantity").getValue(String.class);
                        if(addCal.equals("true")) {
                            Intent intent = new Intent(getApplicationContext(), searchByNameToAddCalories.class);
                            intent.putExtra("name", f);
                            intent.putExtra("id", id);
                            intent.putExtra("cal", cal);
                            intent.putExtra("img", img);
                            intent.putExtra("stand", stand);
                            intent.putExtra("quantity", quantity);
                            progressDialog.dismiss();
                            flag=true;
                            startActivity(intent);
                            break;
                        }
                        else{
                            Intent intent = new Intent(getApplicationContext(), searchByName.class);
                            intent.putExtra("name", f);
                            intent.putExtra("id", id);
                            intent.putExtra("cal", cal);
                            intent.putExtra("img", img);
                            intent.putExtra("stand", stand);
                            intent.putExtra("quantity", quantity);
                            progressDialog.dismiss();
                            flag=true;
                            startActivity(intent);
                            break;
                        }
                    }else  flag=false;
                }
                if(!flag){
                    if(firebaseAuth.getCurrentUser()==null){
                        alert=new AlertDialog.Builder(home_page_Nutrition_admin.this);
                        alert.setMessage("عذراً لايوجد هذا المنتج سجل دخولك لإضافته");
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
                                "الغاء",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                        startActivity(new Intent(getApplicationContext(), home_page_guest.class));

                                    }
                                }

                        );}
                    else {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String id= user.getUid();
                        if(id.equals("Pf7emnnQTEbmukAIDwWgkuv8JbC2")){
                            alert=new AlertDialog.Builder(home_page_Nutrition_admin.this);
                            alert.setTitle("عذراً لايوجد هذا المنتج ");
                            alert.setMessage("التحقق من الطلبات المرسلة");
                            alert.setCancelable(true);
                            alert.setPositiveButton("تحقق", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                            startActivity(new Intent(getApplicationContext(),view_request.class));

                                        }
                                    }

                            );
                            alert.setPositiveButton("الغاء", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                            startActivity(new Intent(getApplicationContext(),home_page_Nutrition_admin.class));

                                        }
                                    }

                            );
                        }
                        else{
                            if(id.equals("kstgUKiRA7T3p1NNl3GuGBHgvcf2")){
                                alert=new AlertDialog.Builder(home_page_Nutrition_admin.this);
                                alert.setMessage("عذراً لايوجد هذا المنتج ");
                                alert.setCancelable(true);
                                alert.setPositiveButton(
                                        "الغاء",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                                startActivity(new Intent(getApplicationContext(),home_page_IT_admin.class));

                                            }
                                        }

                                );
                            }
                            else
                            {if(firebaseAuth.getCurrentUser()!=null){
                                alert=new AlertDialog.Builder(home_page_Nutrition_admin.this);
                                alert.setMessage("عذراً لايوجد هاذا المنتج هل تريد اضافتة");
                                alert.setCancelable(true);
                                alert.setPositiveButton(
                                        "اضافة",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                startActivity(new Intent(getApplicationContext(), Request_page.class));

                                            }
                                        });

                                alert.setNegativeButton(
                                        "الغاء",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                                startActivity(new Intent(getApplicationContext(), home_page_register.class));

                                            }
                                        }

                                );
                            }}

                        }
                    }
                    AlertDialog alert11 = alert.create();
                    alert11.show();


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}