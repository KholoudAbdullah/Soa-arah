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
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Lama on 31/01/18.
 */

public class home_page_register extends AppCompatActivity{

    private FirebaseAuth firebaseAuth;
    private Button button;
    private Button reg;
    AlertDialog.Builder alert;
    String key;
    String []keyword;
    private String addCal="false";
    String []searchR;
    int size;
    ArrayList<String> list=new ArrayList<>();
    private EditText searchtext;
    private DatabaseReference fData;
    private ProgressDialog progressDialog;
    String id;
    String f;
    String cal;
    String img;
    boolean flag;
    String stand;
    String grams;
    private Button scan;
    private DatabaseReference ref1,ref2;
    private ZXingScannerView scannerView;
    ArrayList<String>foods=new ArrayList<>();
    private ListView listView;
    ArrayAdapter<String> adapter;
    private String quantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_register_activity);
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );
        isConnected();

        searchtext=(EditText)findViewById(R.id.searchword);

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
                    home_page_register.this.adapter.getFilter().filter(s);
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

        firebaseAuth = FirebaseAuth.getInstance();
        scan=(Button)findViewById(R.id.scan);


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

        onBackPressed();
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
        getMenuInflater().inflate(R.menu.action_bar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.viewaccount) {
            startActivity(new Intent(getApplicationContext(), view_account_register.class));
        } else if (item.getItemId() == R.id.editaccount) {
            startActivity(new Intent(getApplicationContext(), edit_account_register.class));
        } else if (item.getItemId() == R.id.aboutUs) {
            startActivity(new Intent(getApplicationContext(), aboutUs.class));
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
