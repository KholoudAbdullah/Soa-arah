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


public class view_info_request extends AppCompatActivity implements View.OnClickListener {

    private TextView namefood ;
    private TextView calories;
    private TextView gr;
    private TextView stan;
    private ImageView imageView3;
    private ImageButton reject;
    private ImageButton accept;
    private EditText keyword;
    private String key;
    private String namef,calorie,image,gram,standard,keyword1;

    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReference1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_info_request);

        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );

        namef = getIntent().getStringExtra("namef");
         calorie=getIntent().getStringExtra("calorie");
         image=getIntent().getStringExtra("image");
         gram=getIntent().getStringExtra("gram");
         standard=getIntent().getStringExtra("standard");
        key=getIntent().getStringExtra("keys");

        namefood=(TextView)findViewById(R.id.namefood);
        calories=(TextView)findViewById(R.id.calories);
        gr=(TextView)findViewById(R.id.gr);
        stan=(TextView)findViewById(R.id.stan);
        imageView3=(ImageView)findViewById(R.id.imageView3);
        reject=(ImageButton)findViewById(R.id.reject);
        accept=(ImageButton)findViewById(R.id.accept);
        keyword=(EditText)findViewById(R.id.stt);

        keyword1=keyword.getText().toString();

        gr.setText(" الوزن  :" + gram);
        namefood.setText(namef);
        calories.setText( " السعرات الحراريه  :" +calorie);
        stan.setText(" المقاييس  :" +standard);
        Glide.with(getApplicationContext()).load(image).into(imageView3);


        reject.setOnClickListener(this);
        accept.setOnClickListener(this);




    }

    @Override
    public void onClick(View view) {

        if(view == accept){

             Food newFood = new Food(namef,image,keyword.getText().toString(),calorie,standard,gram);
            newFood.setBarcodN("لم يتم إدخال بيانات");
            newFood.setImageTable("لم يتم إدخال بيانات");

            mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("Food");
            mDatabaseReference.child(namef).setValue(newFood);

            mDatabaseReference1= FirebaseDatabase.getInstance().getReference().child("Requests");
            mDatabaseReference1.child("ByName").child(key).removeValue();
            Toast.makeText(view_info_request.this, "تمت إضافة الصنف", Toast.LENGTH_LONG).show();
            startActivity(new Intent(view_info_request.this, view_request.class));

        }
        if (view== reject) {
           // mDatabaseReference1.child("Requests").child("ByName").child(key).removeValue();
            mDatabaseReference1= FirebaseDatabase.getInstance().getReference().child("Requests");
            mDatabaseReference1.child("ByName").child(key).removeValue();Toast.makeText(view_info_request.this, key, Toast.LENGTH_LONG).show();

            Toast.makeText(view_info_request.this, "تم رفض الطلب", Toast.LENGTH_LONG).show();
            startActivity(new Intent(view_info_request.this, view_request.class));

        }

    }


}
