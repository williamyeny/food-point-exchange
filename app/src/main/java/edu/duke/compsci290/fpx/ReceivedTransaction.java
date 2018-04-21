package edu.duke.compsci290.fpx;

/**
 * Created by Serena on 4/21/18.
 */

public class ReceivedTransaction implements Transaction{
    private String mSenderID;
    private String mReceiverID;
    private int mAmount;
    private String mDate; //in form of mm/dd/yy

    ReceivedTransaction(String senderID, String receiverID, int amount, String date) {
        create(senderID, receiverID, amount, date);
    }

    @Override
    public void create(String senderID, String receiverID, int amount, String date) {
        mSenderID = senderID;
        mReceiverID = receiverID;
        mAmount = amount;
        mDate = date;
    }

    @Override
    public int getAmount() {

        return mAmount;
    }

    @Override
    public String getDate() {
        return mDate;
    }

    @Override
    public String getSenderID() {
        return mSenderID;
    }

    @Override
    public String getReceiverID() {
        return mReceiverID;
    }
}
