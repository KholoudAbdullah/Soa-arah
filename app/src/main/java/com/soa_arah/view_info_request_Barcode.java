package com.soa_arah;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
    private Button accept,sta;
    private FirebaseAuth firebaseAuth;
    private EditText calories1;
    String[] array;
    private String key,stand="";
    private String barcodeN,image1,namef1,imageTable,cal,qun;
    private Button bkeyword;


    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReference1;
    android.app.AlertDialog.Builder alert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_info_bar_request);
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );

        isConnected();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        namef1 = getIntent().getStringExtra("namef");
        image1 = getIntent().getStringExtra("image");
        barcodeN = getIntent().getStringExtra("barcodeN");
        imageTable = getIntent().getStringExtra("imageTable");
        key = getIntent().getStringExtra("keys");
        cal = getIntent().getStringExtra("cal");
        qun=getIntent().getStringExtra("qun");

        firebaseAuth = FirebaseAuth.getInstance();

        namefood = (TextView) findViewById(R.id.name);
        reject = (Button) findViewById(R.id.reject);
        accept = (Button) findViewById(R.id.accept);

        calories1 = (EditText) findViewById(R.id.calories1);
        image = (ImageView) findViewById(R.id.image);
        imageTable1 = (ImageView) findViewById(R.id.imageTable);
        sta=(Button)findViewById(R.id.sta);


        namefood.setText(namef1);

        if (cal.equals("لايوجد"))
        {calories1.setText(""); }
        else {
            calories1.setText(cal);      }



        Glide.with(getApplicationContext()).load(image1).into(image);
        Glide.with(getApplicationContext()).load(imageTable).into(imageTable1);
        PhotoViewAttacher photoViewAttacher=new PhotoViewAttacher(imageTable1);
        photoViewAttacher.update();

        reject.setOnClickListener(this);
        accept.setOnClickListener(this);
        sta.setOnClickListener(this);




        calories1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (calories1.getText().toString().trim().length()<2){

                    calories1.setError("الرجاء إدخال عدد السعرات الحرارية");
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

                if (calories1.getText().toString().trim().length()<1 ){
                    alert= new android.app.AlertDialog.Builder(view_info_request_Barcode.this);
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

                }else if (stand==""){
                    alert= new android.app.AlertDialog.Builder(view_info_request_Barcode.this);
                    alert.setMessage("الرجاء اختيار المقياس المناسب");
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

                else{

                Food newFood = new Food(namef1, image1, calories1.getText().toString(), stand, qun);
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
            if( view == sta){
                StandardList();
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

    public void StandardList(){
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.standard_s, null);
        final CheckBox fs = alertLayout.findViewById(R.id.foodSpoon);
        final CheckBox ft = alertLayout.findViewById(R.id.teaSpoon);
        final CheckBox gra = alertLayout.findViewById(R.id.grams);
        final CheckBox pi = alertLayout.findViewById(R.id.piece);
        final CheckBox lm = alertLayout.findViewById(R.id.ml);
        final CheckBox cu = alertLayout.findViewById(R.id.cup);


        if(!stand.equals("")){
            array=stand.split(",");
            for (int i=0;i<array.length;i++){
                if (array[i].equals("جرام"))
                    gra.setChecked(true);
                if (array[i].equals("مليليتر"))
                    lm.setChecked(true);
                if (array[i].equals("ملعقة اكل"))
                    fs.setChecked(true);
                if (array[i].equals("ملعقة شاي"))
                    ft.setChecked(true);
                if (array[i].equals("عدد الحبات"))
                    pi.setChecked(true);
                if (array[i].equals("كوب"))
                    cu.setChecked(true);
            }
        }


        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("المقايس المقترحة");
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

                stand="";
                if (gra.isChecked()){stand=stand+"جرام,";}
                if (lm.isChecked()){stand=stand+"مليليتر,";}
                if (fs.isChecked()){stand=stand+"ملعقة اكل,";}
                if (ft.isChecked()){stand=stand+"ملعقة شاي,";}
                if (pi.isChecked()){stand=stand+"عدد الحبات,";}
                if (cu.isChecked()){stand=stand+"كوب,";}


            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();

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