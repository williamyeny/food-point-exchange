package edu.duke.compsci290.fpx;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * SQLite database for storing current userinformation
 * Created by jerry on 4/28/18.
 */

public final class UserContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private UserContract() {}

//    String mNetID;
//    boolean mIsGiving;
//    String mYear;
//    String mMajor;
//    String mName;
//    String mPhoneNumber;
//    String mPhoto;

    /* Inner class that defines the table contents */
    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "User";
        public static final String COLUMN_NETID = "mNetID";
        public static final String COLUMN_NAME = "mName";
        public static final String COLUMN_YEAR = "mYear";
        public static final String COLUMN_MAJOR = "mMajor";
        public static final String COLUMN_PHONENUMBER = "mPhoneNumber";
        public static final String COLUMN_PHOTO = "mPhoto";
        public static final String COLUMN_ISGIVING = "mIsGiving";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                    UserEntry._ID + " INTEGER PRIMARY KEY," +
                    UserEntry.COLUMN_NAME + " TEXT," +
                    UserEntry.COLUMN_YEAR + " TEXT,"+
                    UserEntry.COLUMN_MAJOR + " TEXT,"+
                    UserEntry.COLUMN_ISGIVING + " TEXT,"+
                    UserEntry.COLUMN_PHONENUMBER + " TEXT,"+
                    UserEntry.COLUMN_NETID + " TEXT,"+
                    UserEntry.COLUMN_PHOTO + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;


}
