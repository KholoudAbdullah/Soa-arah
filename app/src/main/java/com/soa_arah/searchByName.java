package com.soa_arah;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.text.DecimalFormat;

//import android.support.v7.app.AlertController;
//import com.bumptech.glide.Glide;
//import com.firebase.ui.database.FirebaseRecyclerAdapter;

public class searchByName extends AppCompatActivity {

   private Button viewReview;
   private DatabaseReference fData;
   private RecyclerView mfoodR;
   Button calculate;
   String[] array;
   Spinner span;
   EditText grm;
   double calor;
    String foodCal;
    String quantity;
    android.app.AlertDialog.Builder alert;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_name);
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );

        isConnected();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        progressDialog = new ProgressDialog(searchByName.this);
        // Setting progressDialog Title.
        progressDialog.setMessage("الرجاء الانتظار ...");
        progressDialog.show();

        String foodN = getIntent().getStringExtra("name");
        foodCal=getIntent().getStringExtra("cal");
        String image=getIntent().getStringExtra("img");
        String stand=getIntent().getStringExtra("stand");
        quantity=getIntent().getStringExtra("quantity");
        array=stand.split(",");
        span=(Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, array);
        span.setAdapter(adapter);
        TextView textn=(TextView)findViewById(R.id.textView);
        final TextView cal=(TextView)findViewById(R.id.textView6);
        ImageView img=(ImageView)findViewById(R.id.imageView3);
        grm=(EditText)findViewById(R.id.editText);
        grm.setText(quantity);
        Glide.with(getApplicationContext()).load(image).into(img);
        textn.setText(foodN);
        // Hiding the progressDialog after done uploading.
        progressDialog.dismiss();

        if (foodCal.equals("لا يوجد")) {
            cal.setText("خالي من السعرات الحرارية");
        }else

        cal.setText(foodCal+" سعرة حرارية");
        calculate=(Button)findViewById(R.id.button2);

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (foodCal.equals("لا يوجد")){
                    alert= new android.app.AlertDialog.Builder(searchByName.this);
                    alert.setMessage("عذراً المنتج خالي من السعرارت الحرارية");
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
                if (grm.getText().toString().equals("")) {

                    alert= new android.app.AlertDialog.Builder(searchByName.this);
                    alert.setMessage("الرجاء ادخال الكمية");
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
                String selected = span.getSelectedItem().toString();
                double caldoub = Double.parseDouble(getIntent().getStringExtra("cal"));
                double gramdoub = Double.parseDouble(getIntent().getStringExtra("quantity"));
                double q = Double.parseDouble(grm.getText().toString());
                if (selected.equals("ملعقة شاي")){
                     calor=((caldoub*5)/gramdoub)*q;
                }else if(selected.equals("ملعقة اكل")){
                    calor=((caldoub*15)/gramdoub)*q;
                }else if(selected.equals("كوب")){
                    calor=((caldoub*250)/gramdoub)*q;
                }else  if(selected.equals("جرام")){
                    calor=(caldoub*q)/gramdoub;
                }else  if(selected.equals("عدد الحبات")){
                    calor=caldoub*q;
                }else if(selected.equals("مليليتر")){
                    calor=(caldoub*q)/gramdoub;
                }
                DecimalFormat precision = new DecimalFormat("0.00");
// dblVariable is a number variable and not a String in this case
                cal.setText(precision.format(calor)+" سعرة حرارية");
            }
        });

    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.home_1, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.Home) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


            if (user != null) {
                String id = user.getUid();
                //IT admin
                if(id.equals("kstgUKiRA7T3p1NNl3GuGBHgvcf2")){
                    startActivity(new Intent(getApplicationContext(),home_page_IT_admin.class));
                }
                // Nutrition addmin
                else if(id.equals("Pf7emnnQTEbmukAIDwWgkuv8JbC2")){
                    startActivity(new Intent(getApplicationContext(),home_page_Nutrition_admin.class));
                }
                else {
                    startActivity(new Intent(getApplicationContext(), home_page_register.class));

                }

            } else {
                startActivity( new Intent( getApplicationContext(), home_page_guest.class ) );

            }
        } else {
            return super.onOptionsItemSelected( item );
        }
        return true;

    }

    public void toView(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String id = user.getUid();
            Log.d("ff",id);
            if (!(id.equals("kstgUKiRA7T3p1NNl3GuGBHgvcf2")) && !(id.equals("Pf7emnnQTEbmukAIDwWgkuv8JbC2"))) {
                Intent intent = new Intent(searchByName.this, ViewReviewRegisterUser.class);
                intent.putExtra("name", getIntent().getStringExtra("name"));
                startActivity(intent);
            } else {
                Intent intent = new Intent(searchByName.this, ViewReview.class);
                intent.putExtra("name", getIntent().getStringExtra("name"));
                startActivity(intent);
            }
        }else{
            Intent intent = new Intent(searchByName.this, ViewReview.class);
            intent.putExtra("name", getIntent().getStringExtra("name"));
            startActivity(intent);
        }
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
