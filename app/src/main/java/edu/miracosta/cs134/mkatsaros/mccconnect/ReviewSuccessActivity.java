package edu.miracosta.cs134.mkatsaros.mccconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ReviewSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_success);
    }

    public void toMenu(View v){
        Intent intent = getIntent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
    }

    public void toReview(View v){
        Intent intent = getIntent();
        intent.setClass(this, WriteReviewActivity.class);
        startActivity(intent);
    }
}
