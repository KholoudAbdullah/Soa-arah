package com.soa_arah;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class searchByKeyword extends AppCompatActivity implements AdapterView.OnItemClickListener{
ArrayAdapter<String>adapter;
ArrayList<String> list;
    TextView textView;
String[] array;
   ListView listView;


    private Button button;
    private Button reg;
    private EditText searchtext;
    private DatabaseReference fData;
    private Button searchBtn;
    private ProgressDialog progressDialog;
    String id;
    String f;
    String cal;
    String img;
    boolean flag = false;
    String stand;
    String quantity;
    private Button scan;
    private ZXingScannerView scannerView;
    AlertDialog.Builder alert;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_keyword);
        list=getIntent().getStringArrayListExtra("list");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("الرجاء الانتظار ...");

        listView=(ListView)findViewById(R.id.listview);

        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list );
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        textView=(TextView)view;
        progressDialog.show();
        fData = FirebaseDatabase.getInstance().getReference().child("Food");
        fData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    f = snapshot.child("name").getValue(String.class);
                    id = snapshot.getKey();
                    if (f.equals(textView.getText().toString())) {
                        cal = snapshot.child("calories").getValue(String.class);
                        img = snapshot.child("image").getValue(String.class);
                        stand = snapshot.child("standard").getValue(String.class);
                        quantity = snapshot.child("quantity").getValue(String.class);
                        Intent intent = new Intent(getApplicationContext(), searchByName.class);
                        intent.putExtra("name", f);
                        intent.putExtra("id", id);
                        intent.putExtra("cal", cal);
                        intent.putExtra("img", img);
                        intent.putExtra("stand", stand);
                        intent.putExtra("quantity", quantity);
                        progressDialog.dismiss();
                        startActivity(intent);
                        break;
                    }

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }



}