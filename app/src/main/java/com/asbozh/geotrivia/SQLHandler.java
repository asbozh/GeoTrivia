package com.asbozh.geotrivia;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLHandler {


    public static final String KEY_ROWID = "_id";
    public static final String KEY_QUESTION = "Question";
    public static final String KEY_OPTION1 = "Option1";
    public static final String KEY_OPTION2 = "Option2";
    public static final String KEY_OPTION3 = "Option3";
    public static final String KEY_OPTION4 = "Option4";
    public static final String KEY_ANSWER = "Answer";

    private static final String DATABASE_NAME = "QuestionsDb";
    private static final String DATABASE_TABLE_GEO = "TABLE_GEO_Questions";
    private static final String DATABASE_TABLE_HIS = "TABLE_HIS_Questions";
    private static final String DATABASE_TABLE_BIO = "TABLE_BIO_Questions";
    private static final String DATABASE_TABLE_PHI = "TABLE_PHI_Questions";
    private static final int DATABASE_VERSION = 2;

    private DbHelper mHelper;
    private final Context mContext;
    private SQLiteDatabase mDatabase;

    private static class DbHelper extends SQLiteOpenHelper {


        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            Log.d("asbozh", "DbHelper constructor");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("asbozh", "SQL On Create");
            db.execSQL("CREATE TABLE " + DATABASE_TABLE_GEO + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_QUESTION + " TEXT NOT NULL, " +
                    KEY_OPTION1 + " TEXT NOT NULL, " +
                    KEY_OPTION2 + " TEXT NOT NULL, " +
                    KEY_OPTION3 + " TEXT NOT NULL, " +
                    KEY_OPTION4 + " TEXT NOT NULL, " +
                    KEY_ANSWER + " TEXT NOT NULL);");
            db.execSQL("CREATE TABLE " + DATABASE_TABLE_HIS + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_QUESTION + " TEXT NOT NULL, " +
                    KEY_OPTION1 + " TEXT NOT NULL, " +
                    KEY_OPTION2 + " TEXT NOT NULL, " +
                    KEY_OPTION3 + " TEXT NOT NULL, " +
                    KEY_OPTION4 + " TEXT NOT NULL, " +
                    KEY_ANSWER + " TEXT NOT NULL);");
            db.execSQL("CREATE TABLE " + DATABASE_TABLE_BIO + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_QUESTION + " TEXT NOT NULL, " +
                    KEY_OPTION1 + " TEXT NOT NULL, " +
                    KEY_OPTION2 + " TEXT NOT NULL, " +
                    KEY_OPTION3 + " TEXT NOT NULL, " +
                    KEY_OPTION4 + " TEXT NOT NULL, " +
                    KEY_ANSWER + " TEXT NOT NULL);");
            db.execSQL("CREATE TABLE " + DATABASE_TABLE_PHI + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_QUESTION + " TEXT NOT NULL, " +
                    KEY_OPTION1 + " TEXT NOT NULL, " +
                    KEY_OPTION2 + " TEXT NOT NULL, " +
                    KEY_OPTION3 + " TEXT NOT NULL, " +
                    KEY_OPTION4 + " TEXT NOT NULL, " +
                    KEY_ANSWER + " TEXT NOT NULL);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d("asbozh", "SQL On Upgrade");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_GEO);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_HIS);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_BIO);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_PHI);
            onCreate(db);
        }
    }

    public SQLHandler (Context c){
        mContext = c;
    }

    public SQLHandler open() throws SQLException {
        mHelper = new DbHelper (mContext);
        mDatabase = mHelper.getWritableDatabase();
        Log.d("asbozh", "SQL Handler open");
        return this;
    }
    public void close (){
        Log.d("asbozh", "SQL Handler close");
        mHelper.close();

    }

    public long createEntry(String tableName, String question, String option1, String option2, String option3, String option4, String answer) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_QUESTION, question);
        cv.put(KEY_OPTION1, option1);
        cv.put(KEY_OPTION2, option2);
        cv.put(KEY_OPTION3, option3);
        cv.put(KEY_OPTION4, option4);
        cv.put(KEY_ANSWER, answer);
        return mDatabase.insert(tableName, null, cv);
    }
    public String getQuestion (String tableName, int number) throws SQLException {
        String result = null;
        String[] columns = new String[] { KEY_ROWID, KEY_QUESTION, KEY_OPTION1, KEY_OPTION2, KEY_OPTION3, KEY_OPTION4, KEY_ANSWER };
        Cursor c = null;
        try {
            c = mDatabase.query(tableName, columns, KEY_ROWID + "=" + number, null, null, null, null);
            if (c != null) {
                c.moveToFirst();
                result = checkForNewLines(c.getString(1));
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return result;
    }

    public String getAnswer (String tableName, int number) throws SQLException {
        String result = null;
        String[] columns = new String[] { KEY_ROWID, KEY_QUESTION, KEY_OPTION1, KEY_OPTION2, KEY_OPTION3, KEY_OPTION4, KEY_ANSWER };
        Cursor c = null;
        try {
            c = mDatabase.query(tableName, columns, KEY_ROWID + "=" + number, null, null, null, null);
            if (c != null) {
                c.moveToFirst();
                result = c.getString(6);
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return result;
    }

    public String[] getOptions (String tableName, int number) throws SQLException {
        String options[] = new String[4];
        String[] columns = new String[] { KEY_ROWID, KEY_QUESTION, KEY_OPTION1, KEY_OPTION2, KEY_OPTION3, KEY_OPTION4, KEY_ANSWER };
        Cursor c = null;
        try {
            c = mDatabase.query(tableName, columns, KEY_ROWID + "=" + number, null, null, null, null);
            if (c != null) {
                c.moveToFirst();
                options[0] = c.getString(2);
                options[1] = c.getString(3);
                options[2] = c.getString(4);
                options[3] = c.getString(5);
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return options;
    }

    private String checkForNewLines(String question) {

        if (question.contains("~N")) {
            question = question.replace("~N", "\n");
        }
        return question;
    }



}
