package com.soa_arah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
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
    private EditText calories;
    private EditText gr;
    private ImageView imageView3;
    private Button reject;
    private Button accept;
    private Button bkeyword;
    private EditText keyword;
    private FirebaseAuth firebaseAuth;
    private String key,cKeyword="";
    private String namef,calorie,image,quantity,standard;
    StringBuffer result = new StringBuffer();

    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReference1;
    android.app.AlertDialog.Builder alert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_info_request);
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );

        isConnected();

        firebaseAuth = FirebaseAuth.getInstance();
        namef = getIntent().getStringExtra("namef");
        calorie=getIntent().getStringExtra("calorie");
        image=getIntent().getStringExtra("image");
        quantity=getIntent().getStringExtra("quantity");
        standard=getIntent().getStringExtra("standard");
        key=getIntent().getStringExtra("keys");

        namefood=(TextView)findViewById(R.id.namefood);
        calories=(EditText)findViewById(R.id.calories);
        gr=(EditText)findViewById(R.id.gr);
        imageView3=(ImageView)findViewById(R.id.imageView3);
        reject=(Button)findViewById(R.id.reject);
        accept=(Button)findViewById(R.id.accept);
        keyword=(EditText)findViewById(R.id.stt);
        bkeyword=(Button)findViewById(R.id.keywordN);


        cKeyword = cKeyword+keyword.getText().toString();

        gr.setText(quantity);
        namefood.setText(namef);
        calories.setText(calorie);

        Glide.with(getApplicationContext()).load(image).into(imageView3);


        reject.setOnClickListener(this);
        accept.setOnClickListener(this);
        bkeyword.setOnClickListener(this);



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

                if (cKeyword.length()<2){
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
                } else if (calories.getText().toString().trim().length()<1 ){
                    alert= new android.app.AlertDialog.Builder(view_info_request.this);
                    alert.setMessage("لم يتم إدخال السعرات الحرارية");
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
                } else if (gr.getText().toString().trim().length()<1 ){
                    alert= new android.app.AlertDialog.Builder(view_info_request.this);
                    alert.setMessage("لم يتم إدخال الكمية");
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

                if (cKeyword.substring(cKeyword.length()-1).equals("-")){
                   cKeyword= cKeyword.substring(0,cKeyword.length()-1);
                    Toast.makeText(view_info_request.this, cKeyword, Toast.LENGTH_SHORT).show();

                }

                Food newFood = new Food(namef, image, cKeyword, calories.getText().toString().trim(), standard, gr.getText().toString().trim());
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

        if (view == bkeyword){
                keyWord();
        }


    }

    public void keyWord(){

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.n_keyword, null);
        final CheckBox food = alertLayout.findViewById(R.id.NFood);
        final CheckBox choco = alertLayout.findViewById(R.id.Chocolate);
        final CheckBox des = alertLayout.findViewById(R.id.dessert);
        final CheckBox bak = alertLayout.findViewById(R.id.Bakery);
        final CheckBox jui = alertLayout.findViewById(R.id.juice);
        final CheckBox cof = alertLayout.findViewById(R.id.coffee);
        final CheckBox hd = alertLayout.findViewById(R.id.HDrink);
        final CheckBox cd = alertLayout.findViewById(R.id.CDrink);



        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("الكلمات المفتاحية المقترحة");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        alert.setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alert.setPositiveButton("حفظ", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (cKeyword!=null)
                    cKeyword=cKeyword+"-";

                if (food.isChecked()){cKeyword=cKeyword+"اكلات شعبية-";}
                if (choco.isChecked()){cKeyword=cKeyword+"شوكلت-";}
                if (des.isChecked()){cKeyword=cKeyword+"حلا-";}
                if (bak.isChecked()){cKeyword=cKeyword+"فطائر-";}
                if (jui.isChecked()){cKeyword=cKeyword+"عصير-";}
                if (cof.isChecked()){cKeyword=cKeyword+"قهوة-";}
                if (hd.isChecked()){cKeyword=cKeyword+"مشروبات ساخنة-";}
                if (cd.isChecked()){cKeyword=cKeyword+"مشروبات باردة-";}
                keyword.setText(cKeyword);

            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();


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

    public boolean isConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiInfo != null && wifiInfo.isConnected()) || (mobileInfo != null && mobileInfo.isConnected())) {
            return true;
        } else {
            showDialog();
            return false;
        }
    }




    private void showDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(" عذراً انت غير متصل بالانترنت هل تريد الاتصال بالانترنت او الاغلاق؟")
                .setCancelable(false)
                .setPositiveButton("الاتصال", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("إغلاق", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}