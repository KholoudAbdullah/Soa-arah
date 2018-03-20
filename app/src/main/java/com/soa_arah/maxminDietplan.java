package com.soa_arah;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class maxminDietplan extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference Database;
    private String User_ID;
    private DietPlan plan;
    private String bmi,max;
    private EditText goal;
    private TextView mintext,maxtext;
    private final String min="1200";
    private Button creat;
    private String  Wight,Hight,Hip,Waist,date,gen;
    private Button cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_maxmin_dietplan );
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );
        mintext=(TextView)findViewById(R.id.minnumber);
        maxtext=(TextView)findViewById(R.id.maxnumber);
        goal=(EditText) findViewById(R.id.goalnumber);
        Intent intent = getIntent();
        Waist=intent.getExtras().getString( "Waist","" );
        Wight=intent.getExtras().getString( "Wight","" );
        Hight=intent.getExtras().getString( "Hight","" );
        Hip=intent.getExtras().getString( "Hip","" );
        date=intent.getExtras().getString( "date","" );
        gen=intent.getExtras().getString( "gen","" );
        bmi = intent.getExtras().getString("BMI", "");
        max = intent.getExtras().getString("BMR", "");
        maxtext.setText(max);
        mintext.setText(min);
        creat=(Button)findViewById(R.id.creat);
        cancel=(Button) findViewById(R.id.cancel);
        firebaseAuth =FirebaseAuth.getInstance();
        User_ID = firebaseAuth.getCurrentUser().getEmail();
        final String username= User_ID.substring( 0, User_ID.lastIndexOf( "@" ) );




        creat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(goal.getText().toString().trim().isEmpty()){

                    AlertDialog.Builder alert = new AlertDialog.Builder(
                            maxminDietplan.this );

                    alert.setTitle( "عذراً يجب إدخال عدد السعرات المستهدفة" ).setIcon( R.drawable.f1 );

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
                else if((Double.parseDouble(goal.getText().toString().trim())>1200)||(Double.parseDouble(goal.getText().toString().trim())< Double.parseDouble(max))){


                    AlertDialog.Builder alert = new AlertDialog.Builder(
                            maxminDietplan.this );

                    alert.setTitle( "عذراً يجب إدخال عدد السعرات المستهدفة ما بين الحد القصى والادنى" ).setIcon( R.drawable.f1 );

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

                else {

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("DietPlan");
                   Database= FirebaseDatabase.getInstance().getReference().child("RegisteredUser").child(username);

                    plan = new DietPlan();

                    plan.setCalGoal(goal.getText().toString().trim().toString());
                    plan.setMax(max.toString());
                    plan.setMin(min.toString());
                    plan.setDailyCal("0");
                    plan.setBMI(bmi);
                    plan.setRname(username);
                    plan.setWater("0");
                    mDatabase.child(username).setValue(plan);
                    Database.child("wight").setValue(Wight);
                    Database.child("hight").setValue(Hight);
                    Database.child("dateOfBarth").setValue(date);
                    Database.child("gender").setValue(gen);
                    Database.child("waist").setValue(Waist);
                    Database.child("hip").setValue(Hip);
                    android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(
                            maxminDietplan.this);
                    alert.setTitle("تم إنشاء الخطة بنجاح").setIcon(R.drawable.t1);
                    alert.show();
                    startActivity(new Intent(getApplicationContext(), diet_plan.class));


                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), home_page_register.class));
            }
        });






        onBackPressed();
    }
    @Override
    public void onBackPressed()
    {

        // super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
    }
}
