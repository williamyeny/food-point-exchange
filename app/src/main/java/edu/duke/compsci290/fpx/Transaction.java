package edu.duke.compsci290.fpx;

/**
 * Created by Serena on 4/21/18.
 */

public interface Transaction {
    /*
    Transaction should have a sender, a receiver, a date, and an amt
     */


    void create(String senderID, String receiverID, int amount, String date);
    public int getAmount();
    public String getDate();
    public String getSenderID();
    public String getReceiverID();
}
