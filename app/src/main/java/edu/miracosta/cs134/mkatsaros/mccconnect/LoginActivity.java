package edu.miracosta.cs134.mkatsaros.mccconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import edu.miracosta.cs134.mkatsaros.mccconnect.Model.Review;
import edu.miracosta.cs134.mkatsaros.mccconnect.Model.Staff;

/**
 * @author Michael Katsaros
 * @version 1.0
 * The login page will currently only support email and password.
 * There will be no create account option, as since this is a staff only service,
 * the accounts will be made on the server side.
 */

public class LoginActivity extends AppCompatActivity {

    public static final String COLLECTION_STAFF = "staff";
    private FirebaseAuth mAuth;
    public static final String TAG = "MCC Connect";
    private String mEmail;
    private String mPassword;
    protected Context mContext = this;



    CollectionReference mReference;
    DocumentReference mDocRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void createAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }



    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Incorrect email or password.",
                                    Toast.LENGTH_LONG).show();
                            EditText passwordEditText = findViewById(R.id.passwordEditText);
                            passwordEditText.setText("");
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    /**
     * The updateUI method contains two tests. The initial one is to see if the account can
     * sign in the first place. If the user can successfully sign in, it will then check to
     * see if the user has any profile information associated with the account. if so,
     * the user will be brought to MainActivity. Else, the user will be taken to CreateActivity
     * to finish the account profile.
     * @param account
     */
    public void updateUI(FirebaseUser account){
        if(account != null){
            Toast.makeText(this, "Sign in successful", Toast.LENGTH_LONG);

            //now check to see if there is profile info for this user
            FirebaseFirestore reference = FirebaseFirestore.getInstance();
            DocumentReference docRef = reference.collection(COLLECTION_STAFF)
                    .document(account.getEmail());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Intent intent = getIntent();
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){

                            intent.setClass(mContext, MainActivity.class);

                            startActivity(intent);
                        }
                        else{
                            intent.setClass(mContext, CreateActivity.class);
                            startActivity(intent);
                        }
                    }
                }
            });


        }
        else
            Toast.makeText(this, "Sign in unsuccessful. please try again.", Toast.LENGTH_LONG);
    }

    public void handleSigninButton(View v){
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);

        mEmail = emailEditText.getText().toString();
        mPassword = passwordEditText.getText().toString();

        if(mEmail != null && !mEmail.equals("") && mPassword != null && !mPassword.equals(""))
            signIn(mEmail, mPassword);
        else
            Toast.makeText(this, "Please enter all required fields.", Toast.LENGTH_LONG);
    }

}
