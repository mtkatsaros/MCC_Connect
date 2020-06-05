package edu.miracosta.cs134.mkatsaros.mccconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import edu.miracosta.cs134.mkatsaros.mccconnect.Model.Review;
import edu.miracosta.cs134.mkatsaros.mccconnect.Model.Staff;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    CollectionReference mReference;
    DocumentReference mDocRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Staff>  mAllStaffList;
    private ArrayList<Review> mAllReviewsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        mAllStaffList = populateAllStaff();




        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.putParcelableArrayListExtra("AllStaffList", mAllStaffList);
                startActivity(intent);
                finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 3000);
    }

    public ArrayList<Review> populateAllReviews(String email){
        final ArrayList<Review> reviewsList = new ArrayList<>();
        mReference = db.collection(LoginActivity.COLLECTION_STAFF);
        mDocRef = mReference.document(email);
        final String fEmail = email;
        mDocRef.collection(Review.COLLECTION_REVIEW).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Review tempReview;
                    String uEmail;
                    for (QueryDocumentSnapshot document : task.getResult()){
                        tempReview = new Review();
                        uEmail = document.getString(Review.FIELD_USER);
                        for(Staff s : mAllStaffList){
                            if(s.getId().equals(uEmail))
                                tempReview.setUser(s);
                        }
                        for(Staff s : mAllStaffList){
                            if(s.getId().equals(fEmail))
                                tempReview.setRecipient(s);
                        }
                        tempReview.setComment(document.getString(Review.FIELD_COMMENT));
                        tempReview.setStars(Integer.parseInt(document.getString(Review.FIELD_STARS)));
                        reviewsList.add(tempReview);
                    }
                }
            }
        });
        return reviewsList;
    }

    public ArrayList<Staff> populateAllStaff(){
        mAllStaffList = new ArrayList<>();
        mReference = db.collection(LoginActivity.COLLECTION_STAFF);
        mReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    String imageURI;
                        Staff temp;
                    for(QueryDocumentSnapshot document : task.getResult()){
                        temp = new Staff();
                        temp.setId(document.getId());
                        temp.setFirst(document.getString(Staff.FIELD_FIRST));
                        temp.setLast(document.getString(Staff.FIELD_LAST)) ;
                        temp.setDepartment(document.getString(Staff.FIELD_DEPARTMENT));
                        temp.setStars(Integer.parseInt("" + document.get(Staff.FIELD_STARS)));
                        imageURI = document.getString(Staff.FIELD_IMAGE_URI);
                        Uri uri = null;
                        if(imageURI != null && !imageURI.equals(""))
                            uri = Uri.parse(imageURI);
                        temp.setImageURI(uri);
                        mAllStaffList.add(temp);

                    }

                    //TextView textView = findViewById(R.id.textView4);
                    //textView.setText(temp.getFirst());
                    Log.d("MCC Connect", "Snapshot complete ");
                }
                else
                    Log.d("MCC Connect", "Snapshot unsuccessful.");
            }
        });
        return mAllStaffList;
    }
}
