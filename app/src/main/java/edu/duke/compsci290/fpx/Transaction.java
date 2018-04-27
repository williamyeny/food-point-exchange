package edu.duke.compsci290.fpx;

import android.annotation.TargetApi;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by Serena on 4/21/18.
 * Represents a completed transaction between two people
 */

public class Transaction implements Serializable{

    private String mSenderID;
    private String mReceiverID;
    private int mAmount;
    private String mDate; //in form of mm-dd-yy

    Transaction(){
        mAmount = -1;
        mReceiverID = "";
        mDate = "";
        mSenderID = "";
    }

    @TargetApi(26)
    Transaction(String senderID, String receiverID, int amount) {

        //Assume today is the date for the transaction
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year  = localDate.getYear();
        int month = localDate.getMonthValue();
        int day   = localDate.getDayOfMonth();
        String monthdayyear = month + "-" + day + "-" + year;

        create(senderID, receiverID, amount, monthdayyear);
    }

    public void create(String senderID, String receiverID, int amount, String date) {
        mSenderID = senderID;
        mReceiverID = receiverID;
        mAmount = amount;
        mDate = date;
    }

    public String getmSenderID() {
        return mSenderID;
    }

    public String getmReceiverID() {
        return mReceiverID;
    }

    public int getmAmount() {
        return mAmount;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmSenderID(String mSenderID) {
        this.mSenderID = mSenderID;
    }

    public void setmReceiverID(String mReceiverID) {
        this.mReceiverID = mReceiverID;
    }

    public void setmAmount(int mAmount) {
        this.mAmount = mAmount;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }
}
