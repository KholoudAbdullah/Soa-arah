package com.soa_arah;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import javax.xml.transform.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Lama on 20/02/18.
 */

public class Barcode extends AppCompatActivity {


    private ZXingScannerView scannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_activity);
    }

    public void scanCode (View view){

        scannerView =new ZXingScannerView(this);
        scannerView.setResultHandler( new ZXingScannerResultHandler());

        setContentView(scannerView);
        scannerView.startCamera();


    }
    @Override
    public void onPause(){
        super.onPause();
        scannerView.startCamera();
    }

    class ZXingScannerResultHandler implements ZXingScannerView.ResultHandler
    {


        @Override
        public void handleResult(com.google.zxing.Result result) {

            String resultCode =result.getText();
            Toast.makeText(Barcode.this, resultCode, Toast.LENGTH_SHORT).show();
            setContentView(R.layout.barcode_activity);
            scannerView.stopCamera();
        }
    }
}
