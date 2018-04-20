package com.soa_arah;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;

public class RequestByName extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText foodname;
    private EditText calory,gram;
    private Button cancle,send,upload;
    private Uri mImageUri;
    private RadioButton Rfood,Rdrink;

    private StorageReference storageReference;
    private DatabaseReference databaseReference,checkdataF,checkdataR;
    private StorageTask mUploadTask;
    private ProgressDialog progressDialog;
    private String stander;
    private FirebaseAuth firebaseAuth;
    android.app.AlertDialog.Builder alert;
    private ArrayList<String> foodName,requestName;

    boolean fin=true,fin2=true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_by_name);
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );

        isConnected();

        foodName = new ArrayList<String>();
        requestName = new ArrayList<String>();

        storageReference = FirebaseStorage.getInstance().getReference("Request");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Requests").child("ByName");
        checkdataF = FirebaseDatabase.getInstance().getReference().child("Food");
        checkdataR = FirebaseDatabase.getInstance().getReference().child("Requests").child("ByName");


        checkdataF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Food checkFood=snapshot.getValue(Food.class);
                    Log.d("in for 1","*****"+checkFood.getName());
                    foodName.add(checkFood.getName());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

        checkdataR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Food checkFood=snapshot.getValue(Food.class);
                    Log.d("in for 1","*****"+checkFood.getName());
                    requestName.add(checkFood.getName());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

        cancle= (Button)findViewById(R.id.cancel);
        send=(Button) findViewById(R.id.send);
        upload=(Button) findViewById(R.id.uploadimage);
        foodname=(EditText) findViewById(R.id.FName);
        calory=(EditText) findViewById(R.id.calbyg);
        progressDialog = new ProgressDialog(RequestByName.this);
        Rfood= (RadioButton) findViewById(R.id.food);
        Rdrink=(RadioButton) findViewById(R.id.drinkS);
        gram=(EditText) findViewById(R.id.gramL);


        firebaseAuth = FirebaseAuth.getInstance();







        foodname.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (foodname.getText().toString().trim().length()<1){

                    foodname.setError("الرجاء إدخال إسم الصنف");
                }
            }
        });
        calory.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (calory.getText().toString().trim().length()<1){

                    calory.setError("الرجاء إدخال عدد السعرات");
                }
            }
        });

        gram.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (gram.getText().toString().trim().length()<1){

                    gram.setError("الرجاء إدخال عدد القرام/ملم");
                }
            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                openFileChooser();

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(foodname.getText().toString().trim().length()<1){
                    alert= new android.app.AlertDialog.Builder(RequestByName.this);
                    alert.setMessage("الرجاء ادخال إسم الصنف");
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
                else if (calory.getText().toString().trim().length()<1){
                    alert= new android.app.AlertDialog.Builder(RequestByName.this);
                    alert.setMessage("الرجاء إدخال عدد السعرات");
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
                else if (gram.getText().toString().trim().length()<1){
                    alert= new android.app.AlertDialog.Builder(RequestByName.this);
                    alert.setMessage("الرجاء إدخال عدد القرام/ملم");
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
                else if (!Rfood.isChecked()&& !Rdrink.isChecked()){
                    alert= new android.app.AlertDialog.Builder(RequestByName.this);
                    alert.setMessage("الرجاء اختيار الصنف");
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

                    if (mUploadTask != null && mUploadTask.isInProgress()) {
                        android.app.AlertDialog.Builder alert= new android.app.AlertDialog.Builder(RequestByName.this);
                        alert.setMessage("الرجاء الإنتظار يتم تحميل الصورة");
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
                    } else {
                        uploadFile();
                    }
                }
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert= new android.app.AlertDialog.Builder(RequestByName.this);
                alert.setMessage("هل انت متأكد من عدم الارسال؟");
                alert.setCancelable(true);
                alert.setPositiveButton(
                        "نعم",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                startActivity(new Intent(getApplicationContext(), home_page_register.class));

                            }
                        });

                alert.setNegativeButton(
                        "لا",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.cancel();
                            }
                        });

                android.app.AlertDialog alert11 = alert.create();
                alert11.show();
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
                                startActivity(new Intent(getApplicationContext(), home_page_register.class));
                                break;
                            case R.id.diet_plan:
                                startActivity(new Intent(getApplicationContext(), diet_plan.class));

                                break;
                            case R.id.upload:
                                startActivity(new Intent(getApplicationContext(), Request_page.class));

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


    //menu action_bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.viewaccount) {
            startActivity(new Intent(getApplicationContext(), view_account_register.class));
        } else if (item.getItemId() == R.id.editaccount) {
            startActivity(new Intent(getApplicationContext(), edit_account_register.class));
        } else if (item.getItemId() == R.id.aboutUs) {
            startActivity(new Intent(getApplicationContext(), aboutUs.class));
        } else if (item.getItemId() == R.id.Logout){
            firebaseAuth.signOut();
            //closing activity
            finish();
            startActivity(new Intent(getApplicationContext(), home_page_guest.class));

        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }





    private void openFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "الرجاء اختيار صورة"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();

            try {
                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);


                // After selecting image change choose button above text.
                upload.setText("تم اختيار الصورة");
                upload.setBackgroundResource(R.drawable.button_login);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
            // Setting progressDialog Title.
            progressDialog.setTitle("يتم ارسال الطلب ...");
            progressDialog.show();

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {



                            if (foodName.size()>0){
                                for (int i=0;i<foodName.size();i++){
                                    if (foodname.getText().toString().trim().equals(foodName.get(i))){
                                        progressDialog.dismiss();
                                        //fin2=false;
                                        Log.d("in if 1","delete"+foodName.get(i));
                                        alert= new android.app.AlertDialog.Builder(RequestByName.this);
                                        alert.setTitle("الصنف موجود مسبقاً");
                                        alert.setMessage("الانتقال الى صفحة البحث");
                                        alert.setCancelable(true);
                                        alert.setPositiveButton(
                                            "نعم",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    startActivity(new Intent(getApplicationContext(), home_page_register.class));
                                                }
                                            });
                                        android.app.AlertDialog alert11 = alert.create();
                                        alert11.show();
                                        break;
                                    }
                                Log.d("end for foodName",""+foodName.get(i));
                                }
                                fin2=false;
                            }else
                                fin2=false;



                            if(requestName.size()>0){
                                for (int i=0;i<requestName.size();i++){
                                    if (foodname.getText().toString().trim().equals(requestName.get(i))){
                                        progressDialog.dismiss();
                                        // fin2=false;
                                        Log.d("in if 1","delete"+requestName.get(i));
                                        alert= new android.app.AlertDialog.Builder(RequestByName.this);
                                        alert.setTitle("الصنف موجود مسبقاً");
                                        alert.setMessage("الانتقال الى صفحة البحث");
                                        alert.setCancelable(true);
                                        alert.setPositiveButton(
                                            "نعم",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    startActivity(new Intent(getApplicationContext(), home_page_register.class));
                                                }
                                            });
                                        android.app.AlertDialog alert11 = alert.create();
                                        alert11.show();
                                        break;
                                    }
                                //if (fin2)
                                Log.d("end for requestName",""+requestName.get(i));
                                }
                                fin=false;
                            }else
                                fin=false;




                            if (!fin&&!fin2) {

                                Log.d("in else","*******");
                            //for standard measurement
                            if (Rfood.isChecked())
                                stander="جرام,ملعقة شاي,ملعقة اكل,كوب";
                            else if (Rdrink.isChecked())
                                stander="مليلتر,كوب";
                            Food RF = new Food(foodname.getText().toString().trim(),taskSnapshot.getDownloadUrl().toString(),calory.getText().toString().trim(),stander,gram.getText().toString().trim());
                            RF.setImageTable("لايوجد");
                            RF.setBarcodN("لايوجد");
                            String uploadId = databaseReference.push().getKey();
                            databaseReference.child(uploadId).setValue(RF);

                            // Hiding the progressDialog after done uploading.
                            progressDialog.dismiss();

                                AlertDialog.Builder alert = new AlertDialog.Builder(
                                        RequestByName.this);
                                alert.setTitle("تم إرسال الطلب بنجاح").setIcon(R.drawable.t1);
                                AlertDialog dialog = alert.create();

                                // Finally, display the alert dialog
                                dialog.show();

                                // Get screen width and height in pixels
                                DisplayMetrics displayMetrics = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                                // The absolute width of the available display size in pixels.
                                int displayWidth = displayMetrics.widthPixels;
                                // The absolute height of the available display size in pixels.
                                int displayHeight = displayMetrics.heightPixels;

                                // Initialize a new window manager layout parameters
                                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

                                // Copy the alert dialog window attributes to new layout parameter instance
                                layoutParams.copyFrom(dialog.getWindow().getAttributes());



                                // Set alert dialog width equal to screen width 70%
                                int dialogWindowWidth = (int) (displayWidth * 0.9f);
                                // Set alert dialog height equal to screen height 70%
                                int dialogWindowHeight = (int) (displayHeight * 0.15f);

                                // Set the width and height for the layout parameters
                                // This will bet the width and height of alert dialog
                                layoutParams.width = dialogWindowWidth;
                                layoutParams.height = dialogWindowHeight;

                                // Apply the newly created layout parameters to the alert dialog window
                                dialog.getWindow().setAttributes(layoutParams);
                                startActivity(new Intent(getApplicationContext(), home_page_register.class));
                                //close this activity
                                finish();
                                startActivity(new Intent(RequestByName.this, home_page_register.class));


                            }}
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Hiding the progressDialog after done uploading.
                            progressDialog.dismiss();
                            Toast.makeText(RequestByName.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


            if (!fin&&!fin2){

        }
        }else {
            alert= new android.app.AlertDialog.Builder(RequestByName.this);
            alert.setMessage("عذراً يجب اختيار صورة");
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
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( progressDialog!=null && progressDialog.isShowing() ){
            progressDialog.cancel();
        }
    }

    public boolean isConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiInfo != null && wifiInfo.isConnected()) || (mobileInfo != null && mobileInfo.isConnected())) {
            return true;
        } else {
            showDialog();
            return false;
        }
    }




    private void showDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(" عذراً انت غير متصل بالانترنت هل تريد الاتصال بالانترنت او الاغلاق؟")
                .setCancelable(false)
                .setPositiveButton("الاتصال", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("إغلاق", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
