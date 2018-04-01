package com.soa_arah;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
    private Button reject;
    private Button accept;
    private EditText keyword;
    private FirebaseAuth firebaseAuth;
    private EditText calories1;

    private String key;
    private String barcodeN,image1,namef1,imageTable;


    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReference1;
    android.app.AlertDialog.Builder alert;


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
        reject=(Button)findViewById(R.id.reject);
        accept=(Button)findViewById(R.id.accept);
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


        keyword.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (keyword.getText().toString().trim().length()<1){

                    keyword.setError("لم يتم إدخال الكلمات المفتاحية");
                }
            }
        });
        calories1.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (calories1.getText().toString().trim().length()<1){

                    calories1.setError("لم يتم إدخال الكلمات المفتاحية");
                }
            }
        });



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


        onBackPressed();
    }
    @Override
    public void onBackPressed()
    {

        // super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
    }

    @Override
    public void onClick(View view) {

            if (view == accept) {

                if (keyword.getText().toString().trim().length()<1){
                    alert= new android.app.AlertDialog.Builder(view_info_request_Barcode.this);
                    alert.setMessage("لم يتم إدخال الكلمات المفتاحية");
                    alert.setCancelable(true);
                    alert.setPositiveButton(
                            "موافق",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.cancel();

                                }
                            });
                    android.app.AlertDialog alert11 = alert.create();
                    alert11.show();
                    return;
                }
                else if (calories1.getText().toString().trim().length()<1){
                    alert= new android.app.AlertDialog.Builder(view_info_request_Barcode.this);
                    alert.setMessage("لم يتم إدخال الكلمات المفتاحية");
                    alert.setCancelable(true);
                    alert.setPositiveButton(
                            "موافق",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.cancel();

                                }
                            });
                    android.app.AlertDialog alert11 = alert.create();
                    alert11.show();
                    return;
                }

                Food newFood = new Food(namef1, image1, keyword.getText().toString(), calories1.getText().toString(), "لا يوجد", "لا يوجد");
                newFood.setBarcodN(barcodeN);
                newFood.setImageTable(imageTable);

                mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Food");
                mDatabaseReference.child(namef1).setValue(newFood);

                mDatabaseReference1 = FirebaseDatabase.getInstance().getReference().child("Requests");
                mDatabaseReference1.child("ByBarcode").child(key).removeValue();
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        view_info_request_Barcode.this );

                alert.setTitle( "تمت إضافة الصنف" ).setIcon( R.drawable.t1 );
                AlertDialog dialog = alert.create();
                // Finally, display the alert dialog
                dialog.show();

                // Get screen width and height in pixels
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics( displayMetrics );
                // The absolute width of the available display size in pixels.
                int displayWidth = displayMetrics.widthPixels;
                // The absolute height of the available display size in pixels.
                int displayHeight = displayMetrics.heightPixels;

                // Initialize a new window manager layout parameters
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

                // Copy the alert dialog window attributes to new layout parameter instance
                layoutParams.copyFrom( dialog.getWindow().getAttributes() );



                // Set alert dialog width equal to screen width 70%
                int dialogWindowWidth = (int) (displayWidth * 0.9f);
                // Set alert dialog height equal to screen height 70%
                int dialogWindowHeight = (int) (displayHeight * 0.15f);

                // Set the width and height for the layout parameters
                // This will bet the width and height of alert dialog
                layoutParams.width = dialogWindowWidth;
                layoutParams.height = dialogWindowHeight;

                // Apply the newly created layout parameters to the alert dialog window
                dialog.getWindow().setAttributes( layoutParams );
                startActivity(new Intent(view_info_request_Barcode.this, view_request.class));

            }
            if (view == reject) {
                // mDatabaseReference1.child("Requests").child("ByName").child(key).removeValue();
                alert= new android.app.AlertDialog.Builder(view_info_request_Barcode.this);
                alert.setMessage("هل انت متأكد من رفض الطلب؟");
                alert.setCancelable(true);
                alert.setPositiveButton(
                        "نعم",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                mDatabaseReference1 = FirebaseDatabase.getInstance().getReference().child("Requests");
                                mDatabaseReference1.child("ByBarcode").child(key).removeValue();
                                startActivity(new Intent(view_info_request_Barcode.this, view_request.class));

                            }
                        });

                alert.setNegativeButton(
                        "لا",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.cancel();
                            }
                        });

                android.app.AlertDialog alert11 = alert.create();
                alert11.show();

            }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.adminloguot,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.aboutUs) {
            startActivity(new Intent(getApplicationContext(), aboutUs.class));
        }else if (item.getItemId() == R.id.Logout){
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