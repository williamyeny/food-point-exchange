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
    private String mMajor;
    private int mYear;

    Profile(String name, String netID, String major, int year ) {
        mName = name;
        mID = netID;
        mMajor = major;
        mYear = year;
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
    public int getYear() {return mYear;}
    public String getMajor() {return mMajor;};

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
