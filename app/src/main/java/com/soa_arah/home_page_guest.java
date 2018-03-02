package com.soa_arah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class home_page_guest extends AppCompatActivity  {

    private Button button ;
    private Button reg;
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
    private Button scan;
    private ZXingScannerView scannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_guest_activity);

        button = (Button)findViewById(R.id.login);
        reg=(Button) findViewById(R.id.register);
        searchtext=(EditText)findViewById(R.id.searchword);
        searchBtn=(Button)findViewById(R.id.searchButton);
        //progressDialog.setMessage("يتم البحث، الرجاء الانتظار ...");
       // progressDialog.show();
        searchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //progressDialog.dismiss();
                String foodN = getIntent().getStringExtra("FoodN");
                fData = FirebaseDatabase.getInstance().getReference().child("Food");
                fData.addValueEventListener(new ValueEventListener() {
                                                          @Override
                                                          public void onDataChange(DataSnapshot dataSnapshot) {
                                                              for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                   f=snapshot.child("Name").getValue(String.class);
                                                                   cal=snapshot.child("Calories").getValue(String.class);
                                                                   img=snapshot.child("Image").getValue(String.class);
                                                                   stand=snapshot.child("Standardm").getValue(String.class);
                                                                   grams=snapshot.child("Grams").getValue(String.class);
                                                                   id=snapshot.getKey();
                                                                  if(f.equals(searchtext.getText().toString())){
                                                                      Intent intent = new Intent(getApplicationContext(), searchByName.class);
                                                                      intent.putExtra("name", f);
                                                                      intent.putExtra("id",id );
                                                                       intent.putExtra("cal",cal );
                                                                      intent.putExtra("img",img );
                                                                      intent.putExtra("stand",stand);
                                                                      intent.putExtra("garms",grams);
                                                                      startActivity(intent);
                                                                      flag=true;
                                                                      break;
                                                                  }else flag=false;

                                                              }
                                                              }

                                                          @Override
                                                          public void onCancelled(DatabaseError databaseError) {

                                                          }
                                                      });




           }
       });

        reg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Registration.class));
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
            }
        });



    }

    public void scanCode (View view){

        scannerView =new ZXingScannerView(this);
        scannerView.setResultHandler( new home_page_guest.ZXingScannerResultHandler());

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
            Toast.makeText(home_page_guest.this, resultCode, Toast.LENGTH_SHORT).show();
            setContentView(R.layout.home_page_guest_activity);
            scannerView.stopCamera();
        }
    }
}

