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
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeToAddCalories extends AppCompatActivity implements ZXingScannerView.ResultHandler  {
    private DatabaseReference fData;
    String f;
    String id;
    String bar;
    String img;
    String table;
    String cal;
    String q;
    String name,addCal;
    TextView textn;
    TextView textC;
    ImageView imageView;
    ImageView imageTable;
    boolean flag=true;
    private ZXingScannerView mScannerView;
    ProgressDialog progressDialog;
    AlertDialog.Builder alert;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );

        isConnected();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("يتم البحث، الرجاء الانتظار ...");
        firebaseAuth=FirebaseAuth.getInstance();

    }



    public void handleResult(Result rawResult){

        progressDialog.show();
        bar=rawResult.getText();
        boolean digitsOnly = TextUtils.isDigitsOnly(bar);
        if (digitsOnly){
            final Intent intent = new Intent(this,barcodeInfoToAddCalories.class);
            intent.putExtra("bar",bar);
            fData = FirebaseDatabase.getInstance().getReference().child("Food");
            fData.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        f = snapshot.child("barcodN").getValue(String.class);
                        id = snapshot.getKey();
                        if (f.equals(bar)) {
                            name = snapshot.child("name").getValue(String.class);
                            cal = snapshot.child("calories").getValue(String.class);
                            q = snapshot.child("quantity").getValue(String.class);
                            img = snapshot.child("image").getValue(String.class);
                            table = snapshot.child("imageTable").getValue(String.class);
                            intent.putExtra("Name", name);
                            intent.putExtra("cal", cal);
                            intent.putExtra("q", q);
                            intent.putExtra("image", img);
                            intent.putExtra("table", table);
                            startActivity(intent);
                            flag = true;
                            mScannerView.stopCamera();
                            break;


                        } else
                            flag = false;


                    }

                    if (!flag) {
                            alert = new AlertDialog.Builder(BarcodeToAddCalories.this);
                            alert.setMessage("عذراً لايوجد هذا المنتج الإنتقال لإرسال الطلب");
                            alert.setCancelable(true);
                            alert.setPositiveButton(
                                    "موافق",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            startActivity(new Intent(getApplicationContext(), Request_page.class));
                                            mScannerView.stopCamera();

                                        }
                                    });

                            alert.setNegativeButton(
                                    "الغاء",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                            startActivity(new Intent(getApplicationContext(), diet_plan.class));
                                            mScannerView.stopCamera();

                                        }
                                    }

                            );
                        AlertDialog alert11 = alert.create();
                        alert11.show();

                    }

            }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });}
        else if (!digitsOnly){
            alert= new android.app.AlertDialog.Builder(BarcodeToAddCalories.this);
            alert.setMessage("الرجاء مسح باركود الشراء");
            alert.setCancelable(true);
            alert.setPositiveButton(
                    "موافق",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            startActivity(new Intent(getApplicationContext(), BarcodeToAddCalories.class));
                        }
                    });
            android.app.AlertDialog alert11 = alert.create();
            alert11.show();
        }
        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        // Register ourselves as a handler for scan results.
        mScannerView.setResultHandler(this);
        // Start camera on resume
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop camera on pause
        mScannerView.stopCamera();
    }
    public void newac(){



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


