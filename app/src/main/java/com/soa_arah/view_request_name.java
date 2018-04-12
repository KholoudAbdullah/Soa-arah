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

public class view_request_name extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private ListView request_name;
    private ArrayList<Food> re_name;
    private ArrayList<String> key;
    private TextView no_requestname;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBarName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request_name);
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );


        firebaseAuth = FirebaseAuth.getInstance();
        request_name=(ListView)findViewById(R.id.request_name);
        re_name=new ArrayList<Food>();
        key=new ArrayList<String>();
        no_requestname=(TextView)findViewById(R.id.no_requestname);

        progressBarName = (ProgressBar) findViewById(R.id.progressbar1);

        progressBarName.setVisibility(View.VISIBLE);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){

            startActivity(new Intent(getApplicationContext(),LoginPage.class));
        }


        request_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                String calorie =re_name.get(i).getCalories();
                String quantity =re_name.get(i).getQuantity();
                String image =re_name.get(i).getImage();
                String namef =re_name.get(i).getName();
                String standard =re_name.get(i).getStandard();
                String keys=key.get(i);

                Intent intent = new Intent(view_request_name.this, view_info_request.class);
                intent.putExtra("calorie",calorie);
                intent.putExtra("quantity",quantity);
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

                if(re_name.size()==0) {
                    no_requestname.setText("لا توجد طلبات");
                    progressBarName.setVisibility(View.INVISIBLE);
                }

                else {
                    no_requestname.setText("");
                    progressBarName.setVisibility(View.INVISIBLE);
                }


                //disp laying it to list
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, requestsByName);
                request_name.setAdapter(adapter);


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
