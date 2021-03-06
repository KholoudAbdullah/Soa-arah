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

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class searchByKeyword extends AppCompatActivity implements AdapterView.OnItemClickListener{
ArrayAdapter<String>adapter;
ArrayList<String> list;
    TextView textView;
String[] array;
   ListView listView;


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
    String stand;
    String quantity;
    private Button scan;
    private ZXingScannerView scannerView;
    AlertDialog.Builder alert;
    String addCal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_keyword);
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );

        isConnected();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        list=getIntent().getStringArrayListExtra("list");
        addCal=getIntent().getStringExtra("addCal");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("الرجاء الانتظار ...");

        listView=(ListView)findViewById(R.id.listview);

        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list );
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);


        onBackPressed();
    }
    @Override
    public void onBackPressed()
    {

        // super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        textView=(TextView)view;
        progressDialog.show();
        fData = FirebaseDatabase.getInstance().getReference().child("Food");
        fData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    f = snapshot.child("name").getValue(String.class);
                    id = snapshot.getKey();
                    if (f.equals(textView.getText().toString())) {
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
