package com.soa_arah;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class LoginPage extends AppCompatActivity implements View.OnClickListener {

    private EditText username,password;
    private Button login ,toRegtext;
    private TextView forgetpass;
    private String pass,email;


    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //database
    private DatabaseReference mDatabase;

    //progress dialog
    private ProgressDialog progressDialog;
    AlertDialog.Builder alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );


        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        username = (EditText) findViewById(R.id.Usernane);
        password = (EditText) findViewById(R.id.Password);
        login = (Button) findViewById(R.id.LoginButton);
        toRegtext = (Button) findViewById(R.id.toRegtext);
        forgetpass= (TextView) findViewById(R.id.toForgettext);
        progressDialog = new ProgressDialog(this);



        login.setOnClickListener(this);
        toRegtext.setOnClickListener(this);
        forgetpass.setOnClickListener(this);



        username.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (username.getText().toString().trim().length()<1){

                    username.setError("الرجاء إدخال إسم المستخدم");
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (password.getText().toString().trim().length()<6){

                    password.setError("يجب ان تتكون كلمة المرور من ٦ خانات");
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


    //method for user login
    private void userLogin(){
        String name1 =username.getText().toString().trim().toLowerCase();
        email = stringToHex(name1)+"@soaarah.com";
        pass  = password.getText().toString().trim();


        //checking if email and passwords are empty
        if(username.getText().toString().trim().length()<1){
            alert= new android.app.AlertDialog.Builder(LoginPage.this);
            alert.setMessage("الرجاء إدخال إسم المستخدم");
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
            return;
        }

        else if(TextUtils.isEmpty(pass)){

            alert= new android.app.AlertDialog.Builder(LoginPage.this);
            alert.setMessage("الرجاء إدخال كلمة المرور");
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
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("يتم تسجيل الدخول، الرجاء الانتظار ...");
        progressDialog.show();
        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        //if the task is successful
                        if(task.isSuccessful()){
                            //start the profile activity
                            finish();

                            //add
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String id= user.getUid();



                            //IT admin
                            if(id.equals("aSK7RyMA8xfdaQNPF0xS6kAumam2")){
                                startActivity(new Intent(getApplicationContext(),home_page_IT_admin.class));
                            }
                            // Nutrition addmin
                            else if(id.equals("Pf7emnnQTEbmukAIDwWgkuv8JbC2")){
                                startActivity(new Intent(getApplicationContext(),home_page_Nutrition_admin.class));
                            }
                            else {
                                startActivity(new Intent(getApplicationContext(), home_page_register.class));

                            }
                        }
                       /* else if(password.length()<6){
                            Toast.makeText(getApplicationContext(),"الرجاء التأكد من  كلمة المرور",Toast.LENGTH_LONG).show();
                        }*/
                        else {

                            AlertDialog.Builder alert= new AlertDialog.Builder(LoginPage.this);
                            alert.setMessage("الرجاء التأكد من اسم المستخدم او كلمة المرور");
                            alert.setCancelable(true);
                            alert.setPositiveButton(
                                    "موافق",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            dialogInterface.cancel();

                                        }
                                    });
                        }

                        progressDialog.dismiss();
                    }
                });



    }


    @Override
    public void onClick(View v) {
        if(v == login) {
            userLogin();}
        else if(v==toRegtext){
            startActivity(new Intent(LoginPage.this , Registration.class));

        }else if(v==forgetpass){
            startActivity(new Intent(this , ForgotPasswordActivity.class));

        }

        else {
            startActivity(new Intent(this , home_page_guest.class));

        }
    }
    public static String stringToHex(String base)
    {
        StringBuffer buffer = new StringBuffer();
        int intValue;
        for(int x = 0; x < base.length(); x++)
        {
            int cursor = 0;
            intValue = base.charAt(x);
            String binaryChar = new String(Integer.toBinaryString(base.charAt(x)));
            for(int i = 0; i < binaryChar.length(); i++)
            {
                if(binaryChar.charAt(i) == '1')
                {
                    cursor += 1;
                }
            }
            if((cursor % 2) > 0)
            {
                intValue += 128;
            }
            buffer.append(Integer.toHexString(intValue));
        }
        return buffer.toString();
    }
}
