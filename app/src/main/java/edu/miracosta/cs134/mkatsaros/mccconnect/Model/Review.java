package edu.miracosta.cs134.mkatsaros.mccconnect.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;


/**
 * @author Michael Katsaros
 * @version 1.0
 * This class is intended to store reviews from one staff member to another.
 * it has two Staff objects user and recipient, along with a String comment and
 * int stars as a number of points to give the user. In the class, no mutators are
 * needed, as we wouldn't want the ability to change the recipients, or the name of
 * the user sending the stars for that matter.
 */
public class Review implements Parcelable {
    public static final String COLLECTION_REVIEW = "reviews";
    public static final String FIELD_USER = "user";
    public static final String FIELD_STARS = "stars";
    public static final String FIELD_COMMENT = "comment";

    private String mComment, mId;
    private Staff mUser, mRecipient;
    private int mStars;


    public Review(String id, Staff user, Staff recipient, String comment, int stars) {
        mId = id;
        mUser = user;
        mRecipient = recipient;
        mComment = comment;
        mStars = stars;
    }

    public Review(){
        this("", null, null, "", 0);
    }

    protected Review(Parcel in) {
        mComment = in.readString();
        mId = in.readString();
        mUser = in.readParcelable(Staff.class.getClassLoader());
        mRecipient = in.readParcelable(Staff.class.getClassLoader());
        mStars = in.readInt();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @Override
    public String
    toString() {
        return "Review{" +
                "mComment='" + mComment + '\'' +
                ", mId='" + mId + '\'' +
                ", mUser=" + mUser +
                ", mRecipient=" + mRecipient +
                ", mStars=" + mStars +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return mStars == review.mStars &&
                mComment.equals(review.mComment) &&
                mId.equals(review.mId) &&
                mUser.equals(review.mUser) &&
                mRecipient.equals(review.mRecipient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mComment, mId, mUser, mRecipient, mStars);
    }

    public Review(Staff user, Staff recipient, String comment, int stars) {
        this("-1", user, recipient, comment, stars);
    }

    public void setId(String id) {
        mId = id;
    }

    public void setUser(Staff user) {
        mUser = user;
    }

    public void setRecipient(Staff recipient) {
        mRecipient = recipient;
    }

    public String getId() {
        return mId;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    public Staff getUser() {
        return mUser;
    }

    public Staff getRecipient() {
        return mRecipient;
    }

    public int getStars() {
        return mStars;
    }

    public void setStars(int stars) {
        mStars = stars;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mComment);
        dest.writeString(mId);
        dest.writeParcelable(mUser, flags);
        dest.writeParcelable(mRecipient, flags);
        dest.writeInt(mStars);
    }
}
