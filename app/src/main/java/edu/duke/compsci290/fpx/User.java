package edu.duke.compsci290.fpx;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jerry on 4/22/18.
 */

public class User implements Serializable {

    String mNetID;
    boolean mIsGiving;
    String mYear;
    String mMajor;
    String mName;
    String mPhoneNumber;
    String mPhoto;
    List<Transaction> mTXList; //list of all transactions that a person has had



    public User(String mNetID, boolean mIsGiving, String mYear, String mMajor, String mName, String mPhoneNumber, String mPhoto) {
        this.mNetID = mNetID;
        this.mIsGiving = mIsGiving;
        this.mYear = mYear;
        this.mMajor = mMajor;
        this.mName = mName;
        this.mPhoneNumber = mPhoneNumber;
        this.mPhoto = mPhoto;
    }

    public User(){

    }

    public String getmNetID() {
        return mNetID;
    }

    public void setmNetID(String mNetID) {
        this.mNetID = mNetID;
    }

    public boolean getmIsGiving() {
        return mIsGiving;
    }

    public void setmIsGiving(boolean mIsGiving) {
        this.mIsGiving = mIsGiving;
    }

    public String getmYear() {
        return mYear;
    }

    public void setmYear(String mYear) {
        this.mYear = mYear;
    }

    public String getmMajor() {
        return mMajor;
    }

    public void setmMajor(String mMajor) {
        this.mMajor = mMajor;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(String mPhoto) {
        this.mPhoto = mPhoto;
    }

    public void addTransaction(Transaction t) {
        if(mTXList == null ) {
            mTXList = new ArrayList<Transaction>();
        }
        mTXList.add(t);
    }

    public List<Transaction> getTransactionList() {
        return mTXList;

    }

}
