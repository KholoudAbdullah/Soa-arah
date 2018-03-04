package com.soa_arah;

import android.app.ProgressDialog;
import android.content.ContentResolver;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_by_name);
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );
        storageReference = FirebaseStorage.getInstance().getReference("Request");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Requests");



        cancle= (Button)findViewById(R.id.cancel);
        send=(Button) findViewById(R.id.send);
        upload=(Button) findViewById(R.id.upload);
        foodname=(EditText) findViewById(R.id.FName);
        foodImage=(ImageView) findViewById(R.id.Foodimage);
        calory=(EditText) findViewById(R.id.calbyg);
        progressDialog = new ProgressDialog(RequestByName.this);
        Rfood= (RadioButton) findViewById(R.id.food);
        Rdrink=(RadioButton) findViewById(R.id.dark);
        gram=(EditText) findViewById(R.id.gramL);

        firebaseAuth = FirebaseAuth.getInstance();




        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openFileChooser();

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(foodname==null)
                    Toast.makeText(RequestByName.this, "الرجاء ادخال إسم الصنف", Toast.LENGTH_SHORT).show();
                else if (calory==null)
                    Toast.makeText(RequestByName.this, "الرجاء إدخال السُعرة الحرارية", Toast.LENGTH_SHORT).show();
                else if (gram==null)
                    Toast.makeText(RequestByName.this, "الرجاء إدخال القرامات / الملي لتر", Toast.LENGTH_SHORT).show();
                else if (!Rfood.isChecked()&& !Rdrink.isChecked())
                    Toast.makeText(RequestByName.this, " الرجاء اختيار الصنف", Toast.LENGTH_SHORT).show();
                else {

                    if (mUploadTask != null && mUploadTask.isInProgress()) {
                        Toast.makeText(RequestByName.this, "يتم التحميل", Toast.LENGTH_SHORT).show();
                    } else {
                        uploadFile();
                    }
                }
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), home_page_register.class));
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
                                startActivity(new Intent(getApplicationContext(), edit_account_register.class));

                                break;
                            case R.id.request:
                                startActivity(new Intent(getApplicationContext(), Request_page.class));

                                break;
                        }
                        return false;
                    }
                });



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
            startActivity(new Intent(getApplicationContext(), LoginPage.class));

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

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
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
                                stander="ملي لتر,كوب";

                            Food RF = new Food(foodname.getText().toString().trim(),
                                    taskSnapshot.getDownloadUrl().toString(),null,calory.getText().toString().trim(),stander,gram.getText().toString().trim());
                            String uploadId = databaseReference.push().getKey();
                            databaseReference.child(uploadId).setValue(RF);

                            // Hiding the progressDialog after done uploading.
                            progressDialog.dismiss();

                            AlertDialog.Builder alert = new AlertDialog.Builder(
                                    RequestByName.this);
                            alert.setTitle("تم ارسال الطلب بنجاح").setIcon(R.drawable.t1);

                            startActivity(new Intent(RequestByName.this, home_page_register.class));


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
        } else {
            Toast.makeText(this, "لم يتم اختيار الملف", Toast.LENGTH_SHORT).show();
        }
    }
}
