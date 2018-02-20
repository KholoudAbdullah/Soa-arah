package com.soa_arah;

import android.content.Context;
//import android.support.v7.app.AlertController;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.bumptech.glide.Glide;
import com.bumptech.glide.Glide;
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class searchByName extends AppCompatActivity {

   private DatabaseReference fData;
   private RecyclerView mfoodR;
   Button calculate;
   String[] array;
   Spinner span;
   EditText grm;
   double calor;
    String foodCal;
    String grams;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_name);
        String foodN = getIntent().getStringExtra("name");
        foodCal=getIntent().getStringExtra("cal");
        String image=getIntent().getStringExtra("img");
        String stand=getIntent().getStringExtra("stand");
         grams=getIntent().getStringExtra("garms");
        array=stand.split(",");
        span=(Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, array);
        span.setAdapter(adapter);
        TextView textn=(TextView)findViewById(R.id.textView);
        final TextView cal=(TextView)findViewById(R.id.textView6);
        ImageView img=(ImageView)findViewById(R.id.imageView3);
        grm=(EditText)findViewById(R.id.editText);
        grm.setText(grams);
        Glide.with(getApplicationContext()).load(image).into(img);
        textn.setText(foodN);
        cal.setText(foodCal+" سعرة حرارية");
        calculate=(Button)findViewById(R.id.button2);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (grm.getText().toString().equals("")) {
                    Toast.makeText(searchByName.this, "لا يوجد", Toast.LENGTH_LONG).show();
                    return;
                }
                String selected = span.getSelectedItem().toString();
                double caldoub = Double.parseDouble(foodCal);
                double gramdoub = Double.parseDouble(grams);
                double q = Double.parseDouble(grm.getText().toString());
                if (selected.equals("ملعقة شاي")){
                     calor=((caldoub*5)/gramdoub)*q;
                }else if(selected.equals("ملعقة اكل")){
                    calor=((caldoub*15)/gramdoub)*q;
                }else if(selected.equals("كوب")){
                    calor=((caldoub*250)/gramdoub)*q;
                }else  if(selected.equals("جرام")){
                    calor=((caldoub*gramdoub)/gramdoub)*q;
                }else  if(selected.equals("بالحبات")){
                    calor=((caldoub*gramdoub)/gramdoub)*q;
                }
                DecimalFormat precision = new DecimalFormat("0.00");
// dblVariable is a number variable and not a String in this case
                cal.setText(precision.format(calor)+" سعرة حرارية");
            }
        });

    }










}
