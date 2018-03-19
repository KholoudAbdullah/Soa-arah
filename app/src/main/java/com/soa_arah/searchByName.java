package com.soa_arah;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.text.DecimalFormat;

//import android.support.v7.app.AlertController;
//import com.bumptech.glide.Glide;
//import com.firebase.ui.database.FirebaseRecyclerAdapter;

public class searchByName extends AppCompatActivity {

   private DatabaseReference fData;
   private RecyclerView mfoodR;
   Button calculate;
   String[] array;
   Spinner span;
   EditText grm;
   double calor;
    String foodCal;
    String quantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_name);
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );
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
        if (foodCal.equals("لا يوجد")) {
            cal.setText("خالي من السعرات الحرارية");
        }else

        cal.setText(foodCal+" سعرة حرارية");
        calculate=(Button)findViewById(R.id.button2);

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (foodCal.equals("لا يوجد")){
                    Toast.makeText(searchByName.this, "عذراً المنتج خالي من السعرارت الحرارية", Toast.LENGTH_LONG).show();
                    return;
                }
                if (grm.getText().toString().equals("")) {
                    Toast.makeText(searchByName.this, "الرجاء ادخال الكمية", Toast.LENGTH_LONG).show();
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
                }
                DecimalFormat precision = new DecimalFormat("0.00");
// dblVariable is a number variable and not a String in this case
                cal.setText(precision.format(calor)+" سعرة حرارية");
            }
        });

    }





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
                //ID موب صح
                //IT admin
                if (user.getUid().equals( "aSK7RyMA8xfdaQNPF0xS6kAumam2" )) {
                    startActivity( new Intent( getApplicationContext(), home_page_IT_admin.class ) );
                }
                // Nutrition addmin
                else if (user.getUid().equals( "7yO6vzOcv6VtXMjG3pjipXLpZin1" )) {
                    startActivity( new Intent( getApplicationContext(), home_page_Nutrition_admin.class ) );
                } else {
                    startActivity( new Intent( getApplicationContext(), home_page_register.class ) );

                }

            } else {
                startActivity( new Intent( getApplicationContext(), home_page_guest.class ) );

            }
        } else {
            return super.onOptionsItemSelected( item );
        }
        return true;

    }




}
