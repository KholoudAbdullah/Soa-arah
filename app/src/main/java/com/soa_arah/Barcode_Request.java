package com.soa_arah;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Barcode_Request extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    private EditText name;
    private Uri fImageUri,tableUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode__request);

        Intent intent1=getIntent();
        Bundle extras = intent1.getExtras();
        if(extras != null) {

            if (extras.getString( "Name1" )!=null){
                name.setText(extras.getString( "Name1" ));
            }
            if (extras.getString( "fImage1" )!=null){
                fImageUri = Uri.parse(extras.getString( "fImage1" ));
            }
            if (extras.getString( "tImage1" )!=null){
                tableUri = Uri.parse(extras.getString( "tImage1" ));
            }

        }

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
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
        if (name!=null)
            intent.putExtra("Name", name.getText().toString().trim());
        if (fImageUri!= null)
            intent.putExtra("fImage", fImageUri.toString().trim());
        if (tableUri!= null)
            intent.putExtra("tImage", tableUri.toString().trim());

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