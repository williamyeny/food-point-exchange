package edu.duke.compsci290.fpx;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Serena on 4/21/18.
 */

public class Profile {
    private String mName;
    private String mID;
    private List<Transaction> mTXList; //list of all transactions that a person has had
    private Boolean isSender; //sender or receiver? 1 if sender, 0 if receiver

    Profile(String name, String netID ) {
        mName = name;
        mID = netID;
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

    public String getID() {
        return mID;
    }

    public String getName() {
        return mName;
    }

    public void setStatus(boolean status) {
        isSender = status;
    }
    public boolean getStatus() {
        return isSender;
    }
}
