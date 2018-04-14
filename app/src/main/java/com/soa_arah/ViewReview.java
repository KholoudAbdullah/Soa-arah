package com.soa_arah;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ViewReview extends AppCompatActivity {
    private RecyclerView mReviewList;

    private DatabaseReference mDatabase;
    private DatabaseReference deleteDatabase;
    FirebaseAuth mAuth;
    private Query reviewsQuery;
    private String Fname;
    private String user_key;
    private String key;
    private String R_key;
    public int pos;


    private Button deleteButton;
    private ProgressDialog progressDialog;
    android.app.AlertDialog.Builder alert;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_review);
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );

        isConnected();

        progressDialog = new ProgressDialog(ViewReview.this);


        Fname=getIntent().getStringExtra("name");
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Review").child(Fname);
        deleteDatabase = FirebaseDatabase.getInstance().getReference().child("Review").child(Fname);
        mAuth = FirebaseAuth.getInstance();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (mDatabase.hashCode()==0){
                    alert= new android.app.AlertDialog.Builder(ViewReview.this);
                    alert.setTitle("لاتوجد تعليقات");
                    alert.setMessage("العودة الى الصفحة السابقة");
                    alert.setCancelable(true);
                    alert.setPositiveButton(
                            "نعم",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Intent intent = new Intent(ViewReview.this, AddReview.class);
                                    intent.putExtra("name", Fname);
                                    startActivity(intent);
                                }
                            });
                    android.app.AlertDialog alert11 = alert.create();
                    alert11.show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        reviewsQuery = mDatabase.orderByKey();

//        reviewsQuery = mDatabase.orderByKey().equalTo(Fname);
if(mAuth.getCurrentUser()!=null){
        key = mAuth.getCurrentUser().getEmail();

        user_key =key.substring(0,key.lastIndexOf("@"));}
        mReviewList = findViewById(R.id.review_list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(ViewReview.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
//        mReviewList.setHasFixedSize(true);
        mReviewList.setLayoutManager(layoutManager);


//        deleteButton =(Button)findViewById(R.id.deleteButton);
                //the delete button


        //for the delete
//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               //here is the delete review code for the admin
//            }
//        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("error","error"+" 1");
//        FirebaseRecyclerOptions reviewsOptions = new FirebaseRecyclerOptions.Builder<Review>().setQuery(reviewsQuery, Review.class).build();

        FirebaseRecyclerAdapter<Review,ReviewViewHolder> FB_recAd = new FirebaseRecyclerAdapter<Review, ReviewViewHolder>(
                Review.class,
                R.layout.review_row,
                ReviewViewHolder.class,
                reviewsQuery

        ) {

            @Override
            public void populateViewHolder(final ReviewViewHolder holder, Review model, int position) {
//                return null;
                pos = position;
                pos = holder.getLayoutPosition();
                R_key = getRef(pos).getKey();
                Log.d("error","error"+" 2");
                holder.setComment(model.getComment());
                holder.setWriter(model.getWriter());
                holder.setNumLike(model.getNumLike());
                holder.setNumDisLike(model.getNumDisLike());
                holder.setDeleteButtonA(Fname,R_key, user_key);
                Log.d("error","error"+" 3");
//                View view = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.review_row, parent, false);
//
//                return new ReviewViewHolder(view);

                holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        alert= new android.app.AlertDialog.Builder(ViewReview.this);
                        alert.setMessage("هل انت متأكد من خذف التعليق؟");
                        alert.setCancelable(true);
                        alert.setPositiveButton(
                                "نعم",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        pos = holder.getLayoutPosition();
                                        R_key = getRef(pos).getKey();
                                        deleteDatabase=deleteDatabase.child(R_key);


                                        deleteDatabase.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                        deleteDatabase.removeValue();


                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
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

            }


        };
        Log.d("error","error"+" 4");
        mReviewList.setAdapter(FB_recAd);
        Log.d("error","error"+" 5");
    }


    public static class ReviewViewHolder extends RecyclerView.ViewHolder{

        Button deleteButton;
        DatabaseReference DeleteDatabase;
        FirebaseAuth mAuth;


        public ReviewViewHolder(View itemView) {
            super(itemView);
            Log.d("error","error"+" 6");
            View mView=itemView;
            deleteButton = (Button) mView.findViewById(R.id.deleteButton);
            DeleteDatabase = FirebaseDatabase.getInstance().getReference().child("Review");
            mAuth = FirebaseAuth.getInstance();
            Log.d("error","error"+" 7");

        }

        public void setDeleteButtonA(String Fname,String R_key, String user_key) {
//            int R_key=getRef(itemPosition).getKey();


            if(mAuth.getCurrentUser()!=null) {
                if (mAuth.getCurrentUser().getUid().equals("kstgUKiRA7T3p1NNl3GuGBHgvcf2")) {
                    deleteButton.setVisibility(View.VISIBLE);
                }
            }

        }

        public void setComment(String comment){

            Log.d("error","error"+" 8");
            TextView review_comment =(TextView) itemView.findViewById(R.id.review);
            Log.d("error","error"+" 9");
            review_comment.setText(comment);
            Log.d("error","error"+" 10");
        }

        public void setWriter(String writer){
            Log.d("error","error"+" 11");
            TextView review_writer =(TextView) itemView.findViewById(R.id.writer);
            Log.d("error","error"+" 12");
            review_writer.setText(writer);
            Log.d("error","error"+" 13");
        }

        public void setNumLike(String numLike){
            Log.d("error","error"+" 14");
            TextView review_numLike =(TextView) itemView.findViewById(R.id.numLike);
            Log.d("error","error"+" 15");
            review_numLike.setText(numLike);
            Log.d("error","error"+" 16");
        }
        public void setNumDisLike(String numDisLike){
            Log.d("error","error"+" 17");
            TextView review_numDisLike =(TextView) itemView.findViewById(R.id.numDisLike);
            Log.d("error","error"+" 18");
            review_numDisLike.setText(numDisLike);
            Log.d("error","error"+" 19");
        }
    }

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
