package edu.miracosta.cs134.mkatsaros.mccconnect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import edu.miracosta.cs134.mkatsaros.mccconnect.Model.Review;

/**
 * @author Michael Katsaros
 * @version 1.0
 * This ListAdapter displays the name of the user awarding the stars and a rating bar of
 * the number of stars awarded.
 */
public class ReviewListAdapter extends ArrayAdapter<Review> {
    private Context mContext;
    private int mResource;
    private List<Review> mReviewList;

    public ReviewListAdapter(@NonNull Context context, int resource, @NonNull List<Review> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        mReviewList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(mResource, null);

        LinearLayout reviewListLinearLayout = view.findViewById(R.id.reviewListLinearLayout);
        Review selectedReview = mReviewList.get(position);

        TextView reviewUserTextView = view.findViewById(R.id.reviewUserTextView);
        reviewUserTextView.setText(selectedReview.getUser().getFirst() + " "
                + selectedReview.getUser().getLast());
        RatingBar reviewRatingBar = view.findViewById(R.id.reviewRatingBar);
        reviewRatingBar.setNumStars(selectedReview.getStars());
        reviewRatingBar.setRating(Float.parseFloat(String.valueOf(selectedReview.getStars())));

        reviewListLinearLayout.setTag(selectedReview);

        return view;
    }
}
