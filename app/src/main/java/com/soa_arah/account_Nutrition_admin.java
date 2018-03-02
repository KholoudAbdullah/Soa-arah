package com.soa_arah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Lama on 02/03/18.
 */

public class account_Nutrition_admin extends AppCompatActivity implements View.OnClickListener{

    private TextView name;
    private EditText password;
    private EditText password1;
    private ImageButton edit_pass1;
    private ImageButton edit_pass;
    private FirebaseAuth mAuth;
    private RelativeLayout conformpass;

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase1;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_nutrition_admin);

        name = (TextView)findViewById(R.id.name);
        password = (EditText)findViewById(R.id.password);
        password1 = (EditText)findViewById(R.id.password1);
        edit_pass = (ImageButton)findViewById(R.id.edit_pass);
        edit_pass1 = (ImageButton)findViewById(R.id.edit_pass1);
        conformpass=(RelativeLayout)findViewById(R.id.conformpass);

        conformpass.setVisibility(View.GONE);
        edit_pass.setVisibility(View.VISIBLE);
        edit_pass.setOnClickListener(this);
        edit_pass1.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("admin_N").child("name");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String admin = dataSnapshot.getValue(String.class);

                name.setText(admin);

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
                                startActivity(new Intent(getApplicationContext(), edit_account_register.class));

                                break;
                            case R.id.account:
                                startActivity(new Intent(getApplicationContext(), account_Nutrition_admin.class));

                                break;
                        }
                        return false;
                    }
                });




    }

    @Override
    public void onClick(View view) {
        if(view == edit_pass){
            edit_pass.setVisibility(View.INVISIBLE);
           conformpass.setVisibility(View.VISIBLE);


        }

        if (view == edit_pass1) {
            if (password.getText().toString().equals(password1.getText().toString())) {
                progressDialog.setMessage("يتم حفظ التعديلات الرجاء الانتظار..");
                progressDialog.show();
                FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();

                mDatabase1 = FirebaseDatabase.getInstance().getReference();
                String pass = password.getText().toString();


                user1.updatePassword(pass)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    mDatabase1.child("admin_N").child("password").setValue(password.getText().toString());

                                    //display message to the user here
                                    Toast.makeText(account_Nutrition_admin.this, "تمت العملية بنجاح..", Toast.LENGTH_LONG).show();

                                } else {
                                    //display some message here
                                    Toast.makeText(account_Nutrition_admin.this, "هناك خلل..", Toast.LENGTH_LONG).show();

                                }
                                progressDialog.dismiss();
                            }
                        });
            } else {
                Toast.makeText(account_Nutrition_admin.this, " الرقم السري لا يتطابق", Toast.LENGTH_LONG).show();

            }

            }
    }
}
