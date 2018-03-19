package com.soa_arah;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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

import uk.co.senab.photoview.PhotoViewAttacher;


public class view_info_request_Barcode extends AppCompatActivity implements View.OnClickListener {

    private TextView namefood ;
    private ImageView image;
    private ImageView imageTable1;
    private ImageButton reject;
    private ImageButton accept;
    private EditText keyword;
    private FirebaseAuth firebaseAuth;
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
        firebaseAuth = FirebaseAuth.getInstance();
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
        PhotoViewAttacher photoViewAttacher=new PhotoViewAttacher(imageTable1);
        photoViewAttacher.update();

        reject.setOnClickListener(this);
        accept.setOnClickListener(this);


        //menu bottom bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.Navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.search:
                                startActivity(new Intent(getApplicationContext(), home_page_Nutrition_admin.class));
                                break;
                            case R.id.request:
                                startActivity(new Intent(getApplicationContext(), view_request.class));

                                break;
                            case R.id.account:
                                startActivity(new Intent(getApplicationContext(), account_Nutrition_admin.class));

                                break;
                        }
                        return false;
                    }
                });


    }

    @Override
    public void onClick(View view) {
if(keyword.getText().toString().equals("")&&calories1.getText().toString().equals("")){
    Toast.makeText(view_info_request_Barcode.this, "لم يتم إدخال السعرات الحراريه و الكلمات المفتاحية", Toast.LENGTH_LONG).show();
}
else if(keyword.getText().toString().equals("")){
    Toast.makeText(view_info_request_Barcode.this, "لم يتم إدخال الكلمات المفتاحية", Toast.LENGTH_LONG).show();

}
else if(calories1.getText().toString().equals("")){
    Toast.makeText(view_info_request_Barcode.this, "لم يتم إدخال السعرات الحراريه", Toast.LENGTH_LONG).show();

}
else {
    if (view == accept) {

        Food newFood = new Food(namef1, image1, keyword.getText().toString(), calories1.getText().toString(), "لا يوجد", "لا يوجد");
        newFood.setBarcodN(barcodeN);
        newFood.setImageTable(imageTable);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Food");
        mDatabaseReference.child(namef1).setValue(newFood);

        mDatabaseReference1 = FirebaseDatabase.getInstance().getReference().child("Requests");
        mDatabaseReference1.child("ByBarcode").child(key).removeValue();
        Toast.makeText(view_info_request_Barcode.this, "تمت إضافة الصنف", Toast.LENGTH_LONG).show();
        startActivity(new Intent(view_info_request_Barcode.this, view_request.class));

    }
    if (view == reject) {
        // mDatabaseReference1.child("Requests").child("ByName").child(key).removeValue();
        mDatabaseReference1 = FirebaseDatabase.getInstance().getReference().child("Requests");
        mDatabaseReference1.child("ByBarcode").child(key).removeValue();
        Toast.makeText(view_info_request_Barcode.this, key, Toast.LENGTH_LONG).show();

        Toast.makeText(view_info_request_Barcode.this, "تم رفض الطلب", Toast.LENGTH_LONG).show();
        startActivity(new Intent(view_info_request_Barcode.this, view_request.class));

    }
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
