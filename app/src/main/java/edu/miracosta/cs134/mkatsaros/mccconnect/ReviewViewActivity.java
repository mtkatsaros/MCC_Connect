package edu.miracosta.cs134.mkatsaros.mccconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

import edu.miracosta.cs134.mkatsaros.mccconnect.Model.Review;
import edu.miracosta.cs134.mkatsaros.mccconnect.Model.Staff;

public class ReviewViewActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference mCurrentDocRef;

    private List<Review> mReviewList;
    private ReviewListAdapter mReviewListAdapter;
    private TextView countingStarsTextView;
    private ListView reviewsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_view);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        countingStarsTextView = findViewById(R.id.countingStarsTextView);
        mCurrentDocRef = db.collection(LoginActivity.COLLECTION_STAFF)
                .document(Objects.requireNonNull(mCurrentUser.getEmail()));
        mCurrentDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                countingStarsTextView.setText("You currently have " + doc.get(Staff.FIELD_STARS) +
                        " stars:");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("MCC Connect", "Snapshot failed. ");
            }
        });

        mReviewList = getIntent().getParcelableArrayListExtra("");

        reviewsListView = findViewById(R.id.reviewsListView);
        mReviewListAdapter = new ReviewListAdapter(this, R.layout.review_list_item, mReviewList);
        reviewsListView.setAdapter(mReviewListAdapter);

    }

}
