package edu.miracosta.cs134.mkatsaros.mccconnect.Model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * @author Michael Katsaros
 * @version 1.0
 * the Staff Class helps organize individual staff members by storing dedicated
 * general information. the first and last name, along with the department and
 * an optional profile picture are included in this design.
 */

public class Staff implements Parcelable {
    private String mFirst, mLast, mDepartment, mId;
    private Uri mImageURI;
    private int mStars;


    //define static fields for database reference
    //originally had COLLECTION_STAFF in LoginActivity.
    //Just copypasted it here because I did not want to redo all of the previous references.
    public static final String COLLECTION_STAFF = "staff";
    public static final String FIELD_FIRST = "firstName";
    public static final String FIELD_LAST = "lastName";
    public static final String FIELD_DEPARTMENT = "department";
    public static final String FIELD_IMAGE_URI = "imageURI";
    public static final String FIELD_STARS = "stars";

    /**
     * 4 constructors:
     * first constructor includes all values minus URI or stars,
     * second constructor is also without an id. The third constructor has everything minus URI.
     * The imageURI is initialized as null and can be changed upon user request. The first and
     * second constructors were made before fully understanding Firebase, but are left in
     * with the possibility that a developer finds a use for them. Finally, a fourth
     * default constructor as extrapolating data from Firebase is done from a nested
     * inner class, which needs fields to be instantiated outside for them to work.
     */

    public Staff(){
        this("", "", ", ");
    }

    public Staff(String id, String first, String last, String department, int stars){
        mId = id;
        mFirst = first;
        mLast = last;
        mDepartment = department;
        mImageURI = null;
        mStars = stars;
    }

    public Staff(Parcel in) {
        mFirst = in.readString();
        mLast = in.readString();
        mDepartment = in.readString();
        mId = in.readString();
        mImageURI = in.readParcelable(Uri.class.getClassLoader());
        mStars = in.readInt();
    }

    public static final Creator<Staff> CREATOR = new Creator<Staff>() {
        @Override
        public Staff createFromParcel(Parcel in) {
            return new Staff(in);
        }

        @Override
        public Staff[] newArray(int size) {
            return new Staff[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Staff staff = (Staff) o;
        return mStars == staff.mStars &&
                mFirst.equals(staff.mFirst) &&
                mLast.equals(staff.mLast) &&
                mDepartment.equals(staff.mDepartment) &&
                mId.equals(staff.mId) &&
                Objects.equals(mImageURI, staff.mImageURI);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mFirst, mLast, mDepartment, mId, mImageURI, mStars);
    }

    @Override
    public String toString() {
        return "Staff{" +
                "mFirst='" + mFirst + '\'' +
                ", mLast='" + mLast + '\'' +
                ", mDepartment='" + mDepartment + '\'' +
                ", mId='" + mId + '\'' +
                ", mImageURI=" + mImageURI +
                ", mStars=" + mStars +
                '}';
    }

    public Staff(String id, String first, String last, String department) {
        this(id, first, last, department, 0);
    }

    public int getStars() {
        return mStars;
    }

    public Staff(String first, String last, String department) {
        this("-1", first, last, department);
    }

    /**
     * void addStars increases the amount of total stars each user has by an integer value.
     * removeStars method deemed unnecessary as negative integers are allowed in this method.
     * @param stars
     */
    public void addStars(int stars){
        mStars += stars;
    }

    public String getId() {
        return mId;
    }

    public void setStars(int stars) {
        mStars = stars;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getFirst() {
        return mFirst;
    }

    public void setFirst(String first) {
        mFirst = first;
    }

    public String getLast() {
        return mLast;
    }

    public void setLast(String last) {
        mLast = last;
    }

    public String getDepartment() {
        return mDepartment;
    }

    public void setDepartment(String department) {
        mDepartment = department;
    }

    public Uri getImageURI() {
        return mImageURI;
    }

    public void setImageURI(Uri imageURI) {
        mImageURI = imageURI;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mFirst);
        dest.writeString(mLast);
        dest.writeString(mDepartment);
        dest.writeString(mId);
        dest.writeParcelable(mImageURI, flags);
        dest.writeInt(mStars);
    }
}
