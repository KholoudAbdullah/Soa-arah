package com.soa_arah;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
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
import android.widget.TextView;
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

public class RequestByBarcode extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;


    private Uri fImageUri,tableUri;
    private ImageView foodImage,tableI;
    private TextView TXbarnum;
    private Button cancle,send,upload,table;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private StorageTask mUploadTask,tablem;
    private String douTable, barnum;
    private EditText name;
    private ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_by_barcode);


        firebaseAuth = FirebaseAuth.getInstance();
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



        storageReference = FirebaseStorage.getInstance().getReference("Request");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Requests").child("ByBarcode");


        upload=(Button) findViewById(R.id.FImageB);
        table=(Button) findViewById(R.id.TImageB);
        foodImage=(ImageView) findViewById(R.id.FImageV);
        tableI=(ImageView) findViewById(R.id.TImageV);
        name=(EditText) findViewById(R.id.Fname);
        send=(Button) findViewById(R.id.sendw);
        cancle=(Button) findViewById(R.id.cancel);
        TXbarnum=(TextView)findViewById(R.id.barcodeNumber);

        progressDialog = new ProgressDialog(RequestByBarcode.this);

        Intent intent=getIntent();

        barnum = intent.getExtras().getString( "BarcodeNum","" );
        TXbarnum.setText(TXbarnum.getText().toString()+"  "+barnum);

        //food image
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "الرجاء اختيار صورة"), 2);

            }
        });

        table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "الرجاء اختيار صورة"), 3);

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name==null){
                    Toast.makeText(RequestByBarcode.this, "الرجاء ادخال االاسم", Toast.LENGTH_LONG).show();

                }else if (barnum==null){
                    Toast.makeText(RequestByBarcode.this, "الرجاء مسح الباركود", Toast.LENGTH_LONG).show();
                } else {
                    uploadFile();}

            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {


                    // Create a storage reference from our app
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

                    // Create a reference to the file to delete
                    Toast.makeText(RequestByBarcode.this, douTable.toString(), Toast.LENGTH_SHORT).show();

                    // StorageReference desertRef = storageRef.child("images/Request/"+tableUri.toString()+".jpg");
                    StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(douTable);

                    // Delete the file
                    storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // File deleted successfully
                            Toast.makeText(RequestByBarcode.this, "تم الحذف", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Uh-oh, an error occurred!
                            Toast.makeText(RequestByBarcode.this, "error occurred", Toast.LENGTH_SHORT).show();

                        }
                    });}catch (Exception e){
                    Toast.makeText(RequestByBarcode.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            fImageUri = data.getData();

            Picasso.with(this).load(fImageUri).into(foodImage);
            try {
                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fImageUri);

                // After selecting image change choose button above text.
                upload.setText("تم اختيار الصورة");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == 3 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            tableUri = data.getData();

            Picasso.with(this).load(tableUri).into(tableI);
            try {
                // Getting selected image into Bitmap.
                Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), tableUri);

                // After selecting image change choose button above text.
                table.setText("تم اختيار الصورة");
                StorageReference fileReference1 = storageReference.child(System.currentTimeMillis()
                        + "." + getFileExtension(tableUri));
                // Setting progressDialog Title.


                tablem = fileReference1.putFile(tableUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //Toast.makeText(RequestByBarcode.this, "تم تحميل الصوره بنجاح", Toast.LENGTH_LONG).show();
                                douTable= taskSnapshot.getDownloadUrl().toString();

                                // Hiding the progressDialog after done uploading.
                                //  progressDialog.dismiss();


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Hiding the progressDialog after done uploading.
                                //progressDialog.dismiss();
                                Toast.makeText(RequestByBarcode.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
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

        if (fImageUri != null && tableUri!= null) {
            progressDialog.setMessage( "الرجاء الانتظار حتى يتم رفع الطلب" );
            progressDialog.show();

            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(fImageUri));

            StorageReference fileReference1 = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(tableUri));

            mUploadTask = fileReference.putFile(fImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Food RF =  new Food(name.getText().toString().trim(),taskSnapshot.getDownloadUrl().toString(),"لايوجد","لايوجد","لايوجد","لايوجد");
                            RF.setImageTable(douTable);
                            RF.setBarcodN(barnum);
                            String uploadId = databaseReference.push().getKey();
                            databaseReference.child(uploadId).setValue(RF);

                            // Hiding the progressDialog after done uploading.
                            progressDialog.dismiss();

                            AlertDialog.Builder alert = new AlertDialog.Builder(
                                    RequestByBarcode.this);
                            alert.setTitle("تم ارسال الطلب بنجاح").setIcon(R.drawable.t1);
                            alert.show();
                            startActivity(new Intent(RequestByBarcode.this, home_page_register.class));


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Hiding the progressDialog after done uploading.
                            progressDialog.dismiss();
                            Toast.makeText(RequestByBarcode.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "لم يتم اختيار الملف", Toast.LENGTH_SHORT).show();
        }
    }





    // menu
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
}

