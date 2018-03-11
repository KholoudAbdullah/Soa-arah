package com.soa_arah;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class view_info_request_Barcode extends AppCompatActivity implements View.OnClickListener {

    private TextView namefood ;
    private ImageView image;
    private ImageView imageTable1;
    private ImageButton reject;
    private ImageButton accept;
    private EditText keyword;
    private EditText calories1;

    private String key;
    private String barcodeN,image1,namef1,imageTable;


    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReference1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_info_bar_request);

        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );

        namef1 = getIntent().getStringExtra("namef");
        image1=getIntent().getStringExtra("image");
        barcodeN=getIntent().getStringExtra("barcodeN");
        imageTable=getIntent().getStringExtra("imageTable");
        key=getIntent().getStringExtra("keys");

        namefood=(TextView)findViewById(R.id.name);
        reject=(ImageButton)findViewById(R.id.reject);
        accept=(ImageButton)findViewById(R.id.accept);
        keyword=(EditText)findViewById(R.id.keyword);
        calories1=(EditText)findViewById(R.id.calories1);
        image=(ImageView)findViewById(R.id.image);
        imageTable1=(ImageView)findViewById(R.id.imageTable);


        namefood.setText(namef1);


        Glide.with(getApplicationContext()).load(image1).into(image);
        Glide.with(getApplicationContext()).load(imageTable).into(imageTable1);

        reject.setOnClickListener(this);
        accept.setOnClickListener(this);




    }

    @Override
    public void onClick(View view) {

        if(view == accept){

            Food newFood = new Food(namef1,image1,keyword.getText().toString(),calories1.getText().toString(),"لا يوجد","لا يوجد");
            newFood.setBarcodN(barcodeN);
            newFood.setImageTable(imageTable);

            mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("Food");
            mDatabaseReference.child(namef1).setValue(newFood);

            mDatabaseReference1= FirebaseDatabase.getInstance().getReference().child("Requests");
            mDatabaseReference1.child("ByBarcode").child(key).removeValue();
            Toast.makeText(view_info_request_Barcode.this, "تمت إضافة الصنف", Toast.LENGTH_LONG).show();
            startActivity(new Intent(view_info_request_Barcode.this, view_request.class));

        }
        if (view== reject) {
            // mDatabaseReference1.child("Requests").child("ByName").child(key).removeValue();
            mDatabaseReference1= FirebaseDatabase.getInstance().getReference().child("Requests");
            mDatabaseReference1.child("ByBarcode").child(key).removeValue();Toast.makeText(view_info_request_Barcode.this, key, Toast.LENGTH_LONG).show();

            Toast.makeText(view_info_request_Barcode.this, "تم رفض الطلب", Toast.LENGTH_LONG).show();
            startActivity(new Intent(view_info_request_Barcode.this, view_request.class));

        }

    }


}
