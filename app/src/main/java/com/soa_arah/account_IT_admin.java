package com.soa_arah;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class account_IT_admin extends AppCompatActivity implements View.OnClickListener {

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
    android.app.AlertDialog.Builder alert;
    private ProgressBar progressBarName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account__it_admin);
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );


        name = (TextView)findViewById(R.id.name);
        password = (EditText)findViewById(R.id.password);
        password1 = (EditText)findViewById(R.id.password1);
        edit_pass = (ImageButton)findViewById(R.id.edit_pass);
        edit_pass1 = (ImageButton)findViewById(R.id.edit_pass1);
        conformpass=(RelativeLayout)findViewById(R.id.conformpass);
        passlenght=(TextView)findViewById(R.id.passlenght);
        progressBarName = (ProgressBar) findViewById(R.id.progressbar1);



        conformpass.setVisibility(View.GONE);
        edit_pass.setVisibility(View.VISIBLE);
        edit_pass.setOnClickListener(this);
        edit_pass1.setOnClickListener(this);
        progressBarName.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();


        mDatabase= FirebaseDatabase.getInstance().getReference().child("admin_IT").child("name");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String admin = dataSnapshot.getValue(String.class);

                name.setText(admin);
                progressBarName.setVisibility(View.INVISIBLE);

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

                if (password.getText().toString().trim().length()<6){

                    password.setError("يجب ان تتكون كلمة المرور من ٦ خانات او اكثر");

                }
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

                if (!password.getText().toString().trim().equals(password1.getText().toString().trim()) || password1.getText().toString().trim().length()<6){

                    password1.setError("كلمة المرور ليست متطابقة");
                }
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
    public void onClick(View view) {
        if(view == edit_pass){

            String text=password.getText().toString();
            int count =text.length();
            if(count<6) {
                alert= new android.app.AlertDialog.Builder(account_IT_admin.this);
                alert.setMessage("الرجاء إدخال الرقم السري");
                alert.setCancelable(true);
                alert.setPositiveButton(
                        "موافق",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.cancel();

                            }
                        });
                android.app.AlertDialog alert11 = alert.create();
                alert11.show();
            }
            else {
                passlenght.setText("");
                edit_pass.setVisibility(View.INVISIBLE);
                conformpass.setVisibility(View.VISIBLE);
            }

        }

        if (view == edit_pass1) {
            if(password.getText().toString().equals("")||password1.getText().toString().equals("")){
                alert= new android.app.AlertDialog.Builder(account_IT_admin.this);
                alert.setMessage("الرجاء إدخال الرقم السري");
                alert.setCancelable(true);
                alert.setPositiveButton(
                        "موافق",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.cancel();

                            }
                        });
                android.app.AlertDialog alert11 = alert.create();
                alert11.show();


            }
            else if (!password.getText().toString().trim().equals(password1.getText().toString().trim())){
                alert= new android.app.AlertDialog.Builder(account_IT_admin.this);
                alert.setMessage("كلمة المرور ليست متطابقة");
                alert.setCancelable(true);
                alert.setPositiveButton(
                        "موافق",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.cancel();

                            }
                        });
                android.app.AlertDialog alert11 = alert.create();
                alert11.show();
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

                                    mDatabase1.child("admin_IT").child("password").setValue(password.getText().toString());

                                    //display message to the user here
                                    AlertDialog.Builder alert = new AlertDialog.Builder(
                                            account_IT_admin.this );

                                    alert.setTitle( "تمت العملية بنجاح.." ).setIcon( R.drawable.t1 );
                                    alert.setNegativeButton("موافق", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    AlertDialog dialog = alert.create();
                                    // Finally, display the alert dialog
                                    dialog.show();

                                    // Get screen width and height in pixels
                                    DisplayMetrics displayMetrics = new DisplayMetrics();
                                    getWindowManager().getDefaultDisplay().getMetrics( displayMetrics );
                                    // The absolute width of the available display size in pixels.
                                    int displayWidth = displayMetrics.widthPixels;
                                    // The absolute height of the available display size in pixels.
                                    int displayHeight = displayMetrics.heightPixels;

                                    // Initialize a new window manager layout parameters
                                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

                                    // Copy the alert dialog window attributes to new layout parameter instance
                                    layoutParams.copyFrom( dialog.getWindow().getAttributes() );



                                    // Set alert dialog width equal to screen width 70%
                                    int dialogWindowWidth = (int) (displayWidth * 0.9f);
                                    // Set alert dialog height equal to screen height 70%
                                    int dialogWindowHeight = (int) (displayHeight * 0.15f);

                                    // Set the width and height for the layout parameters
                                    // This will bet the width and height of alert dialog
                                    layoutParams.width = dialogWindowWidth;
                                    layoutParams.height = dialogWindowHeight;

                                    // Apply the newly created layout parameters to the alert dialog window
                                    dialog.getWindow().setAttributes( layoutParams );
                                    startActivity(new Intent(getApplicationContext(), home_page_Nutrition_admin.class));


                                } else {
                                    //display message to the user here
                                    AlertDialog.Builder alert = new AlertDialog.Builder(
                                            account_IT_admin.this );

                                    alert.setTitle( "هناك خلل.." ).setIcon( R.drawable.f1 );
                                    alert.setNegativeButton("موافق", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    AlertDialog dialog = alert.create();
                                    // Finally, display the alert dialog
                                    dialog.show();



                                }
                                progressDialog.dismiss();
                            }
                        });
            } else {
                //display message to the user here
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        account_IT_admin.this );

                alert.setTitle( "الرقم السري لا يتطابق" ).setIcon( R.drawable.f1 );
                alert.setNegativeButton("موافق", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = alert.create();
                // Finally, display the alert dialog
                dialog.show();


            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.it_admin_bar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.editaccount1) {
            startActivity(new Intent(getApplicationContext(), account_IT_admin.class));
        } else if (item.getItemId() == R.id.aboutUs1) {
            startActivity(new Intent(getApplicationContext(), aboutUs.class));
        } else if (item.getItemId() == R.id.search1) {
            startActivity(new Intent(getApplicationContext(), home_page_register.class));
        } else if (item.getItemId() == R.id.Logout1) {
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
