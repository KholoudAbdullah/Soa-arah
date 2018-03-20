package com.soa_arah;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Lama on 20/02/18.
 */

public class Barcode extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private DatabaseReference fData;
    String f;
    String id;
    String bar;
    String img;
    String table;
    String cal;
    String q;
    String name;
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
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("يتم البحث، الرجاء الانتظار ...");
        firebaseAuth=FirebaseAuth.getInstance();

        onBackPressed();
    }
    @Override
    public void onBackPressed()
    {

        // super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
    }

    @Override
    public void handleResult(Result rawResult) {
        progressDialog.show();
        bar=rawResult.getText();
        final Intent intent = new Intent(this,barcodeInfo.class);
        intent.putExtra("bar",bar);
        fData = FirebaseDatabase.getInstance().getReference().child("Food");
        fData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    f=snapshot.child("barcodN").getValue(String.class);
                    id=snapshot.getKey();
                    if(f.equals(bar)){
                        name=snapshot.child("name").getValue(String.class);
                        cal=snapshot.child("calories").getValue(String.class);
                        q=snapshot.child("quantity").getValue(String.class);
                        img=snapshot.child("image").getValue(String.class);
                        table=snapshot.child("imageTable").getValue(String.class);
                        intent.putExtra("Name",name);
                        intent.putExtra("cal",cal);
                        intent.putExtra("q",q);
                        intent.putExtra("image",img);
                        intent.putExtra("table",table);
                        startActivity(intent);
                        flag=true;
                        mScannerView.stopCamera();
                        break;


                    }
                    else
                        flag=false;


                    }
                if(!flag){
                        if(firebaseAuth.getCurrentUser()==null){
                    alert=new AlertDialog.Builder(Barcode.this);
                    alert.setMessage("عذراً لايوجد هذا المنتج سجل دخولك لإضافته");
                    alert.setCancelable(true);
                    alert.setPositiveButton(
                            "سجل الدخول",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    startActivity(new Intent(getApplicationContext(), LoginPage.class));
                                    mScannerView.stopCamera();

                                }
                            });

                    alert.setNegativeButton(
                            "الغاء",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    startActivity(new Intent(getApplicationContext(), home_page_guest.class));
                                    mScannerView.stopCamera();

                                }
                            }

                    );}
                    else {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String id= user.getUid();
                            if(id.equals("7yO6vzOcv6VtXMjG3pjipXLpZin1")){
                                alert=new AlertDialog.Builder(Barcode.this);
                                alert.setMessage("عذراً لايوجد هاذا المنتج ");
                                alert.setCancelable(true);
                                alert.setPositiveButton(
                                        "الغاء",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                                startActivity(new Intent(getApplicationContext(),home_page_Nutrition_admin.class));
                                                mScannerView.stopCamera();

                                            }
                                        }

                                );
                            }
                            else{
                                if(id.equals("aSK7RyMA8xfdaQNPF0xS6kAumam2")){
                                    alert=new AlertDialog.Builder(Barcode.this);
                                    alert.setMessage("عذراً لايوجد هاذا المنتج ");
                                    alert.setCancelable(true);
                                    alert.setPositiveButton(
                                            "الغاء",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();
                                                    startActivity(new Intent(getApplicationContext(),home_page_IT_admin.class));
                                                    mScannerView.stopCamera();

                                                }
                                            }

                                    );
                                }
                                else
                                {if(firebaseAuth.getCurrentUser()!=null){
                                    alert=new AlertDialog.Builder(Barcode.this);
                                    alert.setMessage("عذراً لايوجد هاذا المنتج هل تريد اضافتة");
                                    alert.setCancelable(true);
                                    alert.setPositiveButton(
                                            "اضافة",
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
                                                    startActivity(new Intent(getApplicationContext(), home_page_register.class));
                                                    mScannerView.stopCamera();

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
}
