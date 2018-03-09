package com.soa_arah;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lama on 05/03/18.
 */

public class view_request extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReference1;
    private ListView request_name;
    private ListView request_barcode;
    private ArrayList<Food> re_name;
    private ArrayList<String> key,key1;
    private ArrayList<Food> re_barcode ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request);


        request_name=(ListView)findViewById(R.id.request_name);
        request_barcode=(ListView)findViewById(R.id.request_barcode);
        re_name=new ArrayList<Food>();
        re_barcode=new ArrayList<Food>();
        key=new ArrayList<String>();
        key1=new ArrayList<String>();

        request_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                String calorie =re_name.get(i).getCalories();
                String gram =re_name.get(i).getGarms();
                String image =re_name.get(i).getImage();
                String namef =re_name.get(i).getName();
                String standard =re_name.get(i).getStandard();
               String keys=key.get(i);

                Intent intent = new Intent(view_request.this, view_info_request.class);
                intent.putExtra("calorie",calorie);
                intent.putExtra("gram",gram);
                intent.putExtra("image",image);
                intent.putExtra("namef",namef);
                intent.putExtra("standard",standard);
                intent.putExtra("keys",keys);

                startActivity(intent);

            }
        });


        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Requests").child("ByName");

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Food req = postSnapshot.getValue(Food.class);
                    re_name.add(req);
                    key.add(postSnapshot.getKey().toString());
                }

                    String[] requestsByName = new String[re_name.size()];


                    for (int i = 0; i < re_name.size(); i++) {

                        requestsByName[i] = re_name.get(i).getName();

                    }

                    //disp laying it to list
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, requestsByName);
                request_name.setAdapter(adapter);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        request_barcode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {


                String barcodeN=re_barcode.get(i).getBarcodN();
                String image1 =re_barcode.get(i).getImage();
                String namef1 =re_barcode.get(i).getName();
                String imageTable =re_barcode.get(i).getImageTable();
                String keys1=key1.get(i);

                Intent intent1 = new Intent(view_request.this, view_info_request_Barcode.class);
                intent1.putExtra("barcodeN",barcodeN);
                intent1.putExtra("image",image1);
                intent1.putExtra("namef",namef1);
                intent1.putExtra("imageTable",imageTable);
                intent1.putExtra("keys",keys1);

                startActivity(intent1);

            }
        });

        mDatabaseReference1 = FirebaseDatabase.getInstance().getReference().child("Requests").child("ByBarcode");

        mDatabaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Food req = postSnapshot.getValue(Food.class);
                    re_barcode.add(req);
                    key1.add(postSnapshot.getKey().toString());
                }

                String[] requestsByBarcode = new String[re_barcode.size()];


                for (int i = 0; i < re_barcode.size(); i++) {

                    requestsByBarcode[i] = re_barcode.get(i).getName();

                }

                //disp laying it to list
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, requestsByBarcode);
                request_barcode.setAdapter(adapter);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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


    }

}
