package com.soa_arah;

import android.app.ProgressDialog;
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


public class view_info_request extends AppCompatActivity implements View.OnClickListener {

    private TextView namefood ;
    private TextView calories;
    private TextView gr;
    private ImageView imageView3;
    private Button reject;
    private Button accept;
    private EditText keyword;
    private FirebaseAuth firebaseAuth;
    private String key;
    private String namef,calorie,image,quantity,standard,keyword1;

    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReference1;
    android.app.AlertDialog.Builder alert;


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
        reject=(Button)findViewById(R.id.reject);
        accept=(Button)findViewById(R.id.accept);
        keyword=(EditText)findViewById(R.id.stt);

        keyword1=keyword.getText().toString();

        gr.setText(" الكميه    :    " + quantity);
        namefood.setText(namef);
        calories.setText( " السعرات الحراريه    :    " +calorie);

        Glide.with(getApplicationContext()).load(image).into(imageView3);


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
                    alert= new android.app.AlertDialog.Builder(view_info_request.this);
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

                Food newFood = new Food(namef, image, keyword.getText().toString(), calorie, standard, quantity);
                newFood.setBarcodN("لم يتم إدخال بيانات");
                newFood.setImageTable("لم يتم إدخال بيانات");

                mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Food");
                mDatabaseReference.child(namef).setValue(newFood);

                mDatabaseReference1 = FirebaseDatabase.getInstance().getReference().child("Requests");
                mDatabaseReference1.child("ByName").child(key).removeValue();
                //display message to the user here
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        view_info_request.this );

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


                startActivity(new Intent(view_info_request.this, view_request.class));

            }
            if (view == reject) {
                // mDatabaseReference1.child("Requests").child("ByName").child(key).removeValue();


                alert= new android.app.AlertDialog.Builder(view_info_request.this);
                alert.setMessage("هل انت متأكد من رفض الطلب؟");
                alert.setCancelable(true);
                alert.setPositiveButton(
                        "نعم",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mDatabaseReference1 = FirebaseDatabase.getInstance().getReference().child("Requests");
                                mDatabaseReference1.child("ByName").child(key).removeValue();
                                startActivity(new Intent(view_info_request.this, view_request.class));

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