package com.soa_arah;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class barcodeInfo extends AppCompatActivity {
String cal,img,table,q,name,bar,f,id;
    private DatabaseReference fData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_info);
        bar=getIntent().getStringExtra("bar");
        name=getIntent().getStringExtra("Name");
        cal=getIntent().getStringExtra("cal");
        img=getIntent().getStringExtra("image");
        table=getIntent().getStringExtra("table");
        q=getIntent().getStringExtra("q");
        TextView textn=(TextView)findViewById(R.id.textView9);
        textn.setText(bar);
        TextView textC=(TextView)findViewById(R.id.textView12);
        ImageView imageView=(ImageView)findViewById(R.id.imageView7);
        ImageView imageTable=(ImageView)findViewById(R.id.imageView10);
        Glide.with(getApplicationContext()).load(img).into(imageView);
        Glide.with(getApplicationContext()).load(table).into(imageTable);
        textn.setText(name);
        if(cal.equals("لا يوجد")){
            textC.setText("خالي من السعرات الحرارية");
        }else
            textC.setText(cal.toString()+" لكل "+q.toString());
        /*fData = FirebaseDatabase.getInstance().getReference().child("BarcodeFood");
        fData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    f=snapshot.child("barcode").getValue(String.class);
                    id=snapshot.getKey();
                    if(f.equals(bar)){
                        name=snapshot.child("Name").getValue(String.class);
                        TextView textn=(TextView)findViewById(R.id.textView9);
                        textn.setText(name);
                        cal=snapshot.child("cal").getValue(String.class);
                        ml=snapshot.child("ml").getValue(String.class);
                        img=snapshot.child("image").getValue(String.class);
                        table=snapshot.child("table").getValue(String.class);


                        TextView textC=(TextView)findViewById(R.id.textView12);
                        ImageView imageView=(ImageView)findViewById(R.id.imageView7);
                        ImageView imageTable=(ImageView)findViewById(R.id.imageView10);
                        Glide.with(getApplicationContext()).load(img).into(imageView);
                        Glide.with(getApplicationContext()).load(table).into(imageTable);
                        textn.setText(name);
                        if(cal.equals("لا يوجد")){
                            textC.setText("خالي من السعرات الحرارية");
                        }else
                            textC.setText(cal.toString()+" لكل"+ml.toString()+" مل");
                        break;
                    }
                }


            }


        });*/


    }
}
