package com.soa_arah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import uk.co.senab.photoview.PhotoViewAttacher;

public class barcodeInfo extends AppCompatActivity {
    String cal, img, table, q, name, bar, f, id;
    private DatabaseReference fData;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_barcode_info );
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );

        progressDialog = new ProgressDialog(barcodeInfo.this);
        // Setting progressDialog Title.
        progressDialog.setMessage("الرجاء الانتظار ...");
        progressDialog.show();


        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAuth = FirebaseAuth.getInstance();
        bar = getIntent().getStringExtra( "bar" );
        name = getIntent().getStringExtra( "Name" );
        cal = getIntent().getStringExtra( "cal" );
        img = getIntent().getStringExtra( "image" );
        table = getIntent().getStringExtra( "table" );
        q = getIntent().getStringExtra( "q" );
        TextView textn = (TextView) findViewById( R.id.textView9 );
        textn.setText( bar );
        TextView textC = (TextView) findViewById( R.id.textView12 );
        ImageView imageView = (ImageView) findViewById( R.id.imageView7 );
        ImageView imageTable = (ImageView) findViewById( R.id.imageView10 );
        Glide.with( getApplicationContext() ).load( img ).into( imageView );
        Glide.with( getApplicationContext() ).load( table ).into( imageTable );
        textn.setText( name );
        PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher( imageTable );
        photoViewAttacher.update();
        // Hiding the progressDialog after done uploading.
        progressDialog.dismiss();

        if (cal.equals( "لا يوجد" )) {
            textC.setText( "خالي من السعرات الحرارية" );
        } else
            textC.setText( cal.toString() + " سعرة حرارية" );


        onBackPressed();
    }
    @Override
    public void onBackPressed()
    {

        // super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
    }


    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.home_1, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.Home) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


            if (user != null) {
                String id = user.getUid();
                //IT admin
                if(id.equals("kstgUKiRA7T3p1NNl3GuGBHgvcf2")){
                    startActivity(new Intent(getApplicationContext(),home_page_IT_admin.class));
                }
                // Nutrition addmin
                else if(id.equals("Pf7emnnQTEbmukAIDwWgkuv8JbC2")){
                    startActivity(new Intent(getApplicationContext(),home_page_Nutrition_admin.class));
                }
                else {
                    startActivity(new Intent(getApplicationContext(), home_page_register.class));

                }

            } else {
                startActivity( new Intent( getApplicationContext(), home_page_guest.class ) );

            }
        } else {
            return super.onOptionsItemSelected( item );
        }
        return true;

    }
}