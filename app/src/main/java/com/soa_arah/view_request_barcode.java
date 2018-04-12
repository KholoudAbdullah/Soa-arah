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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * Created by Lama on 05/03/18.
 */

public class view_request_barcode extends AppCompatActivity {

    private DatabaseReference mDatabaseReference1;
    private ListView request_barcode;
    private ArrayList<String> key1;
    private ArrayList<Food> re_barcode ;
    private TextView no_request_barcode;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBarBarcode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request_barcode);
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );


        firebaseAuth = FirebaseAuth.getInstance();
        request_barcode=(ListView)findViewById(R.id.request_barcode);
        re_barcode=new ArrayList<Food>();
        key1=new ArrayList<String>();
        no_request_barcode=(TextView)findViewById(R.id.no_request_barcode);

        progressBarBarcode = (ProgressBar) findViewById(R.id.progressbar2);

        progressBarBarcode.setVisibility(View.VISIBLE);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){

            startActivity(new Intent(getApplicationContext(),LoginPage.class));
        }









        request_barcode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {


                String barcodeN=re_barcode.get(i).getBarcodN();
                String image1 =re_barcode.get(i).getImage();
                String namef1 =re_barcode.get(i).getName();
                String imageTable =re_barcode.get(i).getImageTable();
                String keys1=key1.get(i);

                Intent intent1 = new Intent(view_request_barcode.this, view_info_request_Barcode.class);
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

                if(re_barcode.size()==0) {
                    no_request_barcode.setText("لا توجد طلبات");
                    progressBarBarcode.setVisibility(View.INVISIBLE);
                }
                else {
                    no_request_barcode.setText("");
                    progressBarBarcode.setVisibility(View.INVISIBLE);
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


        onBackPressed();
    }
    @Override
    public void onBackPressed()
    {

        // super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
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
