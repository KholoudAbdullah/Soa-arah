package com.soa_arah;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class home_page_guest extends AppCompatActivity {

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
    String []keyword;
    String []searchR;
    int size;
    ArrayList<String>list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_guest_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        scan = (Button) findViewById(R.id.scan);
        button = (Button) findViewById(R.id.login);
        reg = (Button) findViewById(R.id.register);
        searchtext = (EditText) findViewById(R.id.searchword);
        searchBtn = (Button) findViewById(R.id.searchButton);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("يتم البحث، الرجاء الانتظار ...");
        searchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                 searchKeyword();


            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                startActivity(new Intent(getApplicationContext(), Registration.class));
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
            }
        });


    }

    public void scanCode(View view) {
        startActivity(new Intent(getApplicationContext(), Barcode.class));
    }


    public void searchKeyword() {
        list.clear();
        progressDialog.show();
        fData = FirebaseDatabase.getInstance().getReference().child("Food");
        fData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    key = snapshot.child("keyword").getValue(String.class);
                    f = snapshot.child("name").getValue(String.class);
                    id = snapshot.getKey();
                    keyword = key.split(",");
                    int j = 0;
                    for (int i = 0; i < keyword.length; i++) {
                        if (keyword[i].equals(searchtext.getText().toString().trim())) {
                            list.add(f);

                        }

                    }
                }
                progressDialog.dismiss();
                if (list.isEmpty()) {
                    alert = new AlertDialog.Builder(home_page_guest.this);
                    alert.setMessage("عذراً لايوجد هذا العنصر سجل دخولك لإضافته");
                    alert.setCancelable(true);
                    alert.setPositiveButton(
                            "سجل الدخول",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    startActivity(new Intent(getApplicationContext(), LoginPage.class));

                                }
                            });

                    alert.setNegativeButton(
                            "الغاء",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alert11 = alert.create();
                    alert11.show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), searchByKeyword.class);
                    intent.putExtra("list", list);
                    progressDialog.dismiss();
                    startActivity(intent);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
