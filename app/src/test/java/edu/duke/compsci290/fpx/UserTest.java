package edu.duke.compsci290.fpx;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Serena on 4/29/18.
 */

public class UserTest {
    String mNetID = "sl362";
    boolean mIsGiving = true;
    String mYear = "2019";
    String mMajor = "ECE";
    String mName = "sERENA LIU";
    String mPhoneNumber = "7046141335";
    String mPhoto = "fjdsfjkdsbjkfbdsjkb";

    User u = new User(mNetID, mIsGiving,mYear,mMajor,mName,mPhoneNumber,mPhoto);

    @Test
    public void setAndGetNetIDTest() {
        assertEquals(u.getmNetID(), mNetID);
        mNetID = "3dd34";
       u.setmNetID(mNetID);
       assertEquals(u.mNetID, mNetID);

    }

    @Test
    public void setAndGetNameTest() {
        assertEquals(u.getmName(), mName);
        mName = "3dd34";
        u.setmName(mName);
        assertEquals(u.mName, mName);

    }

    @Test
    public void setAndGetmIsGivingTest() {
        assertEquals(u.getmIsGiving(), mIsGiving);
        mIsGiving = false;
        u.setmIsGiving(mIsGiving);
        assertEquals(u.mIsGiving, mIsGiving);

    }

    @Test
    public void setAndGetmPhoneNumber() {
        assertEquals(u.getmPhoneNumber(), mPhoneNumber);
        mPhoneNumber = "7093829473";
        u.setmPhoneNumber(mPhoneNumber);
        assertEquals(u.mPhoneNumber, mPhoneNumber);

    }

    @Test
    public void setAndGetmYear() {
        assertEquals(u.getmYear(), mYear);
        mYear = "2017";
        u.setmYear(mYear);
        assertEquals(u.mYear, mYear);

    }
    @Test
    public void setAndGetmMajor() {
        assertEquals(u.getmMajor(), mMajor);
        mMajor = "cs";
        u.setmMajor(mMajor);
        assertEquals(u.mMajor, mMajor);
    }





}
