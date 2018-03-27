package com.soa_arah;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class RequestByName extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText foodname;
    private EditText calory,gram;
    private Button cancle,send,upload;
    private Uri mImageUri;
    private ImageView foodImage;
    private RadioButton Rfood,Rdrink;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private StorageTask mUploadTask;
    private ProgressDialog progressDialog;
    private String stander;
    private FirebaseAuth firebaseAuth;
    android.app.AlertDialog.Builder alert;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_by_name);
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );
        storageReference = FirebaseStorage.getInstance().getReference("Request");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Requests").child("ByName");



        cancle= (Button)findViewById(R.id.cancel);
        send=(Button) findViewById(R.id.send);
        upload=(Button) findViewById(R.id.uploadimage);
        foodname=(EditText) findViewById(R.id.FName);
        foodImage=(ImageView) findViewById(R.id.Foodimage);
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


        android.app.AlertDialog alert11 = alert.create();
        alert11.show();

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

            Picasso.with(this).load(mImageUri).into(foodImage);
            try {
                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);

                // After selecting image change choose button above text.
                upload.setText("تم اختيار الصورة");
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

                            //for standard measurement
                            if (Rfood.isChecked())
                                stander="جرام,ملعقة شاي,ملعقة اكل,كوب";
                            else if (Rdrink.isChecked())
                                stander="مليلتر,كوب";
                            Food RF = new Food(foodname.getText().toString().trim(),taskSnapshot.getDownloadUrl().toString(), "لا يوجد",calory.getText().toString().trim(),stander,gram.getText().toString().trim());
                            RF.setImageTable("لايوجد");
                            RF.setBarcodN("لايوجد");
                            String uploadId = databaseReference.push().getKey();
                            databaseReference.child(uploadId).setValue(RF);

                            // Hiding the progressDialog after done uploading.
                            progressDialog.dismiss();


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Hiding the progressDialog after done uploading.
                            progressDialog.dismiss();
                            Toast.makeText(RequestByName.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            AlertDialog.Builder alert = new AlertDialog.Builder(
                    RequestByName.this);
            alert.setTitle("تمت عملية التسجيل بنجاح").setIcon(R.drawable.t1);
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

        } else {
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
}
