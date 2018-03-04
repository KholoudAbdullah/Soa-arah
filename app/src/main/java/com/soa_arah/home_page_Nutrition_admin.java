package com.soa_arah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_nutrition_admin_activity);
        scan=(Button)findViewById(R.id.scan);

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
                            f=snapshot.child("Name").getValue(String.class);
                            if(f.equals(searchtext.getText().toString())){
                                cal=snapshot.child("Calories").getValue(String.class);
                                img=snapshot.child("Image").getValue(String.class);
                                stand=snapshot.child("Standardm").getValue(String.class);
                                grams=snapshot.child("Grams").getValue(String.class);
                                id=snapshot.getKey();
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





    }
    public void scanCode (View view) {
        startActivity(new Intent(getApplicationContext(), Barcode.class));
    }
}