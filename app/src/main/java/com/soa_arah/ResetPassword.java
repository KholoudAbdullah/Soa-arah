package com.soa_arah;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResetPassword extends AppCompatActivity implements View.OnClickListener{

    private EditText pass;

    private EditText cpass;

    private Button confirm;


    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    DatabaseReference mDatabase;
    private FirebaseDatabase firebaseDatabase;

    private  String userID;
    android.app.AlertDialog.Builder alert;


    private ProgressDialog progressDialog;
    //private FirebaseAuth firebaseAuth;

    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent intent = getIntent();
        String p = intent.getExtras().getString("name", "");
        Log.d("razan1", "razan1" + p);
        pass = (EditText) findViewById(R.id.pass);


        confirm = (Button) findViewById(R.id.confirm);


        progressDialog = new ProgressDialog(this);


        firebaseDatabase = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = p;//username

        confirm.setOnClickListener(this);
    }

    //password
    @Override
    public void onClick(View v) {



        if(!(TextUtils.isEmpty(pass.getText().toString().trim()))){
            FirebaseUser user1 =FirebaseAuth.getInstance().getCurrentUser();
            if(user1!=null)
            {
                progressDialog.setMessage("يتم تغيير كلمة المرور الرجاء الانتظار..");
                progressDialog.show();
                mDatabase= FirebaseDatabase.getInstance().getReference();
                String password=pass.getText().toString();

                Log.d("RAZAN","RAZAN:"+password+" yyy "+user1.getDisplayName());
                user1.updatePassword(password)

                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    mDatabase.child("RegisteredUser").child(userID).child("password").setValue(pass.getText().toString());

                                    //display message to the user here

                                    AlertDialog.Builder alert = new AlertDialog.Builder(
                                            ResetPassword.this);
                                    alert.setTitle("تمت العملية بنجاح..").setIcon(R.drawable.t1);
                                    AlertDialog dialog = alert.create();

                                    // Finally, display the alert dialog
                                    dialog.show();

                                    Intent i = new Intent(ResetPassword.this, LoginPage.class);
                                    startActivity(i);

                                } else {
                                    //display some message here

                                    alert = new android.app.AlertDialog.Builder(ResetPassword.this);
                                    alert.setMessage("هناك خلل..");
                                    alert.setCancelable(true);
                                    alert.setPositiveButton(
                                            "موافق",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    startActivity(new Intent(getApplicationContext(), LoginPage.class));

                                                }
                                            });
                                    android.app.AlertDialog alert11 = alert.create();
                                    alert11.show();

                                }
                                progressDialog.dismiss();
                            }
                        });

            }
        }

    }
}
