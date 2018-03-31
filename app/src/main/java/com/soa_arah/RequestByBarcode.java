package com.soa_arah;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
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
    private TextView TXbarnum;
    private Button cancle,send,upload,table;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private StorageTask mUploadTask,tablem;
    private String douTable, barnum;
    private EditText name;
    private ProgressDialog progressDialog;
    android.app.AlertDialog.Builder alert;





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
        name=(EditText) findViewById(R.id.Fname);
        send=(Button) findViewById(R.id.sendw);
        cancle=(Button) findViewById(R.id.cancel);
        TXbarnum=(TextView)findViewById(R.id.barcodeNumber);
        name.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (name.getText().toString().trim().length()<1){

                    name.setError("االرجاء إدخال إسم الصنف");
                }
            }
        });
        progressDialog = new ProgressDialog(RequestByBarcode.this);

        name.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (name.getText().toString().trim().length()<1){

                    name.setError("االرجاء إدخال إسم الصنف");
                }
            }
        });

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
                if(name.getText().toString().trim().length()<1){
                    alert= new android.app.AlertDialog.Builder(RequestByBarcode.this);
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

                }else {
                uploadFile();
                }

            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert = new android.app.AlertDialog.Builder(RequestByBarcode.this);
                alert.setMessage("هل انت متاكد من إلغاء الإرسال");
                alert.setCancelable(true);
                alert.setPositiveButton(
                        "نعم",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (fImageUri != null){
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
                                            //Toast.makeText(RequestByBarcode.this, "تم الحذف", Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Uh-oh, an error occurred!
                                            //Toast.makeText(RequestByBarcode.this, "error occurred", Toast.LENGTH_SHORT).show();

                                        }
                                    });}catch (Exception e){
                                    Toast.makeText(RequestByBarcode.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }}

                                startActivity(new Intent(getApplicationContext(), home_page_register.class));

                            }
                        });

                alert.setNegativeButton(
                        "إلغاء",
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
        onBackPressed();
    }
    @Override
    public void onBackPressed()
    {

        // super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 3 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            tableUri = data.getData();


            try {
                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), tableUri);

                // After selecting image change choose button above text.
                table.setText("تم اختيار الصورة");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == 2 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            fImageUri = data.getData();


            try {
                // Getting selected image into Bitmap.
                Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), fImageUri);

                // After selecting image change choose button above text.
                upload.setText("تم اختيار الصورة");
                StorageReference fileReference1 = storageReference.child(System.currentTimeMillis()
                        + "." + getFileExtension(fImageUri));
                // Setting progressDialog Title.


                mUploadTask= fileReference1.putFile(fImageUri)
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
                    + "." + getFileExtension(tableUri));

            StorageReference fileReference1 = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(fImageUri));

            tablem = fileReference.putFile(tableUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Food RF =  new Food(name.getText().toString().trim(),douTable,"لايوجد","لايوجد","لايوجد","لايوجد");
                            RF.setImageTable( taskSnapshot.getDownloadUrl().toString());
                            RF.setBarcodN(barnum);
                            String uploadId = databaseReference.push().getKey();
                            databaseReference.child(uploadId).setValue(RF);

                            // Hiding the progressDialog after done uploading.
                            //progressDialog.dismiss();




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

            AlertDialog.Builder alert = new AlertDialog.Builder(
                    RequestByBarcode.this);
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
            startActivity(new Intent(RequestByBarcode.this, home_page_register.class));

        } else {
            alert= new android.app.AlertDialog.Builder(RequestByBarcode.this);
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
