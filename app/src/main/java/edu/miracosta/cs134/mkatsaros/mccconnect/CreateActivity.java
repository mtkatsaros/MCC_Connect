package edu.miracosta.cs134.mkatsaros.mccconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.miracosta.cs134.mkatsaros.mccconnect.Model.Staff;

/**
 * @author Michael Katsaros
 * @version 1.0
 * This page should only be seen the first time a new user signs in. Once all of the
 * information is entered, their profile info is saved to the database and they will
 * automatically reach MainActivity upon sign in.
 */
public class CreateActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText firstEditText;
    private EditText lastEditText;
    private Spinner departmentSpinner;
    public static final String SPINNER_HINT = "Select a department:";
    private Context mContext = this;
    public static final String[] DEPS = new String[]{SPINNER_HINT, "CS", "MATH", "ENG", "ART"
            , "PHYS", "CHEM"}; //to be expanded upon; shortened for ease of testing


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        firstEditText = findViewById(R.id.firstEditText);
        lastEditText = findViewById(R.id.lastEditText);

        departmentSpinner = findViewById(R.id.departmentSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, DEPS);
        departmentSpinner.setAdapter(adapter);
    }

    /**
     * Add all of the information into the database if all the user info has been entered.
     * Else, remain on the page and prompt the user to enter all information needed.
     * @param v
     */
    public void toMain(View v){
        final String first = firstEditText.getText().toString();
        String last = lastEditText.getText().toString();
        String dept = departmentSpinner.getSelectedItem().toString();
        if(first != null && !first.equals("") && last != null && !last.equals("")
                && dept != null && !dept.equals("") && !dept.equals(SPINNER_HINT)){

            //Create a HashMap of key-value information and add it to the database
            Map<String, Object> user = new HashMap<>();
            user.put(Staff.FIELD_DEPARTMENT, dept);
            user.put(Staff.FIELD_FIRST, first);
            user.put(Staff.FIELD_LAST, last);
            user.put(Staff.FIELD_IMAGE_URI, null);
            user.put(Staff.FIELD_STARS, 0);

            //.document(...) used to make a custom document name
            db.collection(LoginActivity.COLLECTION_STAFF).document(mCurrentUser.getEmail())
                    .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("MCC Connect", "Document added successfully ");
                    Intent intent = getIntent();
                    intent.setClass(mContext, MainActivity.class);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("MCC Connect", "Error adding document ", e);
                    Toast.makeText(mContext, "A system error has occured. Please try again", Toast.LENGTH_LONG);
                }
            });


        }
        else
            Toast.makeText(this, "Please enter all required fields", Toast.LENGTH_LONG);
    }

    /**
     * sign the user out and bring them back to the main menu
     * @param v
     */
    public void toLogin(View v){
        Intent intent = getIntent();
        intent.setClass(this, LoginActivity.class);
        mAuth.signOut();
        startActivity(intent);
    }
}
