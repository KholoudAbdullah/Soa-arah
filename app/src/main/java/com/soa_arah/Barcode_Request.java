package com.soa_arah;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Barcode_Request extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    private String name;
    private Uri fImageUri,tableUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
//        Intent intent1=getIntent();
//        if(intent1.getExtras().getString( "Name1")!="null")
//            name=intent1.getExtras().getString( "Name1");
//        if(intent1.getExtras().getString( "fImage1")!="null")
//            fImageUri = Uri.parse(intent1.getExtras().getString( "fImage1"));
//        if (intent1.getExtras().getString( "tImage1")!="null")
//            tableUri = Uri.parse(intent1.getExtras().getString( "tImage1"));
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v("TAG", rawResult.getText()); // Prints scan results
        // Prints the scan format (qrcode, pdf417 etc.)
        Log.v("TAG", rawResult.getBarcodeFormat().toString());
        Toast.makeText(Barcode_Request.this, rawResult.getText(), Toast.LENGTH_SHORT).show();


      Intent intent = new Intent(Barcode_Request.this, RequestByBarcode.class);
            intent.putExtra("BarcodeNum", rawResult.getText());
//
//            intent.putExtra("Name", name);
//
//            intent.putExtra("fImage", fImageUri.toString().trim());
//
//            intent.putExtra("tImage", tableUri.toString().trim());
//
        startActivity(intent);
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
}