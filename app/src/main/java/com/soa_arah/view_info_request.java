package com.soa_arah;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class view_info_request extends AppCompatActivity implements View.OnClickListener {

    private TextView namefood ;
    private TextView calories;
    private TextView gr;
    private ImageView imageView3;
    private ImageButton reject;
    private ImageButton accept;
    private EditText keyword;
    private FirebaseAuth firebaseAuth;
    private String key;
    private String namef,calorie,image,quantity,standard,keyword1;

    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReference1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_info_request);

        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );
        firebaseAuth = FirebaseAuth.getInstance();
        namef = getIntent().getStringExtra("namef");
         calorie=getIntent().getStringExtra("calorie");
         image=getIntent().getStringExtra("image");
        quantity=getIntent().getStringExtra("quantity");
         standard=getIntent().getStringExtra("standard");
        key=getIntent().getStringExtra("keys");

        namefood=(TextView)findViewById(R.id.namefood);
        calories=(TextView)findViewById(R.id.calories);
        gr=(TextView)findViewById(R.id.gr);
        imageView3=(ImageView)findViewById(R.id.imageView3);
        reject=(ImageButton)findViewById(R.id.reject);
        accept=(ImageButton)findViewById(R.id.accept);
        keyword=(EditText)findViewById(R.id.stt);

        keyword1=keyword.getText().toString();

        gr.setText(" الكميه  :" + quantity);
        namefood.setText(namef);
        calories.setText( " السعرات الحراريه  :" +calorie);

        Glide.with(getApplicationContext()).load(image).into(imageView3);


        reject.setOnClickListener(this);
        accept.setOnClickListener(this);




    }

    @Override
    public void onClick(View view) {

        if(view == accept){

             Food newFood = new Food(namef,image,keyword.getText().toString(),calorie,standard,quantity);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.adminloguot,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.Logout){
            firebaseAuth.signOut();
            //closing activity
            finish();
            startActivity(new Intent(getApplicationContext(), home_page_guest.class));

        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
