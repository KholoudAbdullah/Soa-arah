package com.soa_arah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
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

    private TextView name,passlenght;
    private EditText password;
    private EditText password1;
    private ImageButton edit_pass1;
    private ImageButton edit_pass;
    private FirebaseAuth mAuth;
    private RelativeLayout conformpass;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase1;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_nutrition_admin);
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );
        name = (TextView)findViewById(R.id.name);
        password = (EditText)findViewById(R.id.password);
        password1 = (EditText)findViewById(R.id.password1);
        edit_pass = (ImageButton)findViewById(R.id.edit_pass);
        edit_pass1 = (ImageButton)findViewById(R.id.edit_pass1);
        conformpass=(RelativeLayout)findViewById(R.id.conformpass);
        passlenght=(TextView)findViewById(R.id.passlenght);

        conformpass.setVisibility(View.GONE);
        edit_pass.setVisibility(View.VISIBLE);
        edit_pass.setOnClickListener(this);
        edit_pass1.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
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


        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String text=password.getText().toString();
                int count =text.length();
                if(count>=6) {

                        passlenght.setText("");
                }
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

    @Override
    public void onClick(View view) {
        if(view == edit_pass){

            String text=password.getText().toString();
            int count =text.length();
            if(count<6) {
                passlenght.setText("             الرقم السري يجب أن يحتوي على 6 أرقام أو أكثر ");

            }
            else {
                passlenght.setText("");
                edit_pass.setVisibility(View.INVISIBLE);
                conformpass.setVisibility(View.VISIBLE);
            }

        }

        if (view == edit_pass1) {
            if(password.getText().toString().equals("")||password1.getText().toString().equals("")){
                Toast.makeText(account_Nutrition_admin.this, "الرجاء إدخال الرقم السري", Toast.LENGTH_LONG).show();

            }
            else if (password.getText().toString().equals(password1.getText().toString())) {
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
