package com.soa_arah;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;

public class ViewReviewRegisterUser extends AppCompatActivity {
    private RecyclerView mReviewList;

    private DatabaseReference mDatabase;
    private DatabaseReference LikeDatabase;
    private DatabaseReference disLikeDatabase;
    private DatabaseReference deleteDatabase;

    FirebaseAuth mAuth;
    private Query reviewsQueryR;
    private String Fname;
    private String key;
    private boolean proLike = false;
    private boolean prodisLike = false;
    public String R_key;
    String user_key;
    public int pos;
    android.app.AlertDialog.Builder alert;
    private ProgressDialog progressDialog;



    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_review_register_user);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        isConnected();

        progressDialog = new ProgressDialog(ViewReviewRegisterUser.this);

        Fname = getIntent().getStringExtra("name");
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Review").child(Fname);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (mDatabase.hashCode() == 0) {
                    alert = new android.app.AlertDialog.Builder(ViewReviewRegisterUser.this);
                    alert.setTitle("لاتوجد تعليقات");
                    alert.setMessage("الانتقال لكتابة تعليق");
                    alert.setCancelable(true);
                    alert.setPositiveButton(
                            "موافق",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(ViewReviewRegisterUser.this, AddReview.class);
                                    intent.putExtra("name", Fname);
                                    startActivity(intent);
                                    //
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

        reviewsQueryR = mDatabase.orderByKey();

        LikeDatabase = FirebaseDatabase.getInstance().getReference().child("Review").child(Fname);
        disLikeDatabase = FirebaseDatabase.getInstance().getReference().child("Review").child(Fname);
        deleteDatabase = FirebaseDatabase.getInstance().getReference().child("Review").child(Fname);

        //check for this if there is an error
        LikeDatabase.keepSynced(true);
        disLikeDatabase.keepSynced(true);
        mDatabase.keepSynced(true);
        deleteDatabase.keepSynced(true);

//        reviewsQuery = mDatabase.orderByKey().equalTo(Fname);
        if (mAuth.getCurrentUser() != null) {
            key = mAuth.getCurrentUser().getEmail();
            user_key = key.substring(0, key.lastIndexOf("@"));
        }
        mReviewList = findViewById(R.id.review_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ViewReviewRegisterUser.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
//        mReviewList.setHasFixedSize(true);
        mReviewList.setLayoutManager(layoutManager);


        deleteButton = (Button) findViewById(R.id.deleteButton);
        //the delete button
//        if(mAuth.getCurrentUser()!=null) {
//            if (mAuth.getCurrentUser().getUid().equals("aSK7RyMA8xfdaQNPF0xS6kAumam2")) {
//                deleteButton.setVisibility(View.VISIBLE);
//            }
//        }

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
        Log.d("error", "error" + " 1");
//        FirebaseRecyclerOptions reviewsOptions = new FirebaseRecyclerOptions.Builder<Review>().setQuery(reviewsQuery, Review.class).build();

        FirebaseRecyclerAdapter<Review, ViewReviewRegisterUser.ReviewViewHolderR> FB_recAd = new FirebaseRecyclerAdapter<Review, ViewReviewRegisterUser.ReviewViewHolderR>(
                Review.class,
                R.layout.review_row2,
                ViewReviewRegisterUser.ReviewViewHolderR.class,
                reviewsQueryR
        ) {

            @Override
            public void populateViewHolder(final ViewReviewRegisterUser.ReviewViewHolderR holder, Review model, int position) {
//                return null;

                pos = position;
                pos = holder.getLayoutPosition();
                R_key = getRef(pos).getKey();
                Log.d("pos", " " + pos);
                holder.setComment(model.getComment());
                holder.setWriter(model.getWriter());
                holder.setNumLike(model.getNumLike());
                holder.setNumDisLike(model.getNumDisLike());
                holder.setLikebtn(R_key, Fname, user_key);
                holder.setDislikebtn(R_key, Fname, user_key);
                holder.setDeleteButton(Fname,R_key, user_key);
//                Log.d("error","error"+" 3");
//                View view = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.review_row, parent, false);
//

                holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alert= new android.app.AlertDialog.Builder(ViewReviewRegisterUser.this);
                        alert.setMessage("هل انت متأكد من حذف التعليق؟");
                        alert.setCancelable(true);
                        alert.setPositiveButton(
                                "نعم",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {


                                        pos = holder.getLayoutPosition();
                                        R_key = getRef(pos).getKey();
                                        deleteDatabase = deleteDatabase.child(R_key);

                        deleteDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild("RKey")) {
                                    String user_email = mAuth.getCurrentUser().getEmail();
                                    String RKey = user_email.substring(0, user_email.lastIndexOf("@"));
                                    if (dataSnapshot.child("RKey").getValue().equals(RKey)) {

                                                        deleteDatabase.removeValue();
                                        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(
                                                ViewReviewRegisterUser.this);
                                        alert.setTitle("تم حذف التعليق").setIcon(R.drawable.t1);

                                        android.support.v7.app.AlertDialog dialog = alert.create();

                                        // Finally, display the alert dialog
                                        dialog.show();
                                        alert.setPositiveButton(
                                                "نعم",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                        Intent intent = new Intent(ViewReviewRegisterUser.this, ViewReviewRegisterUser.class);
                                                        intent.putExtra("name", getIntent().getStringExtra("name"));
                                                        startActivity(intent);

                                                    }


                                                });
                                        android.support.v7.app.AlertDialog alert22 = alert.create();
                                        alert22.show();


                                                    }
                                                }


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

                holder.likebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pos = holder.getLayoutPosition();
                        Log.d("likebtn", " " + R_key);

//                        LikeDatabase=LikeDatabase.child(R_key);
                        proLike = true;

                        LikeDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                R_key = getRef(pos).getKey();
                                if (proLike) {
                                    if (dataSnapshot.child(R_key).hasChild("likes")) {
                                        if (dataSnapshot.child(R_key).child("likes").hasChild(user_key)) {
                                            if (dataSnapshot.child(R_key).child("likes").child(user_key).getValue().equals("like")) {
                                                LikeDatabase.child(R_key).child("likes").child(user_key).removeValue();
                                                int newNumLike = Integer.parseInt(dataSnapshot.child(R_key).child("numLike").getValue().toString());
                                                newNumLike = (newNumLike - 1);
                                                LikeDatabase.child(R_key).child("numLike").setValue(Integer.toString(newNumLike));

                                            } else if (dataSnapshot.child(R_key).child("likes").child(user_key).getValue().equals("dislike")) {
                                                LikeDatabase.child(R_key).child("likes").child(user_key).setValue("like");

                                                int newNumDisLike = Integer.parseInt(dataSnapshot.child(R_key).child("numDisLike").getValue().toString());
                                                newNumDisLike = (newNumDisLike - 1);
                                                LikeDatabase.child(R_key).child("numDisLike").setValue(Integer.toString(newNumDisLike));

                                                int newNumLike = Integer.parseInt(dataSnapshot.child(R_key).child("numLike").getValue().toString());
                                                newNumLike = (newNumLike + 1);
                                                LikeDatabase.child(R_key).child("numLike").setValue(Integer.toString(newNumLike));
                                            }
                                            proLike = false;


                                        }
                                    } else {
                                        LikeDatabase.child(R_key).child("likes").child(user_key).setValue("like");

                                        int newNumLike = Integer.parseInt(dataSnapshot.child(R_key).child("numLike").getValue().toString());
                                        newNumLike = (newNumLike + 1);
                                        LikeDatabase.child(R_key).child("numLike").setValue(Integer.toString(newNumLike));

                                        proLike = false;
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                });

                holder.dislikebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pos = holder.getLayoutPosition();
                        Log.d("dislikebtn", " " + R_key);

//                        disLikeDatabase=disLikeDatabase.child(R_key);
                        prodisLike = true;

                        disLikeDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                R_key = getRef(pos).getKey();
                                if (prodisLike) {
                                    if (dataSnapshot.child(R_key).hasChild("likes")) {
                                        if (dataSnapshot.child(R_key).child("likes").hasChild(user_key)) {
                                            if (dataSnapshot.child(R_key).child("likes").child(user_key).getValue().equals("dislike")) {
                                                disLikeDatabase.child(R_key).child("likes").child(user_key).removeValue();
                                                int newNumDisLike = Integer.parseInt(dataSnapshot.child(R_key).child("numDisLike").getValue().toString());
                                                newNumDisLike = (newNumDisLike - 1);
                                                disLikeDatabase.child(R_key).child("numDisLike").setValue(Integer.toString(newNumDisLike));

                                            } else if (dataSnapshot.child(R_key).child("likes").child(user_key).getValue().equals("like")) {
                                                disLikeDatabase.child(R_key).child("likes").child(user_key).setValue("dislike");

                                                int newNumLike = Integer.parseInt(dataSnapshot.child(R_key).child("numLike").getValue().toString());
                                                newNumLike = (newNumLike - 1);
                                                disLikeDatabase.child(R_key).child("numLike").setValue(Integer.toString(newNumLike));

                                                int newNumDisLike = Integer.parseInt(dataSnapshot.child(R_key).child("numDisLike").getValue().toString());
                                                newNumDisLike = (newNumDisLike + 1);
                                                disLikeDatabase.child(R_key).child("numDisLike").setValue(Integer.toString(newNumDisLike));
                                            }
                                            prodisLike = false;

                                        }
                                        prodisLike = false;
                                    } else {
                                        disLikeDatabase.child(R_key).child("likes").child(user_key).setValue("dislike");

                                        int newNumDisLike = Integer.parseInt(dataSnapshot.child(R_key).child("numDisLike").getValue().toString());

                                        newNumDisLike = (newNumDisLike + 1);
                                        disLikeDatabase.child(R_key).child("numDisLike").setValue(Integer.toString(newNumDisLike));

                                        prodisLike = false;
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                });


            }


        };
//        Log.d("error","error"+" 4");
        mReviewList.setAdapter(FB_recAd);
//        Log.d("error","error"+" 5");
    }


    public static class ReviewViewHolderR extends RecyclerView.ViewHolder implements View.OnClickListener {


        View mView;
        Button likebtn;
        Button dislikebtn;
        Button deleteButton;
        int itemPosition;

        DatabaseReference LikeDatabase;
        DatabaseReference disLikeDatabase;
        FirebaseAuth mAuth;


        DatabaseReference DeleteDatabase;

        public ReviewViewHolderR(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
//            Log.d("error","error"+" 6");
            mView = itemView;
//            Log.d("error","error"+" 7");
            likebtn = (Button) mView.findViewById(R.id.like);
            dislikebtn = (Button) mView.findViewById(R.id.dislike);
            deleteButton = (Button) mView.findViewById(R.id.deleteButton2);

            LikeDatabase = FirebaseDatabase.getInstance().getReference().child("Review");
            disLikeDatabase = FirebaseDatabase.getInstance().getReference().child("Review");
            mAuth = FirebaseAuth.getInstance();

            DeleteDatabase = FirebaseDatabase.getInstance().getReference().child("Review");

        }

        public void setLikebtn(String R_key, String Fname, final String user_key) {

            LikeDatabase = LikeDatabase.child(Fname).child(R_key);

            LikeDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("likes")) {
                        if (dataSnapshot.child("likes").hasChild(user_key)) {
                            if (dataSnapshot.child("likes").child(user_key).getValue().equals("like")) {
                                likebtn.setBackgroundResource(R.drawable.like_green);
                                dislikebtn.setBackgroundResource(R.drawable.thumb_down);
                            } else if (dataSnapshot.child("likes").child(user_key).getValue().equals("dislike")) {
                                likebtn.setBackgroundResource(R.drawable.thumb_up);
                                dislikebtn.setBackgroundResource(R.drawable.dislike_red);
                            }

                        } else if (!(dataSnapshot.child("likes").hasChild(user_key))) {
                            dislikebtn.setBackgroundResource(R.drawable.thumb_down);
                            likebtn.setBackgroundResource(R.drawable.thumb_up);
                        }

                    }
//                        else if(!(dataSnapshot.hasChild("likes"))){
//                    dislikebtn.setBackgroundResource(R.drawable.thumb_down);
//                    likebtn.setBackgroundResource(R.drawable.thumb_up);
//
//                }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        public void setDislikebtn(String R_key, String Fname, final String user_key) {

            disLikeDatabase = disLikeDatabase.child(Fname).child(R_key);

            disLikeDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("likes")) {
                        if (dataSnapshot.child("likes").hasChild(user_key)) {
                            if (dataSnapshot.child("likes").child(user_key).getValue().equals("dislike")) {

                                dislikebtn.setBackgroundResource(R.drawable.dislike_red);
                                likebtn.setBackgroundResource(R.drawable.thumb_up);

                            } else if (dataSnapshot.child("likes").child(user_key).getValue().equals("like")) {
                                dislikebtn.setBackgroundResource(R.drawable.thumb_down);
                                likebtn.setBackgroundResource(R.drawable.like_green);

                            }

                        } else if (!(dataSnapshot.child("likes").hasChild(user_key))) {
                            dislikebtn.setBackgroundResource(R.drawable.thumb_down);
                            likebtn.setBackgroundResource(R.drawable.thumb_up);
                        }

                    }
//                    else if(!(dataSnapshot.hasChild("likes"))){
//                        dislikebtn.setBackgroundResource(R.drawable.thumb_down);
//                        likebtn.setBackgroundResource(R.drawable.thumb_up);
//
//                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        public void setDeleteButton(String Fname,String R_key, String user_key) {
//            int R_key=getRef(itemPosition).getKey();

            DeleteDatabase = DeleteDatabase.child(Fname).child(R_key);
            DeleteDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("RKey")) {
                        String user_email = mAuth.getCurrentUser().getEmail();
                        String RKey = user_email.substring(0, user_email.lastIndexOf("@"));
                        if (dataSnapshot.child("RKey").getValue().equals(RKey)) {

                            deleteButton.setVisibility(View.VISIBLE);

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        public void setComment(String comment) {

//            Log.d("error","error"+" 8");
            TextView review_comment = (TextView) itemView.findViewById(R.id.review2);
//            Log.d("error","error"+" 9");
            review_comment.setText(comment);
//            Log.d("error","error"+" 10");
        }

        public void setWriter(String writer) {
//            Log.d("error","error"+" 11");
            TextView review_writer = (TextView) itemView.findViewById(R.id.writer2);
//            Log.d("error","error"+" 12");
            review_writer.setText(writer);
//            Log.d("error","error"+" 13");
        }

        public void setNumLike(String numLike) {
//            Log.d("error","error"+" 14");
            TextView review_numLike = (TextView) itemView.findViewById(R.id.numLike2);
//            Log.d("error","error"+" 15");
            review_numLike.setText(numLike);
//            Log.d("error","error"+" 16");
        }

        public void setNumDisLike(String numDisLike) {
//            Log.d("error","error"+" 17");
            TextView review_numDisLike = (TextView) itemView.findViewById(R.id.numDisLike2);
//            Log.d("error","error"+" 18");
            review_numDisLike.setText(numDisLike);

        }

        @Override
        public void onClick(View v) {

            final ArrayList<Review> reviewArrayLists = new ArrayList<>();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Review");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        reviewArrayLists.add(snapshot.getValue(Review.class));
                    }

                    itemPosition = getLayoutPosition();
                    Log.d("pos new","  "+itemPosition);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }


    }


    public void toAdd(View view) {
        Intent intent = new Intent(ViewReviewRegisterUser.this, AddReview.class);
        intent.putExtra("name", getIntent().getStringExtra("name"));
        startActivity(intent);
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