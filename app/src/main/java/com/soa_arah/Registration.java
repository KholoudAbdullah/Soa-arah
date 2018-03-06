package com.soa_arah;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Registration extends AppCompatActivity implements View.OnClickListener{

    private EditText name;
    private EditText password;
    private TextView phone;
    private EditText wedith;
    private EditText hight;
    private EditText Waist;
    private EditText thigh;
    private TextView textpasswordempty;
    private TextView textnameempty;
    private TextView textphoneempty;
    private Spinner gen;
    private TextView log;
    String date="لم يتم إدخال بيانات";
    private ProgressDialog progressDialog;
    private Button button;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    Intent intent;
    RegisteredUser user1;
    private static final String TAG = "MainActivity";

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    String record="لم يتم إدخال بيانات";
    String gender[]={"أنثى","ذكر"};
    ArrayAdapter<String> adapter;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );
        firebaseAuth = FirebaseAuth.getInstance();

        mDatabase= FirebaseDatabase.getInstance().getReference().child("RegisteredUser");
        name=(EditText)findViewById(R.id.name);

        password=(EditText)findViewById(R.id.password);
        phone=(TextView)findViewById(R.id.phone);

        wedith=(EditText)findViewById(R.id.wedith);
        hight=(EditText)findViewById(R.id.hight);
        Waist=(EditText)findViewById(R.id.Waist);
        thigh=(EditText)findViewById(R.id.thigh);
        //textpasswordempty =(TextView)findViewById(R.id.textpasswordempty);
        textphoneempty = (TextView)findViewById(R.id.textphoneempty);
        textnameempty = (TextView)findViewById(R.id.textnameempty);
        button=(Button)findViewById(R.id.button);
        log=(TextView)findViewById(R.id.log);
        gen=(Spinner)findViewById(R.id.gen);
        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,gender);
        gen.setAdapter(adapter);
        gen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        record = "أنثى";
                        break;
                    case 1:
                        record = "ذكر";
                        break;


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        button.setOnClickListener(this);
        log.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Registration.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                date = day + "/" + month + "/" + year;
                mDisplayDate.setText(date);
            }
        };



    }



    @SuppressLint("ResourceType")
    private void registerUser() {
        //getting email and password from edit texts

        final String Name = name.getText().toString().toLowerCase();
        final String Phone = phone.getText().toString().trim();
        final String Wedith = wedith.getText().toString().trim();
        final String Hight = hight.getText().toString().trim();
        final String waist = Waist.getText().toString().trim();
        final String Thigh = thigh.getText().toString().trim();
        String email = null;
        String Password = password.getText().toString().trim();

        String nemail = Name + "@soaarah.com";
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]";
        if (nemail.matches( emailPattern )==false) {
            AlertDialog.Builder alert = new AlertDialog.Builder(
                    Registration.this );

            alert.setTitle( " اسم المستخدم لا يجب أن يحتوي على مسافة" ).setIcon( R.drawable.f1 );

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

            // Set the alert dialog window width and height
            // Set alert dialog width equal to screen width 90%
            // int dialogWindowWidth = (int) (displayWidth * 0.9f);
            // Set alert dialog height equal to screen height 90%
            // int dialogWindowHeight = (int) (displayHeight * 0.9f);

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
        }
           else if (!(name.getText().toString().trim().isEmpty() && password.getText().toString().trim().isEmpty())) {
                email = stringToHex( Name ) + "@soaarah.com";
                //if the email and password and Location are not empty
                //displaying a progress dialog
                progressDialog.setMessage( " الرجاء الانتظار حتى يتم التسجيل" );
                progressDialog.show();

                //creating a new user


                firebaseAuth.createUserWithEmailAndPassword( email, Password )
                        .addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
                            @SuppressLint("ResourceType")
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    String User_ID = firebaseAuth.getCurrentUser().getUid();
                                    final String uid = firebaseAuth.getInstance().getCurrentUser().getUid();
                                    user1 = new RegisteredUser();

                                    user1.setName( name.getText().toString().toLowerCase() );
                                    user1.setPhoneNum( phone.getText().toString() );
                                    String email = stringToHex( name.getText().toString().toLowerCase() ) + "@soaarah.com";
                                    user1.setEmail( email );
                                    user1.setID( User_ID );

                                    if (wedith.getText().toString().isEmpty()) {
                                        user1.setWight( "لم يتم إدخال بيانات" );
                                    } else {
                                        user1.setWight( wedith.getText().toString() );
                                    }

                                    if (hight.getText().toString().isEmpty()) {
                                        user1.setHight( "لم يتم إدخال بيانات" );
                                    } else {
                                        user1.setHight( hight.getText().toString() );
                                    }

                                    if (Waist.getText().toString().isEmpty()) {
                                        user1.setWaist( "لم يتم إدخال بيانات" );
                                    } else {
                                        user1.setWaist( Waist.getText().toString() );
                                    }

                                    if (thigh.getText().toString().isEmpty()) {
                                        user1.setHip( "لم يتم إدخال بيانات" );
                                    } else {
                                        user1.setHip( thigh.getText().toString() );
                                    }


                                    user1.setGender( record );
                                    user1.setDateOfBarth( date );
                                    mDatabase.child( stringToHex( name.getText().toString().toLowerCase() ) ).setValue( user1 );
                                    // Intent intent=new Intent(RegisterOrgActivity.this, ProfileActivity.class);
                                    // intent.putExtra("org", (Serializable) org1);
                                    //display message to the user here

                                    //Toast.makeText(Registration.this, "تمت العملية بنجاح", Toast.LENGTH_LONG).show();
                                    //close this activity
                                    finish();
                                    //opening login activity
                                    intent = new Intent( Registration.this, ActivityPhoneAuth.class );
                                    intent.putExtra( "Phone", phone.getText().toString().trim().toString() );
                                    startActivity( intent );

                                } else {
                                    //display some message here
                                    AlertDialog.Builder alert = new AlertDialog.Builder(
                                            Registration.this );

                                    alert.setTitle( "عذراً يوجد مستخدم بهذا الاسم" ).setIcon( R.drawable.f1 );
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

                                    // Set the alert dialog window width and height
                                    // Set alert dialog width equal to screen width 90%
                                    // int dialogWindowWidth = (int) (displayWidth * 0.9f);
                                    // Set alert dialog height equal to screen height 90%
                                    // int dialogWindowHeight = (int) (displayHeight * 0.9f);

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
                                    //Toast.makeText(Registration.this, "هناك خلل..", Toast.LENGTH_LONG).show();
                                }
                                progressDialog.dismiss();
                            }
                        } );
            } else if (name.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty()) {
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        Registration.this );

                alert.setTitle( "عذراً أحد الحقول الإلزامية فارغ" ).setIcon( R.drawable.f1 );

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

                // Set the alert dialog window width and height
                // Set alert dialog width equal to screen width 90%
                // int dialogWindowWidth = (int) (displayWidth * 0.9f);
                // Set alert dialog height equal to screen height 90%
                // int dialogWindowHeight = (int) (displayHeight * 0.9f);

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
            }
    }
    @Override
    public void onClick(View view) {
        if(view == button ){
            //calling register method on click
            registerUser();}
        if(view == log ) {
            String m=name.getText().toString();

            Toast.makeText(Registration.this,  stringToHex(m) , Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(this, LoginPage.class) ); //profile=login

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

