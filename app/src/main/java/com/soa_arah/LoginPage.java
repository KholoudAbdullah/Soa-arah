package com.soa_arah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
    private Button login;
    private TextView registraion,forgetpass;
    private String pass,email;


    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //database
    private DatabaseReference mDatabase;

    //progress dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);



        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        username = (EditText) findViewById(R.id.Usernane);
        password = (EditText) findViewById(R.id.Password);
        login = (Button) findViewById(R.id.LoginButton);
        registraion = (TextView) findViewById(R.id.toRegtext);
        forgetpass= (TextView) findViewById(R.id.toForgettext);
        progressDialog = new ProgressDialog(this);



        login.setOnClickListener(this);
        registraion.setOnClickListener(this);
        forgetpass.setOnClickListener(this);



    }


    //method for user login
    private void userLogin(){
        String name1 =username.getText().toString().trim();
        email = name1+"@soaarah.com";
        pass  = password.getText().toString().trim();


        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(LoginPage.this, "الرجاء ادخال اسم المستخذم", Toast.LENGTH_LONG).show();
            return;
        }

        else if(TextUtils.isEmpty(pass)){
            Toast.makeText(LoginPage.this, "الرجاء ادخال من كلمة المرور", Toast.LENGTH_LONG).show();
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


                            //ID موب صح
                            //IT admin
                            if(id.equals("aSK7RyMA8xfdaQNPF0xS6kAumam2")){
                                startActivity(new Intent(getApplicationContext(),home_page_IT_admin.class));
                            }
                            // Nutrition addmin
                            else if(id.equals("aSK7RyMA8xfdaQNPF0xS6kAumam2")){
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
                            //display some message here

                            Toast.makeText(getApplicationContext(),"الرجاء التأكد من اسم المستخدم او كلمة المرور", Toast.LENGTH_LONG).show();
                        }

                        progressDialog.dismiss();
                    }
                });



    }


    @Override
    public void onClick(View v) {
        if(v == login) {
            userLogin();}
        else if(v==registraion){
            startActivity(new Intent(LoginPage.this , Registration.class));

        }
        else {
            startActivity(new Intent(this , home_page_guest.class));

        }
    }
}
