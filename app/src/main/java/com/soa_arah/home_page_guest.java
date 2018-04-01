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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class home_page_guest extends AppCompatActivity {

    private Button button;
    private Button reg;
    private EditText searchtext;
    private DatabaseReference fData;
    private Button searchBtn;
    private ProgressDialog progressDialog;
    String id;
    String f;
    String cal;
    String img;
    boolean flag = false;
    private String addCal;
    String stand;
    String quantity;
    private Button scan;
    private ZXingScannerView scannerView;
    AlertDialog.Builder alert;
    String key;
    String []keyword;
    String []searchR;
    int size;
    ArrayList<String>list=new ArrayList<>();
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_guest_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            //ID موب صح
            //IT admin
            if(user.getUid().equals("aSK7RyMA8xfdaQNPF0xS6kAumam2")){
                startActivity(new Intent(getApplicationContext(),home_page_IT_admin.class));
            }
            // Nutrition addmin
            else if(user.getUid().equals("Pf7emnnQTEbmukAIDwWgkuv8JbC2")){
                startActivity(new Intent(getApplicationContext(),home_page_Nutrition_admin.class));
            }
            else {
                startActivity(new Intent(getApplicationContext(), home_page_register.class));

            }
        }
isConnected();
        scan = (Button) findViewById(R.id.scan);
        button = (Button) findViewById(R.id.login);
        reg = (Button) findViewById(R.id.register);
        searchtext = (EditText) findViewById(R.id.searchword);
        searchBtn = (Button) findViewById(R.id.searchButton);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("يتم البحث، الرجاء الانتظار ...");

        searchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                 searchKeyword();


            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                startActivity(new Intent(getApplicationContext(), Registration.class));
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
            }
        });

        onBackPressed();
    }
    @Override
    public void onBackPressed()
    {

        // super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.guest_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

         if (item.getItemId() == R.id.aboutUs) {
            startActivity(new Intent(getApplicationContext(), aboutUs.class));


        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void scanCode(View view) {
        startActivity(new Intent(getApplicationContext(), Barcode.class));
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
                        if (keyword[i].equals(searchtext.getText().toString().trim())) {
                            list.add(f);

                        }

                    }
                }
                progressDialog.dismiss();
                if (list.isEmpty()) {
                    alert = new AlertDialog.Builder(home_page_guest.this);
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
                    addCal="false";
                    intent.putExtra( "addCal" ,addCal );
                    progressDialog.dismiss();
                    startActivity(intent);

                }
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
