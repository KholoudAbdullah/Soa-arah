package com.soa_arah;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class RequestByBarcode extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;


    private Uri fImageUri,tableUri;
    private TextView TXbarnum;
    private Button cancle,send,upload,table;
    private StorageReference storageReference;
    private DatabaseReference databaseReference,checkdataF,checkdataR;
    private StorageTask mUploadTask,tablem;
    private String douTable, barnum,invalidChar,valid;
    private EditText name,cal;
    private String stander,calo;
    private ProgressDialog progressDialog,progressDialog1;
    android.app.AlertDialog.Builder alert;
    private RadioButton Rfood,Rdrink;
    private boolean flag2=true;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_by_barcode);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        isConnected();

        Intent intent = getIntent();
        barnum = intent.getExtras().getString("BarcodeNum", "");

        progressDialog1 = new ProgressDialog(RequestByBarcode.this);
        progressDialog1.setMessage("الرجاء الانتظار ..");
        progressDialog1.show();

        checkdataF = FirebaseDatabase.getInstance().getReference().child("Food");
        checkdataR = FirebaseDatabase.getInstance().getReference().child("Requests").child("ByBarcode");
        checkdataF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Food checkFood = snapshot.getValue(Food.class);
                    if (barnum.equals(checkFood.getBarcodN())) {
                        progressDialog1.dismiss();
                        alert = new android.app.AlertDialog.Builder(RequestByBarcode.this);
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

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Hiding the progressDialog after done uploading.
                progressDialog1.dismiss();

            }
        });

        progressDialog1.show();

        checkdataR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Food checkRequest = snapshot.getValue(Food.class);
                    if (barnum.equals(checkRequest.getBarcodN())) {
                        progressDialog1.dismiss();
                        alert = new android.app.AlertDialog.Builder(RequestByBarcode.this);
                        alert.setTitle("تم رفع طلب لهذا المنتج مسبقاً");
                        alert.setMessage("الإنتقال الى الصفحة الرئسية");
                        alert.setCancelable(true);
                        alert.setPositiveButton(
                                "موافق",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        startActivity(new Intent(getApplicationContext(), home_page_register.class));
                                    }
                                });

                        android.app.AlertDialog alert11 = alert.create();
                        alert11.show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Hiding the progressDialog after done uploading.
                progressDialog1.dismiss();

            }

        });
        // Hiding the progressDialog after done uploading.
        progressDialog1.dismiss();


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


        upload = (Button) findViewById(R.id.FImageB);
        table = (Button) findViewById(R.id.TImageB);
        name = (EditText) findViewById(R.id.Fname);
        send = (Button) findViewById(R.id.sendw);
        Rfood = (RadioButton) findViewById(R.id.food);
        Rdrink = (RadioButton) findViewById(R.id.drinkS);
        cancle = (Button) findViewById(R.id.cancel);
        TXbarnum = (TextView) findViewById(R.id.barcodeNumber);
        cal = (EditText) findViewById(R.id.cal);



        invalidChar ="!#$%&'()*+,./:;<=>?@[]^`{|}~0123456789";
        TXbarnum.setText(TXbarnum.getText().toString() + "  " + barnum);



        progressDialog = new ProgressDialog(RequestByBarcode.this);

        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (name.getText().toString().trim().length() < 1) {

                    name.setError("الرجاء إدخال إسم الصنف");
                }
                for (int i=0;i<name.getText().toString().trim().length();i++){
                    valid=name.getText().toString().trim().charAt(i)+"";
                    if (invalidChar.contains(valid)){
                        name.setError("الرجاء ادخال احرف فقط");

                    }
                }
            }
        });


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
                if (name.getText().toString().trim().length() < 1) {
                    alert = new android.app.AlertDialog.Builder(RequestByBarcode.this);
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
                }else if (flag2){
                    for (int i=0;i<name.getText().toString().trim().length();i++){
                        valid=name.getText().toString().trim().charAt(i)+"";
                        if (invalidChar.contains(valid)){
                            alert= new android.app.AlertDialog.Builder(RequestByBarcode.this);
                            alert.setMessage("الرجاء ادخال احرف فقط");
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
                    }
                } else if (!Rfood.isChecked() && !Rdrink.isChecked()) {
                    alert = new android.app.AlertDialog.Builder(RequestByBarcode.this);
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
                } else {
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
                                if (fImageUri != null) {
                                    try {


                                        // Create a storage reference from our app
                                        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

                                        // Create a reference to the file to delete
                                        //Toast.makeText(RequestByBarcode.this, douTable.toString(), Toast.LENGTH_SHORT).show();

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
                                        });
                                    } catch (Exception e) {
                                        Toast.makeText(RequestByBarcode.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                }

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
                table.setBackgroundResource(R.drawable.button_login);
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
                upload.setBackgroundResource(R.drawable.button_login);
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

                            //for standard measurement
                            if (Rfood.isChecked())
                                stander="جرام,ملعقة شاي,ملعقة اكل,كوب";
                            else if (Rdrink.isChecked())
                                stander="مليلتر,كوب";

                            if (cal.getText().toString().equals(""))
                                calo="لايوجد" ;
                            else
                                calo=cal.getText().toString().trim();


                            Food RF =  new Food(name.getText().toString().trim(),douTable,calo,stander,"لايوجد");
                            RF.setImageTable( taskSnapshot.getDownloadUrl().toString());
                            RF.setBarcodN(barnum);
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
                            Toast.makeText(RequestByBarcode.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            AlertDialog.Builder alert = new AlertDialog.Builder(
                    RequestByBarcode.this);
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
