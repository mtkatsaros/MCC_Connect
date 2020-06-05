package edu.miracosta.cs134.mkatsaros.mccconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.miracosta.cs134.mkatsaros.mccconnect.Model.Review;
import edu.miracosta.cs134.mkatsaros.mccconnect.Model.Staff;

public class WriteReviewActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;
    DocumentReference mDocReference;
    CollectionReference mColReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ArrayList<Staff>  mAllStaffList;
    private Spinner staffSpinner;
    private EditText commentEditText;
    EditText starsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        staffSpinner = findViewById(R.id.staffSpinner);
        ArrayAdapter adapter = new ArrayAdapter<>(this
                , android.R.layout.simple_spinner_item, filterStaffMembers());
        staffSpinner.setAdapter(adapter);

        commentEditText = findViewById(R.id.commentEditText);
        starsEditText = findViewById(R.id.starsEditText);


    }

    /**
     * add onCompleteListener. if it can load the Collection "reviews", simply add a new
     * document. else, add a collection fields and then insert the document. Also add more
     * stars to the recipient.
     * @param v
     */
    public void submitComment(View v){
        //staff/recipient's email
        Staff recipient = mAllStaffList.get(staffSpinner.getSelectedItemPosition());
        mDocReference = db.collection(LoginActivity.COLLECTION_STAFF).document(
                recipient.getId());
        mColReference = mDocReference.collection(Review.COLLECTION_REVIEW);

        //increase the number of stars the recipient has and add to database
        recipient.setStars(recipient.getStars() + Integer.parseInt(starsEditText.getText().toString()));
        mDocReference.update(Staff.FIELD_STARS, recipient.getStars());

        mColReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Map<String, Object> review = new HashMap<>();
                review.put(Review.FIELD_COMMENT, commentEditText.getText().toString());
                review.put(Review.FIELD_STARS, starsEditText.getText().toString());
                review.put(Review.FIELD_USER, mCurrentUser.getEmail());

                //many SuccessListeners later... :/
                if(task.isSuccessful())
                    mColReference.add(review);
                else {
                    mDocReference.set(Review.COLLECTION_REVIEW);
                    mColReference.add(review);
                }
            }
        });

        Intent intent = getIntent();
        intent.setClass(this, ReviewSuccessActivity.class);
        startActivity(intent);
    }


    /**
     * left out filter for now; it will just make the allStaffList become a String list
     * @return
     */
    public ArrayList<String> filterStaffMembers(){
        ArrayList<String> staffList = new ArrayList<>();
        mAllStaffList = getIntent().getParcelableArrayListExtra("AllStaffList");
        for(Staff s : mAllStaffList){
                staffList.add(s.getFirst() + " " + s.getLast());
        }
        return staffList;
    }
}
