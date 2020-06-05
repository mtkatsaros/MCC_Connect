package edu.miracosta.cs134.mkatsaros.mccconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import edu.miracosta.cs134.mkatsaros.mccconnect.Model.Review;
import edu.miracosta.cs134.mkatsaros.mccconnect.Model.Staff;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference mDocRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DocumentReference mDocumentReference;
    private CollectionReference mReference;


    private TextView welcomeTextView;
    private Button starsButton;
    private MapView trafficMapView;
    private ArrayList<Review> mAllReviewsList;
    private ArrayList<Staff>  mAllStaffList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mAllReviewsList = populateAllReviews(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());


        //load all of the user db info and wire up the user specific UI
        db = FirebaseFirestore.getInstance();
        mDocumentReference = db.collection(LoginActivity.COLLECTION_STAFF)
                .document(Objects.requireNonNull(mCurrentUser.getEmail()));
        mDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                Log.i("MCC Connect", "Snapshot successful");
                String first = document.getString(Staff.FIELD_FIRST);
                String last = document.getString(Staff.FIELD_LAST);
                String department = document.getString(Staff.FIELD_DEPARTMENT);
                int stars = Integer.parseInt("" + document.get(Staff.FIELD_STARS)) ;
                String id = mCurrentUser.getEmail();
                Uri imgUri;
                String uri = document.getString(Staff.FIELD_IMAGE_URI);
                if(uri != null && uri != "")
                    imgUri = Uri.parse(uri);
                welcomeTextView = findViewById(R.id.welcomeTextView);
                welcomeTextView.setText("Welcome, " + first + "!");
                starsButton = findViewById(R.id.starsButton);
                starsButton.setText("" + stars);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("MCC Connect", "Snapshot Failed ");
            }
        });

        //TODO: wire up the MapView
        trafficMapView = findViewById(R.id.trafficMapView);
        GeoPoint gpEnd = new GeoPoint((int)33.190720, (int)-117.302179);

    }

    public void readReviews(View v){
        Intent intent = getIntent();
        intent.setClass(this, ReviewViewActivity.class);
        intent.putParcelableArrayListExtra("AllReviewsList", mAllReviewsList);
        startActivity(intent);
    }

    public void writeReview(View v){
        Intent intent = getIntent();
        intent.setClass(this, WriteReviewActivity.class);
        startActivity(intent);
    }

    public void logout(View v){
        Intent intent = getIntent();
        intent.setClass(this, LoginActivity.class);
        mAuth.signOut();
        startActivity(intent);
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
}
