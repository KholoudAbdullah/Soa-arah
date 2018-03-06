package com.soa_arah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
public class home_page_Nutrition_admin extends AppCompatActivity {
    private EditText searchtext;
    private DatabaseReference fData;
    private Button searchBtn;
    private ProgressDialog progressDialog;
    String id;
    String f;
    String cal;
    String img;
    boolean flag;
    String stand;
    String grams;
    private Button scan;
    private ZXingScannerView scannerView;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_nutrition_admin_activity);
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );
        scan=(Button)findViewById(R.id.scan);

        firebaseAuth = FirebaseAuth.getInstance();

        searchtext=(EditText)findViewById(R.id.searchword);
        searchBtn=(Button)findViewById(R.id.searchButton);
        //progressDialog.setMessage("يتم البحث، الرجاء الانتظار ...");
        // progressDialog.show();
        searchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //progressDialog.dismiss();
                String foodN = getIntent().getStringExtra("FoodN");
                fData = FirebaseDatabase.getInstance().getReference().child("Food");
                fData.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            f=snapshot.child("name").getValue(String.class);
                            cal=snapshot.child("calories").getValue(String.class);
                            img=snapshot.child("image").getValue(String.class);
                            stand=snapshot.child("standard").getValue(String.class);
                            grams=snapshot.child("garms").getValue(String.class);
                            id=snapshot.getKey();
                            if(f.equals(searchtext.getText().toString())){
                                Intent intent = new Intent(getApplicationContext(), searchByName.class);
                                intent.putExtra("name", f);
                                intent.putExtra("id",id );
                                intent.putExtra("cal",cal );
                                intent.putExtra("img",img );
                                intent.putExtra("stand",stand);
                                intent.putExtra("garms",grams);
                                startActivity(intent);
                                flag=true;
                                break;
                            }else flag=false;

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




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
    public void scanCode (View view) {
        startActivity(new Intent(getApplicationContext(), Barcode.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.adminloguot,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

         if (item.getItemId() == R.id.Logout){
            firebaseAuth.signOut();
            //closing activity
            finish();
            startActivity(new Intent(getApplicationContext(), LoginPage.class));

        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }
}