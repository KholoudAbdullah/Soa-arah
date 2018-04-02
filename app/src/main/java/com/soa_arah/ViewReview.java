package com.soa_arah;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
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


    private Button deleteButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_review);
        setRequestedOrientation( ActivityInfo. SCREEN_ORIENTATION_PORTRAIT );

        Fname=getIntent().getStringExtra("name");
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Review").child(Fname);
        mAuth = FirebaseAuth.getInstance();
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


        deleteButton =(Button)findViewById(R.id.deleteButton);
                //the delete button
        if(mAuth.getCurrentUser()!=null) {
            if (mAuth.getCurrentUser().getUid().equals("aSK7RyMA8xfdaQNPF0xS6kAumam2")) {
                deleteButton.setVisibility(View.VISIBLE);
            }
        }

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
            public void populateViewHolder(ReviewViewHolder holder,  Review model, int position) {
//                return null;
                R_key = getRef(position).getKey();
                Log.d("error","error"+" 2");
                holder.setComment(model.getComment());
                holder.setWriter(model.getWriter());
                holder.setNumLike(model.getNumLike());
                holder.setNumDisLike(model.getNumDisLike());
                Log.d("error","error"+" 3");
//                View view = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.review_row, parent, false);
//
//                return new ReviewViewHolder(view);

                holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteDatabase=deleteDatabase.child(R_key);

                        deleteDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(mAuth.getCurrentUser()!=null){

                                    if(mAuth.getCurrentUser().getUid().equals("aSK7RyMA8xfdaQNPF0xS6kAumam2")){

                                        deleteDatabase.removeValue();


                                    }}


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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

        public ReviewViewHolder(View itemView) {
            super(itemView);
            Log.d("error","error"+" 6");
            View mView=itemView;
            Log.d("error","error"+" 7");

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


//    public void toAdd(View view){
//        Intent intent = new Intent(ViewReview.this, AddReview.class);
//        intent.putExtra("name",getIntent().getStringExtra("name"));
//        startActivity(intent);
//    }
}
