package edu.duke.compsci290.fpx;

/**
 * Created by Serena on 4/21/18.
 */

public class Transaction {
    private String mSenderID;
    private String mReceiverID;
    private int mAmount;
    private String mDate; //in form of mm-dd-yy

    Transaction(String senderID, String receiverID, int amount, String date) {
        create(senderID, receiverID, amount, date);
    }

    public void create(String senderID, String receiverID, int amount, String date) {
        mSenderID = senderID;
        mReceiverID = receiverID;
        mAmount = amount;
        mDate = date;
    }

    public int getAmount() {

        return mAmount;
    }

    public String getDate() {
        return mDate;
    }

    public String getSenderID() {
        return mSenderID;
    }

    public String getReceiverID() {
        return mReceiverID;
    }
}
