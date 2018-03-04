package com.soa_arah;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

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
    private ZXingScannerView mScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);

    }


    @Override
    public void handleResult(Result rawResult) {
        bar=rawResult.getText();

        final Intent intent = new Intent(this,barcodeInfo.class);
        intent.putExtra("bar",bar);
        fData = FirebaseDatabase.getInstance().getReference().child("BarcodeFood");
        fData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    f=snapshot.child("barcode").getValue(String.class);
                    id=snapshot.getKey();
                    if(f.equals(bar)){
                        name=snapshot.child("Name").getValue(String.class);
                        cal=snapshot.child("cal").getValue(String.class);
                        q=snapshot.child("quantity").getValue(String.class);
                        img=snapshot.child("image").getValue(String.class);
                        table=snapshot.child("table").getValue(String.class);
                        intent.putExtra("Name",name);
                        intent.putExtra("cal",cal);
                        intent.putExtra("q",q);
                        intent.putExtra("image",img);
                        intent.putExtra("table",table);
                        startActivity(intent);
                        mScannerView.stopCamera();
                    }
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
